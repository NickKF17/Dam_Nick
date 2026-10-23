# Práctica MultiHilo I - Documentación de las pruebas

Todos los programas se han compilado con `javac` y ejecutado con `java`.
(En la consola donde los probé las tildes y la ñ salían como `?`, aquí las he escrito bien.
Es solo un tema de codificación de la consola, el código está bien.)

## Cómo se ha hecho cada ejercicio

| Ejercicio | Clase | Cómo se crea el hilo |
|-----------|-------|----------------------|
| 1 - Par / Impar | `ParImpar` | implementa `Runnable` |
| 2 - Iron Man | `IronMan` | hereda de `Thread` |
| 3 - Relevos | `Correr` | hereda de `Thread` |
| 4 - Carrera de relevos | `Equipo` (y `Correr`) | `Equipo` implementa `Runnable` |
| 5 - Artificiero y bomba | `Bomba` | implementa `Runnable` |

Así cumplo lo de usar `Thread` en al menos 2 ejercicios (2 y 3) y `Runnable` en al menos otros 2 (1, 4 y 5).

---

## Ejercicio 1 - PAR / IMPAR

Un solo método (`mostrarNumeros`) saca los pares o los impares según un dato (`pares`: true/false).
Se lanzan 2 hilos con la misma clase, uno con `true` y otro con `false`.
Cada número espera un rato aleatorio (50-150 ms) para que se vea cómo se mezclan los hilos.

**Prueba 1**
```
2 1 3 4 5 6 7 8 9 10
```
**Prueba 2**
```
2 1 4 6 3 5 8 7 10 9
```
**Prueba 3**
```
2 1 4 3 6 5 7 8 9 10
```
(En la consola salen uno por línea, aquí los pongo juntos para ahorrar espacio.)

**Conclusión:** el orden cambia en cada ejecución. Los dos hilos van "a la vez" y no se sabe cuál escribe antes.
Lo que sí se mantiene es que los pares salen siempre ordenados entre ellos (2, 4, 6, 8, 10) y los impares también (1, 3, 5, 7, 9).

---

## Ejercicio 2 - IRON MAN

Cada participante es un hilo (`extends Thread`) con su nombre y sus 3 velocidades.
Las pruebas se enlazan en `run()`: bici (180 km), marathon (42 km) y natación (4000 m).
Como 1 hora = 1000 ms, el tiempo de cada prueba es `distancia / velocidad * 1000`.

Cálculo a mano de lo que debería tardar cada uno:

| Participante | Bici | Marathon | Natación | Total |
|--------------|------|----------|----------|-------|
| Pedro | 180/20 = 9000 ms | 42/6 = 7000 ms | 4000/1000 = 4000 ms | **20000 ms** |
| Juan | 180/18 = 10000 ms | 42/7 = 6000 ms | 4000/2000 = 2000 ms | **18000 ms** |
| Vicente | 180/30 = 6000 ms | 42/6 = 7000 ms | 4000/1000 = 4000 ms | **17000 ms** |

**Salida real:**
```
Vicente comienza la prueba en bici
Juan comienza la prueba en bici
Pedro comienza la prueba en bici
Vicente ha acabado la prueba en bici
Vicente comienza la marathon
Pedro ha acabado la prueba en bici
Pedro comienza la marathon
Juan ha acabado la prueba en bici
Juan comienza la marathon
Vicente ha acabado la marathon
Vicente comienza la prueba de natación
Juan ha acabado la marathon
Juan comienza la prueba de natación
Pedro ha acabado la marathon
Pedro comienza la prueba de natación
Vicente ha acabado la prueba de natación
>>> Vicente llega el primero a meta: ¡GANADOR!
Juan ha acabado la prueba de natación
Juan llega a meta
Pedro ha acabado la prueba de natación
Pedro llega a meta
```

**¿Quién gana?** **Vicente** (17 "horas"), después Juan (18) y por último Pedro (20).
Coincide con el cálculo a mano. Vicente gana aunque nade lento y corra igual que Pedro, porque en bici es mucho más rápido (30 km/h).

---

## Ejercicio 3 - RELEVOS

La clase `Correr` hereda de `Thread`. Cada corredor corre 100 m en 10 tramos de 10 m,
y cada tramo tarda un número aleatorio entre 950 y 1050 ms.
Para que un corredor no empiece hasta que acabe el anterior, el `main` hace `start()` y luego **`join()`** del corredor
antes de crear el siguiente.

**Salida real:**
```
España:Pepe comienza su relevo
España:Pepe ha acabado su relevo - Ha tardado: 9.99 segundos
España:Maria comienza su relevo
España:Maria ha acabado su relevo - Ha tardado: 9.89 segundos
España:Juan comienza su relevo
España:Juan ha acabado su relevo - Ha tardado: 9.95 segundos
España:Marta comienza su relevo
España:Marta ha acabado su relevo - Ha tardado: 9.94 segundos
FINAL DE PROGRAMA
```
Cada corredor tarda unos 10 segundos (10 tramos de unos 1000 ms) y nunca empieza uno antes de que acabe el anterior.

---

## Ejercicio 4 - CARRERA DE RELEVOS

Uso una copia de `Correr` (sin el `main`) y una clase nueva `Equipo` que implementa `Runnable`.
Cada equipo es un hilo que va lanzando a sus corredores con `start()` + `join()` igual que en el ejercicio 3.
Los equipos de España y EEUU se lanzan a la vez.

**Salida real:**
```
EEUU:John comienza su relevo
España:Pepe comienza su relevo
España:Pepe ha acabado su relevo - Ha tardado: 9.85 segundos
España:Maria comienza su relevo
EEUU:John ha acabado su relevo - Ha tardado: 10.02 segundos
EEUU:Kim comienza su relevo
España:Maria ha acabado su relevo - Ha tardado: 9.95 segundos
España:Juan comienza su relevo
EEUU:Kim ha acabado su relevo - Ha tardado: 10.04 segundos
EEUU:Mike comienza su relevo
España:Juan ha acabado su relevo - Ha tardado: 10.05 segundos
España:Marta comienza su relevo
EEUU:Mike ha acabado su relevo - Ha tardado: 10.05 segundos
EEUU:Sarah comienza su relevo
España:Marta ha acabado su relevo - Ha tardado: 9.95 segundos
FINAL DE PROGRAMA - España
EEUU:Sarah ha acabado su relevo - Ha tardado: 10.12 segundos
FINAL DE PROGRAMA - EEUU
```
**Ganador en esta ejecución:** España (terminó antes que EEUU). Como los tiempos son aleatorios y están muy parecidos (unos 40 s cada equipo), puede ganar cualquiera en otra ejecución.

---

## Ejercicio 5 - EL ARTIFICIERO Y LA BOMBA

La clase `Bomba` implementa `Runnable` y tiene 2 métodos: `cuentaAtras()` y `artificiero()`.
Según un parámetro (`modo` 1 o 2) el `run()` arranca uno u otro. Se lanzan 2 hilos.

- La cuenta atrás va de 9 a 0 esperando 1000 ms después de cada número (así la bomba explota a los 10 s).
- El artificiero tarda entre 9500 y 10500 ms.
- Si la bomba termina primero, el `main` (que espera con `join()`) **interrumpe al artificiero**, salta su `InterruptedException` y sale "El artificiero ha muerto".
- Si el artificiero termina primero, interrumpe a la bomba y salta su `InterruptedException`: "La bomba ha sido desactivada".

He ejecutado el programa 6 veces. Resultados:

| Prueba | Resultado |
|--------|-----------|
| 1 | El artificiero desactiva la bomba |
| 2 | Explota la bomba, el artificiero muere |
| 3 | El artificiero desactiva la bomba |
| 4 | Explota la bomba, el artificiero muere |
| 5 | Explota la bomba, el artificiero muere |
| 6 | El artificiero desactiva la bomba |

**Salida cuando explota (prueba 2):**
```
9
El artificiero comienza a desactivar la bomba
8
7
6
5
4
3
2
1
0
PUUUUUUMMMMMMMMMM
El artificiero ha muerto
```

**Salida cuando se desactiva (prueba 1):**
```
9
El artificiero comienza a desactivar la bomba
8
7
6
5
4
3
2
1
0
El artificiero ha desactivado la bomba
La bomba ha sido desactivada
```

**Conclusión:** salió 3 veces cada resultado. Tiene sentido porque el artificiero tarda entre 9,5 y 10,5 s y la bomba explota a los 10 s: más o menos la mitad de las veces llega antes y la mitad después.
