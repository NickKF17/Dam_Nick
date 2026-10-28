package tercerTrimestre;

/*
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
*/
// El asterisco importa TODAS las clases del paquete java.io (File, FileReader, BufferedReader, IOException...). Cómodo, aunque importar una a una deja más claro qué se usa.
import java.io.*;
// Files y Path (paquete NIO, más moderno) ofrecen métodos para leer un fichero entero de golpe.
import java.nio.file.Files;
import java.nio.file.Path;
// ArrayList para guardar las líneas leídas.
import java.util.ArrayList;
// Scanner: el mismo que usamos con el teclado, pero apuntando a un fichero.
import java.util.Scanner;

// Seis formas distintas de LEER un fichero de texto. Todas hacen lo mismo: mostrar el Quijote por consola.
public class Ficheros1 {
	// El main llama a cada variante; compáralas entre sí.
	public static void main(String[] args) {
		// el método 1 y 2 son similares, solo cambian algunas líneas para hacer la función mas compacta
		// En ambos y en el método 3 leemos el fichero linea a línea
		// Variante clásica con FileReader + BufferedReader y do-while.
		lectura1();
		lectura2();
		// de esta forma el fichero se cierra al terminar su uso y evitamos tener que hacerlo nosotros
		// Variante recomendada: try-with-resources.
		lectura2bis();
		// usamos la clase Scanner que ya conocemos para leer del fichero
		lectura3();
		// Leemos el fichero de una vez y lo guardamos, línea a línea, en una lista
		// Lectura de golpe a una lista.
		lectura4();
		// Leemos el fichero de una vez y lo guardamos en un String.
		// Lectura de golpe a un String.
		lectura5();
	}

	// Lectura 1: la forma 'de manual', con todos los pasos explícitos.
	public static void lectura1() {
		System.out.println("lectura1");
		// Tratar las excepciones es obligatorio cuando se trabaja con ficheros
		// Con ficheros SIEMPRE hay que tratar excepciones: el fichero puede no existir, no tener permisos, etc.
		try {
			// necesitamos dos objetos. FileReader simboliza al ficharo. Recibe como argumento el nombre 
			// o la ruta al mismo
			// FileReader abre el fichero de texto para leer caracteres. Lanza FileNotFoundException si no existe.
			FileReader fichero = new FileReader("/mnt/temp/quijote.txt");
			// BufferedReader nos lo podemos imaginar como un cursor que apunta al lugar donde vamos leyendo
			// Como si señaláramos en un libro con el dedo la siguiente línea que nos toca leer
			// inicialmente apunta al principio del fichero
			// BufferedReader añade un buffer y el método readLine() para leer líneas enteras.
			BufferedReader lector = new BufferedReader(fichero);
			// Variable donde guardaremos cada línea leída.
			String linea;
			// Usamos un bucle para leer del fichero. Como al menos tendremos que leer una vez
			// la estructura do-while parece la mas correcta. Luego veremos una alternativa
			// do-while: se ejecuta al menos una vez, y se repite mientras no hayamos llegado al final.
			do{
				// readline lee una linea completa del fichero o null cuando hayamos llegado al final
				// el último caracter, el salto de línea si existe, que simbolizamos con \n se suprime cuando se lee
				// readLine() devuelve la siguiente línea SIN el salto de línea, o null si ya no hay más.
				linea = lector.readLine();
				// Hay que comprobar el null antes de imprimir; si no, en la última vuelta imprimiríamos 'null'.
				if(linea!=null)
					System.out.println(linea);
			// cuando leamos null es nuestra condición de salida: hemos leído el fichero completo
			// Condición de salida: null = fin de fichero.
			}while(linea!=null);
			// No olvidemos cerrar al final para no consumir recursos innecesariamente
			// Cerramos para liberar el fichero. Si salta una excepción antes, esta línea NO se ejecuta (por eso existe try-with-resources).
			lector.close();
		// Existen excepciones concretas (FileNotFound, IOException) para la mayoría de problemas que 
		// puede haber con un fichero. Si usamos una excepción genérica, podemos ver que ocurre
		// exactamente con el método getMessage
		// Exception es genérica y atrapa todo; getMessage() nos cuenta qué pasó.
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}
	
	// Lectura 2: igual, pero más compacta.
	public static void lectura2() {
		System.out.println("lectura2");
		// Este método es similar al anterior, pero mas compacto
		try {
			// Creamos en la misma sentencia los dos objetos que necesitamos
			// Se crean los dos objetos anidados en una sola sentencia.
			BufferedReader lector = new BufferedReader(new FileReader("/mnt/temp/quijote.txt"));
			String linea;
			// Leemos las líneas del fichero en la condición el while y salimos si lo que leemos es null
			// Truco muy habitual: asigna y comprueba en la misma condición. Primero se ejecuta el paréntesis interior (leer y guardar), luego se compara con null.
			while ((linea = lector.readLine()) != null) {
				System.out.println(linea);
			}
			// Seguimos cerrando a mano.
			lector.close();
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}

	// Lectura 2bis: con try-with-resources.
	public static void lectura2bis() {
		System.out.println("lectura2bis");
		// Aún mas compacto y nos ahorramos cerrar el fichero: se cierra solo
		// El recurso se declara entre paréntesis del try y Java llama a close() automáticamente al terminar (con o sin error). Ya no hace falta close().
		try(BufferedReader lector = new BufferedReader(new FileReader("/mnt/temp/quijote.txt"))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				System.out.println(linea);
			}
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}

	// Lectura 3: con Scanner.
	public static void lectura3() {
		System.out.println("lectura3");
		try {
			// En este método vamos a usar la clase Scanner que ya conocemos de cuando leíamos del teclado
			// La clase File simboliza al fichero y scanner será nuestro cursor
			// File representa la ruta del fichero (no lo abre todavía).
			File fichero = new File("/mnt/temp/quijote.txt");
			// Scanner sobre un File: lanza FileNotFoundException si no existe.
			Scanner scanner = new Scanner(fichero);
			// como condición de salida usamos hasNextLine que nos devuelve false cuando llegamos al final del fichero
			// y ya no hay mas líneas para leer y true mientras que esto no ocurra
			// hasNextLine() comprueba SI hay línea disponible antes de leer (no hace falta comprobar null).
			while (scanner.hasNextLine()) {
				// nextLine lee del fichero una línea completa, hasta que se encuentra un salto de línea
				// Al igual que en el caso anterior, también suprime los caracteres \n
				// nextLine() lee una línea completa.
				String linea = scanner.nextLine();
				System.out.println(linea);
			}
			// Cerrar el Scanner.
			scanner.close();
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}

	// Lectura 4: todo el fichero de una vez a una lista (cada elemento = una línea).
	public static void lectura4() {
		System.out.println("lectura4");
		// En este método leeremos el fichero de una vez y lo guardaremos en un ArrayList donde cada elemento
		// es una línea del fichero. Necesitamos el objeto Path que simboliza la ruta al fichero
		// Path no necesitar ir dentro de la excepción
		// Path representa una ruta. Path.of(...) existe desde Java 11.
		Path ruta = Path.of("/mnt/temp/quijote.txt");
		// Se inicializa a null para poder usarla después del try; si falla la lectura seguirá siendo null.
		ArrayList<String> lineas = null;
		try {
			// El método readAllLines lee el fichero completamente y luego lo cierra y almacena el contenido en un List
			// Como nosotros no hemos usado el objeto List (y el ArrayList que si conocemos es compatible y mas potente)
			// Usamos un cast
			// readAllLines lee todo y cierra el fichero. Devuelve List<String>; el cast a ArrayList funciona en la práctica.
			// NOTA del mentor: la documentación no garantiza que sea un ArrayList; lo más seguro es declarar List<String> y no hacer cast. Además readAllLines carga TODO en memoria: no es adecuado para ficheros enormes.
			lineas = (ArrayList<String>)Files.readAllLines(ruta);
			// No obstante, si quieres usar List sería así:
			// List<String> lineas = Files.readAllLines(ruta);
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
		// Para procesar el contenido, trabajamos con el ArrayList
		// También ha suprimido por nosotros los saltos de línea finales de cada línea
		// Comprobamos que la lectura salió bien antes de recorrer.
		if(lineas!=null)
			// for-each: recorre la lista sin índices.
			for (String linea : lineas)
				System.out.println(linea);
	}

	// Lectura 5: todo el fichero en un único String.
	public static void lectura5() {
		System.out.println("lectura5");
		// El último método lee todo el contenido del fichero como un único String
		Path ruta = Path.of("/mnt/temp/quijote.txt");
		// Inicializado a null por si falla.
		String contenido = null;
		try {
			// leemos todo el contenido de una vez en un String
			// cuidado que aquí los saltos de línea siguen existiendo. Si nuestro fichero tuviera dos lineas así
			// uno
			// dos
			// contenido sería así: "uno\ndos\n"
			// El \n simboliza el salto de línea y ocupa un solo caracter y no dos
			// readString (Java 11+) devuelve el contenido completo, conservando los saltos de línea \n.
			contenido = Files.readString(ruta);
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
		// print (sin ln) porque el contenido ya trae sus propios saltos de línea.
		// NOTA del mentor: si falló la lectura, contenido es null y se imprimirá 'null'.
		System.out.print(contenido);
	}
}
