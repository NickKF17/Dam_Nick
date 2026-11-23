# Guía de PSP: Procesos e Hilos en Java (desde cero)

*Basada en tus apuntes UT1 (procesos) y UT2 (hilos). Pensada para quien parte sin saber nada: primero la idea, luego las clases y métodos, y siempre un ejemplo.*

## Cómo usar esta guía

1. Lee las secciones **en orden**: cada una se apoya en la anterior.
2. Cada concepto tiene: **qué es**, **para qué sirve** y **un ejemplo** que puedes copiar y ejecutar.
3. Al final hay una **chuleta** con todo resumido y una lista de **errores típicos de examen**.
4. Los nombres de ejemplo (`LeerNombre`, `Cola`, `Contador`...) son inventados por mí, pero siguen la misma idea que los de tus diapositivas.

## 0. Mapa del temario

| Bloque | Tema | Idea en una frase |
| --- | --- | --- |
| UT1 | Procesos | Desde mi programa Java lanzo **otros programas** y me comunico con ellos |
| UT1 | Streams | Cómo se lee y escribe información en Java (teclado, ficheros, procesos) |
| UT1 | Sincronización de procesos | Cómo evitar que dos procesos se pisen al usar el mismo fichero |
| UT2 | Hilos | Dentro de **un** programa, hago varias cosas a la vez |
| UT2 | Sincronización de hilos | `synchronized`, `wait/notify`, semáforos: que los hilos no se pisen |
| UT2 | ExecutorService | La forma moderna y recomendada de lanzar hilos |

---

## 1. Conceptos desde cero

### 1.1 Programa, proceso e hilo

- **Programa**: un archivo con instrucciones, guardado en disco. No hace nada por sí solo (por ejemplo `Calcular.class`).
- **Proceso**: un programa **en ejecución**. El sistema operativo (SO) le da su propia **memoria** (la *imagen de memoria*) y recursos (CPU, ficheros...). Cada proceso está aislado de los demás.
- **Hilo (thread)**: una secuencia de instrucciones que se ejecuta **dentro de un proceso**. Un proceso puede tener varios hilos y **todos comparten la memoria del proceso**.

Analogía: un **proceso** es un restaurante (su cocina, sus ingredientes, su local). Los **hilos** son los cocineros que trabajan dentro del mismo restaurante: comparten cocina e ingredientes, y por eso a veces se estorban entre sí.

|  | Proceso | Hilo |
| --- | --- | --- |
| Memoria | Propia, aislada | Compartida con los demás hilos del proceso |
| Crearlo | Pesado, lento | Ligero, rápido |
| Comunicación | Difícil: pipes, ficheros, sockets | Fácil: variables y objetos compartidos |
| Riesgo | Competir por recursos externos (ficheros) | Condiciones de carrera sobre variables compartidas |
| Puede existir solo | Sí | **No**, siempre vive dentro de un proceso |

Cada hilo tiene **lo suyo** (identificador, contador de programa, registros de CPU, pila de variables) y **comparte** con los otros el código, los datos (variables `static`, objetos) y los ficheros abiertos.

### 1.2 Concurrencia y paralelismo

- **Concurrencia**: varias tareas *avanzan a la vez en el tiempo*. Con una sola CPU se consigue **intercalando** (un trocito de A, un trocito de B...): *pseudoparalelismo*.
- **Paralelismo real**: varias tareas se ejecutan *literalmente en el mismo instante*, cada una en un núcleo distinto.

### 1.3 El planificador (scheduler)

El SO decide qué proceso/hilo usa la CPU en cada momento. Hay dos tipos de algoritmos:

- **Apropiativos**: el SO puede **expulsar** a un proceso de la CPU. Mejor aprovechamiento, más complejo. Es lo habitual en sistemas multiusuario y multitarea (Linux, Windows).
- **No apropiativos**: el proceso tiene la CPU hasta que termina o se bloquea. Más simples, menor rendimiento (sistemas batch).

Consecuencia clave: **no puedes predecir el orden de ejecución**. Por eso un programa concurrente puede dar resultados distintos en cada ejecución.

### 1.4 Tipos de procesos según su relación

- **Independientes**: no se comunican con nadie (los menos habituales).
- **Cooperativos**: colaboran para una tarea común (uno produce datos, otro los usa).
- **Competitivos**: necesitan el **mismo recurso** y luchan por él.

Cooperativos y competitivos necesitan **sincronización** y **comunicación**.

---

## 2. Procesos en Java

En `java.lang` hay dos clases clave:

- **`ProcessBuilder`**: *prepara* el proceso (qué comando, en qué carpeta, con qué variables, adónde redirigir la salida).
- **`Process`**: el proceso **ya en marcha**. Con él lo controlas (esperar, matar, leer su salida...).

Flujo de trabajo siempre igual: **configurar con `ProcessBuilder` → `start()` → obtienes un `Process` → lo controlas**.

### 2.1 Métodos de `ProcessBuilder`

| Método | Para qué sirve |
| --- | --- |
| `new ProcessBuilder("cmd", "arg1", ...)` | Constructor con *varargs*: comando y argumentos separados. También existe con `List<String>` |
| `start()` | Lanza el proceso y devuelve un `Process` |
| `command()` | Obtiene o cambia el comando y sus argumentos |
| `directory(File)` | Obtiene o cambia el directorio de trabajo (por defecto, el del proceso padre: `user.dir`) |
| `environment()` | Devuelve un `Map<String,String>` con las variables de entorno; puedes leerlas o modificarlas |
| `redirectInput(File)` | De dónde lee la entrada estándar el hijo |
| `redirectOutput(File)` | Adónde va la salida estándar del hijo |
| `redirectError(File)` | Adónde va la salida de errores del hijo |

### 2.2 Métodos de `Process`

| Método | Para qué sirve |
| --- | --- |
| `waitFor()` | **Bloquea** al padre hasta que el hijo termina; devuelve el código de salida |
| `waitFor(tiempo, unidad)` | Espera como máximo ese tiempo; devuelve `true` si terminó, `false` si no |
| `exitValue()` | Código de salida del hijo (lanza excepción si aún no ha terminado) |
| `isAlive()` | `true` si el proceso sigue ejecutándose |
| `destroy()` | Mata el proceso |
| `getInputStream()` | Para **leer lo que el hijo escribe** por su salida estándar |
| `getErrorStream()` | Para leer la salida de **errores** del hijo |
| `getOutputStream()` | Para **escribir al hijo** (es su entrada estándar) |

> **Ojo con los nombres, son contraintuitivos.** Se nombran desde el punto de vista del **padre**: `getInputStream` es lo que *entra al padre* (la salida del hijo); `getOutputStream` es lo que *sale del padre* (la entrada del hijo).

### 2.3 Ejemplo 1: lanzar un programa y esperar

```java
import java.io.IOException;

public class LanzarComando {
    public static void main(String[] args) {
        // Preparamos: comando "ls" con el argumento "-l" (en Windows sería "cmd", "/c", "dir")
        ProcessBuilder pb = new ProcessBuilder("ls", "-l");

        try {
            Process p = pb.start();            // se crea el proceso hijo
            int codigo = p.waitFor();          // el padre se queda esperando
            System.out.println("El hijo terminó con código: " + codigo);
        } catch (IOException e) {
            System.out.println("No se pudo lanzar el comando: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Me interrumpieron mientras esperaba");
        }
    }
}
```

**Qué ocurre:** `start()` lanza `ls -l` como proceso independiente. Padre e hijo corren concurrentemente; `waitFor()` hace que el padre espere. `start()` puede lanzar `IOException` (el comando no existe) y `waitFor()` puede lanzar `InterruptedException`.

### 2.4 Código de salida (exit code)

Es un número entero que el hijo le devuelve al padre para decirle **cómo terminó**:

- **0** = todo correcto.
- **Cualquier otro valor (1–255)** = algún error.

El hijo lo fija con `System.exit(valor)`. No tiene nada que ver con el texto que se pasan por los streams.

### 2.5 Ejemplo 2: esperar con tiempo límite y matar

```java
import java.util.concurrent.TimeUnit;

public class EsperarConTimeout {
    public static void main(String[] args) throws Exception {
        Process p = new ProcessBuilder("sleep", "30").start();   // tarda 30 s

        if (p.waitFor(5, TimeUnit.SECONDS)) {
            System.out.println("Terminó a tiempo");
        } else {
            System.out.println("Timeout: lo mato");
            p.destroy();                                         // forzamos el fin
        }
    }
}
```

### 2.6 Directorio de trabajo y variables de entorno

```java
import java.io.File;
import java.util.Map;

ProcessBuilder pb = new ProcessBuilder("java", "mispaquetes.Calcular");

pb.directory(new File("../CalcularParImpar/bin"));   // carpeta donde está el .class

Map<String, String> env = pb.environment();          // variables de entorno
env.put("MI_VARIABLE", "hola");                      // añadimos una
System.out.println(env.get("PATH"));                 // leemos una
```

El directorio de trabajo es el punto de partida para rutas relativas, ficheros y recursos del hijo.

### 2.7 Lanzar **tu propio** programa Java como hijo

En las actividades programas **dos proyectos**: el padre y el hijo. Reglas:

- Cada uno es un proyecto independiente con su propio `main`.
- El **hijo debe estar compilado** antes de lanzarlo.
- El padre debe conocer la **ruta** del hijo (relativa o absoluta) y el nombre completo de la clase (`paquete.Clase`).

```java
File directorio = new File("../CalcularParImpar/bin");              // ruta relativa a los .class
ProcessBuilder pb = new ProcessBuilder("java", "mispaquetes.Calcular");
pb.directory(directorio);
Process p = pb.start();
```

### 2.8 Comunicación padre ↔ hijo con streams

Un proceso lanzado con `start()` **no tiene consola propia**. Sus tres canales estándar (`stdin`, `stdout`, `stderr`) quedan conectados al padre:

| El hijo usa... | El padre lo ve con... | Dirección |
| --- | --- | --- |
| `System.in` (lee) | `p.getOutputStream()` | Padre **escribe** → hijo lee |
| `System.out` (escribe) | `p.getInputStream()` | Hijo escribe → padre **lee** |
| `System.err` (errores) | `p.getErrorStream()` | Hijo escribe → padre **lee** |

#### Ejemplo completo: `LeerNombre` (hijo) y `LlamarLeerNombre` (padre)

Es el ejercicio de tus diapositivas. El hijo cuenta las vocales de un nombre; si tiene menos de 3 letras, termina con error.

**Hijo – `LeerNombre.java`:**

```java
import java.util.Scanner;

public class LeerNombre {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);        // lee lo que le mande el padre
        String nombre = sc.nextLine();

        if (nombre.length() < 3) {
            System.exit(-1);                        // error (KO)
        }

        int vocales = 0;
        for (char c : nombre.toLowerCase().toCharArray()) {
            if ("aeiou".indexOf(c) >= 0) vocales++;
        }
        System.out.println(vocales);                // el padre leerá esto
        System.exit(0);                             // todo bien (OK)
    }
}
```

**Padre – `LlamarLeerNombre.java`:**

```java
import java.io.*;
import java.util.Scanner;

public class LlamarLeerNombre {
    public static void main(String[] args) throws Exception {
        ProcessBuilder pb = new ProcessBuilder("java", "LeerNombre");
        pb.directory(new File("../LeerNombre/bin"));
        Process p = pb.start();

        // 1) Escribirle el nombre al hijo. El salto de línea \n es IMPRESCINDIBLE
        OutputStream os = p.getOutputStream();
        os.write("Alejandro\n".getBytes());
        os.flush();                                  // vacía el buffer: sin esto puede no llegar

        // 2) Esperar y mirar cómo terminó
        int exitVal = p.waitFor();
        System.out.println("Valor de salida: " + exitVal);

        // 3) Si fue bien, leer su salida
        if (exitVal == 0) {
            Scanner sc = new Scanner(p.getInputStream());
            if (sc.hasNext()) {
                System.out.println("Vocales: " + sc.nextLine());
            }
            sc.close();
        }
    }
}
```

**Puntos clave que se repiten en los exámenes:**

- El `\n` al final del texto: el `Scanner.nextLine()` del hijo no termina hasta recibir un salto de línea.
- `flush()` tras `write()`.
- Comprobar `waitFor()` / `exitValue()` y **condicionar** el resto a que valga 0.
- Si el hijo escribe mucha salida, conviene leerla *mientras* corre (no solo al final), porque el buffer del pipe puede llenarse y bloquear al hijo.

### 2.9 Redirigir entrada/salida a ficheros

A veces quieres que la salida del hijo vaya directamente a un fichero (log, errores...) **sin tocar el código del hijo**. Se hace con `ProcessBuilder` **antes** de `start()`:

```java
ProcessBuilder pb = new ProcessBuilder("java", "LeerNombre");
pb.directory(new File("../LeerNombre/bin"));

pb.redirectInput(new File("nombre.txt"));    // el hijo lee de este fichero (en vez del teclado)
pb.redirectOutput(new File("minombre.txt")); // lo que escriba el hijo se guarda aquí
pb.redirectError(new File("error.txt"));     // y sus errores aquí

Process p = pb.start();
int codigo = p.waitFor();
```

Regla de oro: **la redirección se configura en `ProcessBuilder` (antes de arrancar); los streams se manejan en `Process` (con el proceso ya corriendo)**. Si rediriges un canal, el stream correspondiente de `Process` queda "vacío" (null/nulo) y ya no sirve leer de él.

Los ficheros se crean en la carpeta desde la que se ejecuta el proyecto, salvo que pongas una ruta concreta: `new File("/ruta/carpeta", "salida")`.

### 2.10 `Runtime.exec()` (alternativa antigua)

`Runtime.getRuntime().exec("comando")` también lanza procesos y devuelve un `Process`. Diferencia: con `ProcessBuilder` configuras todo *antes* de crear el proceso; con `Runtime` el comando se pasa al ejecutar. Está **deprecado desde Java 18** en algunas variantes, así que usa **`ProcessBuilder`**.

### 2.11 Ejercicio resuelto: abrir una web en el navegador

```java
import java.util.Scanner;

public class AbrirWeb {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Web a visitar: ");
        String url = sc.nextLine();

        new ProcessBuilder("google-chrome", url).start();   // lanza Chrome con la URL
    }
}
```

(El nombre del ejecutable depende del SO: `google-chrome` en Linux, `chrome` o `cmd /c start chrome` en Windows.)

---

## 3. Streams: cómo lee y escribe Java

Un **stream (flujo)** es un canal por el que viaja información en orden. El mismo código sirve para teclado, ficheros, procesos o red. Todo está en el paquete `java.io`. Hay dos familias:

|  | Entrada | Salida | Se usa para |
| --- | --- | --- | --- |
| **Bytes** | `InputStream` | `OutputStream` | Datos binarios (imágenes, cualquier cosa) |
| **Caracteres** | `Reader` | `Writer` | **Texto** (Unicode) |

Regla práctica: **texto → Reader/Writer; binario → InputStream/OutputStream**.

### 3.1 Clases de bytes importantes

- `FileInputStream` / `FileOutputStream`: leer/escribir un fichero en bytes.
- `BufferedInputStream` / `BufferedOutputStream`: añaden un buffer (más rápido).
- `DataInputStream` / `DataOutputStream`: leer/escribir tipos primitivos de Java.
- `ObjectInputStream` / `ObjectOutputStream`: leer/escribir objetos completos.
- `PrintStream`: la que tiene `print()` y `println()` (es el tipo de `System.out`).
- `PipedInputStream` / `PipedOutputStream`: tuberías (pipes) entre hilos.

Métodos de `InputStream`: `read()` (devuelve el siguiente byte, o **-1** si se acabó), `read(byte[])`, `available()`, `skip(n)`, `mark/reset`, `close()`. Métodos de `OutputStream`: `write(int)`, `write(byte[])`, `flush()` (fuerza el envío del buffer), `close()`.

### 3.2 Texto: `FileReader`, `FileWriter`, `BufferedReader`

```java
import java.io.*;

public class EjemploTexto {
    public static void main(String[] args) {

        // ESCRIBIR (true = añadir al final; false u omitido = sobrescribir)
        try (FileWriter fw = new FileWriter("pruebaTexto.txt", true)) {
            fw.write("Hola mundo\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // LEER línea a línea con buffer
        try (BufferedReader br = new BufferedReader(new FileReader("pruebaTexto.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {   // null = fin de fichero
                System.out.println(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

- `FileReader.read()` devuelve un carácter como `int` y **-1** al final.
- `BufferedReader.readLine()` devuelve una línea completa y **`null`** al final del fichero.
- `try (...)` se llama *try-with-resources*: cierra el recurso **solo** al terminar. Úsalo siempre que puedas.
- Para leer de un proceso hijo, la diapositiva usa `Scanner` envolviendo el `InputStream`: `new Scanner(p.getInputStream())`. También vale `new BufferedReader(new InputStreamReader(p.getInputStream()))`.

---

## 4. Sincronización entre procesos

### 4.1 Por qué hace falta

Cuando varios procesos corren a la vez pueden interferirse. Sincronizar sirve para:

- **Compartir recursos sin conflictos** (que dos procesos no escriban a la vez en el mismo fichero).
- **Mantener el orden correcto** (B no procesa un fichero hasta que A lo ha descargado: B *depende* de A).
- **Evitar condiciones de carrera**: el resultado depende del orden concreto en que se ejecutan los accesos.

**Ejemplo de conflicto:** dos procesos escriben 5 "A" y 5 "B" en el mismo fichero. Se esperaría `AAAAABBBBB`, pero puede salir `AABABBBAAB`, `BBBBBAAAAA`... El SO garantiza que cada `write` individual es **atómico**, pero escrituras separadas se pueden **intercalar**.

### 4.2 Vocabulario esencial

| Término | Significado |
| --- | --- |
| **Región / sección crítica** | Conjunto de instrucciones que usan un recurso y deben ejecutarse **en exclusión mutua** |
| **Exclusión mutua (mutex)** | Mientras uno usa el recurso, ningún otro puede |
| **Lock (bloqueo)** | Tener el uso exclusivo de un recurso |
| **Condición de carrera** | Resultado que depende del orden/velocidad de ejecución |
| **Deadlock (interbloqueo)** | Cada proceso espera un recurso que tiene otro; **nadie** puede avanzar nunca |
| **Inanición (starvation)** | Un proceso espera indefinidamente porque otros siempre pasan antes |
| **Sincronización condicional** | Solo se accede al recurso cuando está en cierto estado (p. ej. "hay dato disponible") |

**Ejemplo de deadlock:** P1 tiene el recurso X y pide Y; P2 tiene Y y pide X. Ninguno cede: bloqueo eterno. Es como dos coches en un cruce estrecho que se esperan mutuamente.

### 4.3 Problema lectores–escritores (ficheros)

- **Solo lectura**: el SO deja que muchos procesos lean a la vez.
- **Escritura o lectura-escritura**: se pide acceso **exclusivo**; los demás esperan.

### 4.4 Los semáforos (idea general)

Un **semáforo** es un contador que controla cuántos procesos pueden usar un recurso a la vez. Analogía: un baño público con 3 cabinas y un contador de cabinas libres.

- Al entrar, el contador **baja 1**; al salir, **sube 1**.
- Si vale **0**, todas están ocupadas: hay que **esperar**.
- **Binario**: solo 0 o 1 (verde/rojo). **General**: cualquier entero no negativo.

Ventaja: sirven para cualquier problema. Inconveniente: son de bajo nivel y un descuido (olvidar liberar, cambiar el orden) provoca bloqueos.

### 4.5 Sincronizar procesos en Java

Java **no** tiene palabras reservadas para sincronizar *procesos* (para hilos sí: `synchronized`). Entre procesos se usan recursos externos: **ficheros** como semáforo, **bloqueo de ficheros** (`FileLock`), pipes, sockets o una base de datos.

#### a) Semáforo con `File` — versión INCORRECTA

```java
File lock = new File("lock.tmp");
while (lock.exists()) {          // ¿está ocupado?
    Thread.sleep(100);
}
lock.createNewFile();            // lo ocupo
// ... usar el recurso ...
lock.delete();                   // lo libero
```

**Por qué falla:** dos procesos pueden llegar a `exists()` a la vez, ver ambos que no existe, y entrar los dos. Comprobar y crear son **dos pasos separados**, no atómicos.

#### b) Semáforo con `File` — versión MEJORADA

```java
File lock = new File("lock.tmp");
while (!lock.createNewFile()) {  // intenta crearlo; false = ya existía (ocupado)
    Thread.sleep(100);           // espera activa
}
try {
    // ... usar el recurso ...
} finally {
    lock.delete();               // SIEMPRE se libera, aunque haya error
}
```

`createNewFile()` comprueba y crea en **una sola operación atómica**. Sigue teniendo *espera activa* (el proceso duerme y reintenta), lo cual es poco elegante.

#### c) Semáforo con `FileChannel` y `FileLock` — la forma RECOMENDADA

```java
import java.io.*;
import java.nio.channels.*;

File lockFile = new File("lock.tmp");
lockFile.createNewFile();

try (FileOutputStream fos = new FileOutputStream(lockFile);
     FileChannel channel = fos.getChannel();
     FileLock lock = channel.lock()) {      // AQUÍ se bloquea si otro proceso lo tiene

    // Sección crítica: solo un proceso a la vez llega aquí

}   // al salir del try, el FileLock se libera solo (es AutoCloseable)
```

- `channel.lock()` **bloquea de verdad** al proceso (sin espera activa) hasta conseguir el cerrojo.
- Al salir del `try`, se libera automáticamente.
- **Importante:** usa un fichero **específico solo para el bloqueo**. No uses el mismo objeto para bloquear y para escribir, ni `PrintWriter`/`BufferedWriter` sobre él: no se sincronizan bien.

---

## 5. Hilos (threads): fundamentos

### 5.1 Qué son y por qué usarlos

Un **hilo** es una secuencia de código que se ejecuta dentro de un proceso. Todo programa Java arranca con un **hilo principal** (el que ejecuta `main`), y desde él puedes crear más.

Beneficios:

- **Capacidad de respuesta**: una tarea larga no congela el resto del programa.
- **Compartir recursos**: comparten memoria, crearlos no reserva memoria extra.
- **Paralelismo real** si hay varios núcleos.
- **Prioridades** para tareas críticas.

**Ejemplo mental (los ratones de tus apuntes):** 3 ratones comen durante 6, 5 y 7 segundos.

- **Secuencial** (un solo hilo): comen uno tras otro. Tiempo total = suma = **18 s**.
- **Concurrente** (un hilo por ratón): comen a la vez. Tiempo total ≈ el más lento = **7 s**.

### 5.2 Estados de un hilo

| Estado (apuntes) | Significado | En Java (`Thread.State`) |
| --- | --- | --- |
| **Nuevo** | Creado con `new`, aún no `start()` | `NEW` |
| **Runnable / Listo** | Preparado para ejecutar; puede estar en CPU o esperando turno | `RUNNABLE` |
| **Bloqueado / No ejecutable** | Dormido (`sleep`), esperando E/S, en `wait()`, o esperando un cerrojo | `BLOCKED`, `WAITING`, `TIMED_WAITING` |
| **Terminado / Muerto** | Acabó su `run()` (o excepción) | `TERMINATED` |

`hilo.getState()` devuelve el estado actual. **Un hilo muerto no se puede reiniciar** con `start()`: hay que crear otro con `new`.

El método `isAlive()` devuelve `true` desde `start()` hasta que muere.

### 5.3 Crear un hilo: dos formas

En ambas, **lo que hace el hilo va en el método `run()`**, y se arranca con **`start()`**.

#### Forma 1: extender `Thread`

```java
public class Raton extends Thread {
    private String nombre;
    private int segundos;

    public Raton(String nombre, int segundos) {
        this.nombre = nombre;
        this.segundos = segundos;
    }

    @Override
    public void run() {                                  // el trabajo del hilo
        System.out.println(nombre + " empieza a comer");
        try {
            Thread.sleep(segundos * 1000L);              // simula tardar en comer
        } catch (InterruptedException e) {
            return;                                      // me interrumpieron: salgo
        }
        System.out.println(nombre + " terminó");
    }

    public static void main(String[] args) {
        new Raton("Mickey", 6).start();                  // start(), NO run()
        new Raton("Jerry", 5).start();
        new Raton("Pinky", 7).start();
    }
}
```

#### Forma 2: implementar `Runnable`

```java
public class TareaRaton implements Runnable {
    private String nombre;
    public TareaRaton(String nombre) { this.nombre = nombre; }

    @Override
    public void run() {
        System.out.println(nombre + " en el hilo " + Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        Thread t = new Thread(new TareaRaton("Mickey"));   // envolvemos el Runnable en un Thread
        t.start();

        // También con lambda (Runnable tiene un solo método):
        new Thread(() -> System.out.println("Hola desde una lambda")).start();
    }
}
```

|  | `extends Thread` | `implements Runnable` |
| --- | --- | --- |
| Ventaja | Más sencillo, hereda métodos útiles | Más general: tu clase puede **heredar de otra** |
| Limitación | Solo puedes extender una clase (herencia simple) | Necesitas envolverlo en un `Thread` o un `Executor` |

**Error clásico:** llamar a `hilo.run()` en vez de `hilo.start()`. Con `run()` **no se crea ningún hilo nuevo**, se ejecuta como un método normal en el hilo actual. Y no puedes hacer `start()` dos veces al mismo hilo.

### 5.4 Métodos importantes de `Thread`

| Método | Qué hace |
| --- | --- |
| `start()` | Arranca el hilo (ejecuta `run()` en un hilo nuevo) |
| `run()` | El código del hilo (lo sobrescribes tú) |
| `Thread.sleep(ms)` | Duerme el hilo actual `ms` milisegundos (lanza `InterruptedException`) |
| `join()` | El hilo actual **espera** a que otro termine |
| `interrupt()` | Solicita que el hilo se detenga (provoca `InterruptedException` si está dormido/esperando) |
| `isAlive()` | ¿Está vivo? |
| `getState()` | Estado actual |
| `getName()` / `setName()` | Nombre del hilo |
| `Thread.currentThread()` | El hilo que está ejecutando este código |
| `getPriority()` / `setPriority()` | Prioridad 1–10 |
| `setDaemon(true)` / `isDaemon()` | Marcar/consultar hilo demonio |
| `yield()` | Cede voluntariamente la CPU a otros hilos de igual prioridad |

### 5.5 `join()`: esperar a otro hilo

```java
public static void main(String[] args) throws InterruptedException {
    Raton r1 = new Raton("Mickey", 3);
    Raton r2 = new Raton("Jerry", 2);
    r1.start();
    r2.start();

    r1.join();     // main espera a que r1 termine
    r2.join();     // y luego a que r2 termine
    System.out.println("Todos han comido");   // se imprime siempre al final
}
```

Sin `join()`, `main` podría imprimir el mensaje final **antes** de que los hilos acaben.

### 5.6 Ejercicio TIC-TAC

```java
public class Tic extends Thread {
    public void run() {
        while (true) {
            System.out.println("TIC");
            try { Thread.sleep(500); } catch (InterruptedException e) { return; }
        }
    }
}

public class Tac extends Thread {
    public void run() {
        while (true) {
            System.out.println("TAC");
            try { Thread.sleep(500); } catch (InterruptedException e) { return; }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        new Tic().start();
        new Tac().start();
    }
}
```

**¿Salen ordenados (TIC TAC TIC TAC)?** No está garantizado. El planificador decide el orden y los dos hilos son independientes. Para forzar el orden hay que **sincronizarlos** (sección 9).

### 5.7 Detener un hilo: `interrupt()`

La forma natural de morir es terminar `run()`. Para pararlo desde fuera se usa `interrupt()`, que provoca `InterruptedException` en el hilo dormido o en espera; ahí decides qué hacer (normalmente `return`).

```java
Thread cuentaAtras = new Thread(() -> {
    try {
        for (int i = 9; i >= 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }
        System.out.println("¡BOOM!");
    } catch (InterruptedException e) {
        System.out.println("Bomba desactivada");
        return;                                  // sale del hilo (NO del programa)
    }
});
cuentaAtras.start();

Thread.sleep(4000);
cuentaAtras.interrupt();                         // lo paramos a los 4 s
```

En el ejemplo de la **bomba** de tus apuntes hay dos hilos (cuenta atrás y artificiero); `main` vigila con `isAlive()` y, cuando uno termina, **interrumpe al otro**.

- `return` en el `catch` sale **del hilo**; `System.exit(0)` sale **del programa entero**.
- `stop()` existe pero está **obsoleto y es peligroso**: no lo uses (solo entiende que lo verás en código antiguo). Igual con `suspend()`/`resume()`.

### 5.8 Hilos demonio, prioridades y planificación

**Hilo demonio (daemon):** presta servicios en segundo plano; **no impide que el programa termine**. Cuando solo quedan demonios, la JVM se cierra. Ejemplo típico: el *garbage collector*. Se marca **antes** de `start()`.

```java
Thread t = new Thread(() -> { while (true) { /* servicio de fondo */ } });
t.setDaemon(true);   // antes de start()
t.start();
```

Los hilos que crea un demonio también son demonios.

**Prioridad:** número de 1 a 10. Constantes: `Thread.MIN_PRIORITY` (1), `Thread.NORM_PRIORITY` (5, la de `main`), `Thread.MAX_PRIORITY` (10). Un hilo hereda la prioridad de su padre. Usa solo las constantes, porque los niveles de Java no coinciden con los del SO. **Es una sugerencia al planificador, no una garantía.**

**Planificación en Java (*fixed priority scheduling*):**

- Se ejecuta el hilo *Ejecutable* de **mayor prioridad**.
- A igual prioridad, turnos **round-robin** (cola FIFO circular).
- Si un hilo de mayor prioridad pasa a Ejecutable, **expulsa** al actual (*planificación apropiativa*).
- Con **time-slicing** (Linux, Windows) cada hilo tiene un *cuanto* de tiempo y luego cede el turno. Sin time-slicing (como Solaris) un hilo puede acaparar la CPU hasta morir: un **hilo egoísta**.
- **`yield()`** cede la CPU a hilos de la misma prioridad; **no está garantizado** que funcione. Por portabilidad, no asumas time-slicing.

**Hilos de usuario vs. de kernel y modelos** (teoría): los de *usuario* los gestiona una librería (el SO ve un solo hilo); los de *kernel* los gestiona el SO. Modelos: **Mx1** (muchos de usuario → un kernel; si uno se bloquea, se bloquea todo), **1x1** (cada uno a uno; más concurrencia pero cuesta crear hilos de kernel) y **MxN** (multiplexa muchos de usuario sobre algunos de kernel; lo mejor de ambos).

---

## 6. `ExecutorService`: la forma moderna de lanzar hilos

Crear hilos "a mano" no es lo recomendado. Java aconseja usar **`ExecutorService`**, que gestiona un **pool de hilos** (un conjunto limitado y reutilizable de hilos).

**Ventajas:**

- **Desacopla** "qué hacer" de "cómo ejecutarlo".
- **Reutiliza hilos**: al acabar una tarea, el hilo vuelve al pool y coge otra (menos coste que crear/destruir hilos).
- **Limita** el número de hilos y evita agotar los recursos si llegan miles de tareas.
- Permite recuperar **resultados** con `Callable` + `Future` (no se ve en el curso).

### 6.1 Tipos de pool (clase `Executors`)

| Método | Qué crea |
| --- | --- |
| `newFixedThreadPool(n)` | Pool con **n hilos fijos**, reutilizados hasta acabar la cola de tareas |
| `newCachedThreadPool()` | Crea hilos según hacen falta y reutiliza los libres; los inactivos mucho tiempo se eliminan |
| `newSingleThreadExecutor()` | **Un solo hilo**: las tareas se ejecutan una a una, en orden |
| `newScheduledThreadPool(n)` | Para tareas **programadas** (una vez tras un retardo o periódicas). *(En tus diapositivas aparece como `newScheduledExecutor`)* |

### 6.2 Métodos clave

| Método | Qué hace |
| --- | --- |
| `execute(Runnable)` | Lanza una tarea; no devuelve resultado |
| `submit(...)` | Lanza una tarea; con `Callable` permite recuperar un resultado (`Future`) |
| `shutdown()` | "Cierra": **no acepta tareas nuevas**, pero deja terminar las que están en marcha o en cola |
| `awaitTermination(t, unidad)` | **Bloquea** hasta que acaben todas (o pase el tiempo). Es como un `join()` masivo |
| `isTerminated()` | `true` si se llamó a `shutdown()` y ya no queda nada ejecutándose |
| `shutdownNow()` | Detiene ya: interrumpe los hilos (`interrupt`) y descarta las tareas pendientes |

### 6.3 Ejemplo completo

```java
import java.util.concurrent.*;

public class EjemploPool {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);   // 3 hilos

        for (int i = 1; i <= 10; i++) {
            final int n = i;
            pool.execute(() -> {                                  // 10 tareas, solo 3 hilos
                System.out.println("Tarea " + n + " en " + Thread.currentThread().getName());
                try { Thread.sleep(500); } catch (InterruptedException e) { }
            });
        }

        pool.shutdown();                                           // no más tareas
        pool.awaitTermination(1, TimeUnit.MINUTES);                // espero a que acaben
        System.out.println("Todas las tareas terminadas");
    }
}
```

Verás que los nombres de hilo se repiten (`pool-1-thread-1`...): **se reutilizan**. Si olvidas `shutdown()`, el programa **no termina** porque los hilos del pool siguen vivos esperando tareas.

---

## 7. El gran problema: recursos compartidos y condiciones de carrera

Los hilos comparten memoria, y eso es potente pero peligroso. Problemas típicos:

| Problema | Qué es |
| --- | --- |
| **Condición de carrera** | El resultado depende del orden concreto en que se intercalan los hilos |
| **Inconsistencia de memoria** | Hilos que ven valores distintos de la misma variable (por cachés de CPU) |
| **Inanición** | Un hilo nunca consigue el recurso porque otros pasan siempre antes |
| **Interbloqueo (deadlock)** | Hilos esperando indefinidamente algo que solo puede dar otro hilo bloqueado |
| **Bloqueo activo (livelock)** | Como el deadlock, pero los hilos cambian de estado constantemente sin avanzar |

Dato importante: estos errores **no siempre aparecen**. Pueden salir en unas ejecuciones y en otras no. Por eso son tan difíciles de depurar.

### 7.1 Ejemplo de condición de carrera: el contador

```java
class Contador {
    private int valor = 100;
    public void incrementa()  { valor++; }      // ¡NO es atómico!
    public void decrementa()  { valor--; }
    public int  getValor()    { return valor; }
}
```

`valor++` parece una sola operación pero son **tres**: leer el valor, sumar 1, escribir el resultado. Si dos hilos se intercalan entre esos pasos, se pierden actualizaciones:

| Paso | Hilo A (suma) | Hilo B (resta) | `valor` |
| --- | --- | --- | --- |
| 1 | lee 100 |  | 100 |
| 2 |  | lee 100 | 100 |
| 3 | calcula 101 y escribe |  | 101 |
| 4 |  | calcula 99 y escribe | **99** |

Si un hilo suma 300 veces y otro resta 300, debería quedar en 100; pero **puede quedar en cualquier valor**. Hay que pasar el **mismo objeto** `Contador` a los dos hilos (si cada uno tiene el suyo, no compartirían nada).

### 7.2 Acciones atómicas y `volatile`

Una acción **atómica** ocurre entera, sin interrupciones. En Java son atómicas leer/escribir variables primitivas (salvo `long` y `double`) y cualquier variable `volatile`.

`volatile` obliga a que todos los hilos vean siempre el valor real (no una copia en caché). **No arregla el `valor++`** con varios hilos escribiendo; sirve cuando **un hilo escribe** y los demás **solo leen**:

```java
private volatile boolean parar = false;   // un hilo lo pone a true, otros lo leen
```

### 7.3 Sección crítica: qué debe cumplir una solución

1. **Exclusión mutua**: solo un hilo dentro a la vez.
2. **Progreso**: si nadie está dentro, entre uno de los que esperan sin retrasos indebidos.
3. **Espera limitada**: nadie espera indefinidamente.

(Además: no se hacen suposiciones sobre la velocidad relativa de los hilos y un hilo permanece dentro un tiempo finito.)

---

## 8. `synchronized`: exclusión mutua con monitores

En Java, cada objeto tiene un **monitor** (cerrojo). `synchronized` hace que **solo un hilo a la vez** pueda ejecutar ese código usando ese monitor. Los demás esperan en la cola del objeto (el orden de elección es indeterminista).

#### a) Método sincronizado

```java
class Contador {
    private int valor = 100;
    public synchronized void incrementa() { valor++; }
    public synchronized void decrementa() { valor--; }
    public synchronized int  getValor()   { return valor; }
}
```

#### b) Bloque sincronizado (más preciso)

```java
public void incrementa() {
    // código que no necesita protección...
    synchronized (this) {        // solo esto está en exclusión mutua
        valor++;
    }
}
```

Un método `synchronized` es equivalente a un bloque `synchronized (this)` que envuelve todo el cuerpo.

**Reglas clave:**

- La sincronización es **por objeto (monitor)**: los hilos deben usar **la misma instancia** para excluirse. Dos objetos distintos = dos monitores distintos = sin exclusión entre ellos. (En el ejemplo de los **relevos**, hay un objeto por equipo: cada equipo se sincroniza por separado.)
- Sincroniza **lo mínimo**: demasiado código sincronizado convierte tu programa en secuencial y pierdes las ventajas de los hilos.
- Al **entrar** en un bloque sincronizado el hilo ve los valores actualizados de memoria; al **salir** publica sus cambios.
- Meter los `System.out.println` dentro de los bloques sincronizados evita salidas desordenadas que parecen errores.

### 8.1 Ejercicio resuelto: 5 hilos × 5000 incrementos

```java
public class ContadorCompartido {
    static int contador = 0;
    static final Object cerrojo = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread[] hilos = new Thread[5];
        for (int i = 0; i < 5; i++) {
            hilos[i] = new Thread(() -> {
                for (int j = 0; j < 5000; j++) {
                    synchronized (cerrojo) {     // QUITA esto para ver el error
                        contador++;
                    }
                }
            });
            hilos[i].start();
        }
        for (Thread h : hilos) h.join();         // esperamos a los cinco
        System.out.println("Resultado: " + contador + " (esperado 25000)");
    }
}
```

Sin `synchronized` suele salir **menos de 25000**; con él, siempre **25000**.

---

## 9. Coordinación: `wait()`, `notify()` y `notifyAll()`

`synchronized` evita que se pisen, pero no resuelve **"espera a que ocurra X"** (sincronización condicional). Para eso están estos tres métodos, de la clase `Object` (todas las clases los tienen):

| Método | Qué hace |
| --- | --- |
| `wait()` | El hilo se **suspende** y **libera el cerrojo** hasta que otro llame a `notify()`/`notifyAll()` sobre el mismo objeto |
| `wait(ms)` | Igual, pero también despierta si pasa el tiempo |
| `notify()` | Despierta **a uno** de los hilos que esperan (elección arbitraria) |
| `notifyAll()` | Despierta **a todos** los que esperan |

**Reglas imprescindibles:**

- Solo se pueden llamar **dentro de un bloque/método `synchronized`** sobre ese mismo objeto (si no, `IllegalMonitorStateException`).
- `wait()` obliga a capturar `InterruptedException`.
- Es **espera no activa**: el hilo dormido no consume CPU.

### 9.1 Siempre `while`, nunca `if`

```java
synchronized (this) {
    while (!condicion) {     // while, NO if
        wait();
    }
    // ya puedo continuar: la condición es cierta
}
```

Por qué `while`: (1) al despertar, otro hilo pudo cambiar la condición antes de que tú ejecutes; (2) `notifyAll()` despierta a varios y solo uno puede seguir; (3) Java permite **despertares espurios** (un hilo sale del `wait` sin `notify`). Con `while` se vuelve a comprobar y, si no se cumple, se vuelve a dormir.

### 9.2 Ejemplo: el libro (lector y escritor)

```java
class Libro {
    private boolean terminado = false;

    public synchronized void leer(String lector) throws InterruptedException {
        while (!terminado) {                         // si no está acabado, espero
            System.out.println(lector + " espera...");
            wait();
        }
        System.out.println(lector + " lee el libro");
    }

    public synchronized void terminar() {
        terminado = true;
        notifyAll();                                 // aviso a todos los lectores
    }
}
```

Los lectores llaman a `leer()`, se quedan en `wait()`; el escritor tarda 5 s, llama a `terminar()` y los despierta. Si usas `notify()` solo despierta a **uno** de los lectores; con `notifyAll()` leen todos.

### 9.3 Productor–consumidor

Un hilo **produce** datos y otro los **consume**, usando un espacio intermedio. Sin control aparecen errores: el consumidor lee datos repetidos o se salta alguno, o el productor sobrescribe datos no consumidos.

Solución: la cola compartida con métodos `synchronized` + `wait/notify`:

```java
class Cola {
    private int numero;
    private boolean disponible = false;      // true = hay un dato listo para consumir

    public synchronized int get() throws InterruptedException {
        while (!disponible) {                // no hay dato: espero
            wait();
        }
        disponible = false;                  // lo consumo, la cola queda vacía
        notifyAll();                         // aviso al productor
        return numero;
    }

    public synchronized void put(int valor) throws InterruptedException {
        while (disponible) {                 // el dato anterior no se ha consumido: espero
            wait();
        }
        numero = valor;
        disponible = true;                   // hay dato nuevo
        notifyAll();                         // aviso al consumidor
    }
}

class Productor extends Thread {
    private Cola cola;
    Productor(Cola c) { cola = c; }
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                cola.put(i);
                System.out.println("Produce " + i);
            }
        } catch (InterruptedException e) { }
    }
}

class Consumidor extends Thread {
    private Cola cola;
    Consumidor(Cola c) { cola = c; }
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("Consume " + cola.get());
            }
        } catch (InterruptedException e) { }
    }
}

public class Principal {
    public static void main(String[] args) {
        Cola c = new Cola();                 // UNA sola cola compartida
        new Productor(c).start();
        new Consumidor(c).start();
    }
}
```

La variable `disponible` es la **condición**: el productor espera si hay dato sin consumir; el consumidor espera si no hay dato. Cada uno **avisa** al otro cuando cambia el estado.

**Diferencia con exclusión mutua:** en exclusión mutua simple solo un hilo accede a la vez; en productor–consumidor **los dos modifican** el dato compartido y además deben **turnarse** según el estado.

---

## 10. Semáforos en Java: `Semaphore`

`synchronized` permite **un solo** hilo en la sección crítica. Un **`Semaphore`** (paquete `java.util.concurrent`) permite hasta **N** hilos a la vez: ideal para recursos limitados (3 impresoras, 5 conexiones de base de datos...).

| Elemento | Qué hace |
| --- | --- |
| `new Semaphore(n)` | Crea un semáforo con **n permisos** (n hilos simultáneos como máximo) |
| `new Semaphore(n, true)` | Modo **justo**: los permisos se dan por orden de petición (evita inanición) |
| `acquire()` | Pide un permiso; si no hay, **espera** (lanza `InterruptedException`) |
| `acquire(k)` | Pide `k` permisos a la vez |
| `release()` | Devuelve un permiso |
| `release(k)` | Devuelve `k` permisos |

```java
import java.util.concurrent.*;

class Cola {
    private Semaphore semaforo;
    Cola(Semaphore s) { this.semaforo = s; }

    public void pasar(String nombre) {
        try {
            System.out.println("ESPERANDO semaforo: " + nombre);
            semaforo.acquire();                        // pido permiso
            try {
                System.out.println("PASANDO semaforo: " + nombre);
                Thread.sleep(3000);                    // uso el recurso
            } finally {
                System.out.println("SOLTANDO semaforo: " + nombre);
                semaforo.release();                    // SIEMPRE devuelvo el permiso
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Hilo extends Thread {
    private Cola cola;
    Hilo(Cola c) { cola = c; }
    public void run() { cola.pasar(getName()); }
}

public class Principal {
    public static void main(String[] args) {
        Semaphore semaforo = new Semaphore(2);               // máximo 2 a la vez
        Cola cola = new Cola(semaforo);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            pool.submit(new Hilo(cola));                     // 10 tareas
        }
        pool.shutdown();
    }
}
```

Importante: **todos los hilos deben compartir el mismo objeto `Semaphore`**. Pon `release()` en un `finally` para no perder permisos si hay un error. Un semáforo con **1 permiso** se comporta como un cerrojo (semáforo binario). Si todos los permisos están concedidos, los demás hilos esperan.

**Cuándo usar cada uno:** `synchronized` para exclusión mutua simple (1 hilo); `Semaphore` cuando N hilos pueden entrar a la vez; a veces se combinan.

---

## 11. Monitores, paso de mensajes y problemas clásicos

### 11.1 Monitores

Un **monitor** es un componente de **alto nivel** que encierra los recursos compartidos como **privados** y solo permite accederlos mediante métodos públicos que se ejecutan **en exclusión mutua**. Tiene una cola de procesos suspendidos. En Java, **cada objeto es un monitor**, y `synchronized` + `wait/notify` lo implementan (tu clase `Contador` o `Cola` *es* un monitor).

| Ventajas | Desventaja |
| --- | --- |
| Uniformidad (una sola capacidad: exclusión mutua) | Con muchas condiciones de sincronización el código se complica mucho |
| Modularidad (el código crítico está separado) |  |
| Simplicidad (no te ocupas de los detalles) |  |

**Semáforo vs monitor:** el semáforo es de bajo nivel (flexible pero propenso a errores y dispersa su gestión por el código); el monitor es de alto nivel (más seguro y ordenado).

### 11.2 Paso de mensajes

Técnica para sincronizar y comunicar procesos **sin memoria compartida**: se envía y se recibe un mensaje.

- **Asíncrono**: el emisor **no espera** a que lo reciban y sigue. Se usan **buzones/colas** donde se guardan los mensajes. El emisor solo se bloquea si el buzón está lleno.
- **Síncrono** (*rendezvous*, "encuentro"): el emisor **espera** a que alguien reciba el mensaje. Incluye la llamada a procedimiento remoto (RPC) de cliente/servidor.

Los mensajes pueden ir por un **buffer en memoria** (mismo equipo) o por un **socket** (entre máquinas por red; hoy es lo más habitual). En Java se usan como cualquier stream: `read/write` en vez de `send/receive`. Recuerda que el padre y el hijo de la sección 2.8 ya se estaban pasando mensajes por *pipes*.

### 11.3 Inanición e interbloqueo

- **Inanición:** un hilo espera indefinidamente, p. ej. porque la primitiva usa prioridades y otros siempre pasan antes. Mitigación: semáforos **justos** (`new Semaphore(n, true)`).
- **Interbloqueo:** cada hilo espera un recurso que tiene otro del grupo. Ocurre **aunque** la exclusión mutua se cumpla perfectamente.

```java
// DEADLOCK clásico: orden de bloqueo cruzado
Object A = new Object(), B = new Object();

Thread t1 = new Thread(() -> {
    synchronized (A) { pausa(); synchronized (B) { System.out.println("t1"); } }
});
Thread t2 = new Thread(() -> {
    synchronized (B) { pausa(); synchronized (A) { System.out.println("t2"); } }
});
t1.start(); t2.start();   // t1 tiene A y quiere B; t2 tiene B y quiere A: se bloquean para siempre
```

**Cómo evitarlo:** que **todos los hilos adquieran los recursos en el mismo orden** (siempre A y luego B), mantener los bloqueos el menor tiempo posible y no bloquear dentro de bloqueos si no hace falta.

---

## 12. Chuleta final

### Procesos

```java
ProcessBuilder pb = new ProcessBuilder("cmd", "arg");
pb.directory(new File("ruta"));
pb.redirectOutput(new File("salida.txt"));      // opcional, ANTES de start
Process p = pb.start();
p.getOutputStream();   // padre escribe -> hijo lee (stdin del hijo)
p.getInputStream();    // padre lee   <- hijo escribe (stdout)
p.getErrorStream();    // padre lee   <- errores del hijo
int cod = p.waitFor(); // 0 = OK
p.destroy();           // matar
```

### Hilos

```java
// Crear
new Thread(() -> { ... }).start();        // o extends Thread / implements Runnable
t.join();  Thread.sleep(ms);  t.interrupt();  t.isAlive();

// Sincronizar
synchronized (obj) { ... }                // o método synchronized
while (!cond) { wait(); }  notifyAll();   // dentro de synchronized

// Pool
ExecutorService p = Executors.newFixedThreadPool(n);
p.execute(tarea);  p.shutdown();  p.awaitTermination(1, TimeUnit.MINUTES);

// Semáforo
Semaphore s = new Semaphore(n);  s.acquire();  try { ... } finally { s.release(); }
```

### Qué herramienta uso para qué

| Necesito... | Uso |
| --- | --- |
| Lanzar otro programa | `ProcessBuilder` + `Process` |
| Hablar con ese programa | `getOutputStream` / `getInputStream` |
| Guardar su salida en un fichero | `redirectOutput(File)` |
| Que dos procesos no pisen un fichero | `FileChannel.lock()` (mejor) o `File.createNewFile()` |
| Hacer varias cosas a la vez | `Thread` / `Runnable` / `ExecutorService` |
| Esperar a que acabe un hilo | `join()` |
| Esperar a que acaben muchas tareas del pool | `shutdown()` + `awaitTermination()` |
| Un solo hilo en una zona de código | `synchronized` |
| N hilos como máximo en una zona | `Semaphore(n)` |
| "Espera hasta que se cumpla X" | `wait()` / `notifyAll()` dentro de `while` |
| Un hilo produce, otro consume | Cola con `synchronized` + `wait/notify` |
| Parar un hilo | `interrupt()` y salir en el `catch` |

### Errores típicos (los que más se pierden puntos)

1. Llamar a `run()` en lugar de `start()`.
2. Hacer `start()` dos veces sobre el mismo hilo.
3. Olvidar el **`\n`** y el **`flush()`** al escribir al proceso hijo.
4. Confundir `getInputStream` (lo que el padre **lee**) con `getOutputStream` (lo que el padre **escribe**).
5. Lanzar un proceso hijo **sin compilarlo** o con la ruta mal.
6. Rediriger con `redirectOutput` y luego intentar leer de `getInputStream()`.
7. `wait()`/`notify()` **fuera** de un bloque `synchronized` → `IllegalMonitorStateException`.
8. Usar `if` en vez de `while` alrededor de `wait()`.
9. Pasar **objetos distintos** a los hilos y esperar que se sincronicen (el monitor/semáforo debe ser **el mismo objeto compartido**).
10. Olvidar `shutdown()` del pool (el programa no termina) o `release()` del semáforo (los hilos se quedan esperando).
11. Sincronizar demasiado código y perder la concurrencia.
12. Interbloqueo por adquirir recursos en distinto orden.
13. Confiar en el orden de ejecución de los hilos o en las prioridades: **no está garantizado**.
14. Usar `System.exit(0)` en un hilo pensando que solo sale del hilo: mata **todo el programa** (para salir del hilo usa `return`).

### Qué repasar con tus códigos de ejemplo

Cuando te estudies los ejercicios del repositorio de clase, busca esto en cada uno: **(1)** qué objeto es el compartido, **(2)** cuál es la sección crítica, **(3)** qué mecanismo la protege y **(4)** qué pasaría si lo quitaras. Con esas cuatro preguntas puedes explicar cualquier ejemplo.
