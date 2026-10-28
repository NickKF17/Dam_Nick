import java.io.BufferedReader;   // Lector con buffer: permite leer línea a línea
import java.io.File;             // Representa un fichero (para comprobar si existe)
import java.io.FileReader;       // Abre un fichero de texto para lectura
import java.util.Scanner;        // Para leer del teclado

public class Ejercicio01 {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in); // Objeto para leer del teclado
		String nombreFichero;                     // Aquí guardaremos el nombre que escriba el usuario
		File fichero;                             // Objeto File para preguntar si existe
		// Repetimos la petición hasta que el fichero exista (siempre se pregunta al menos una vez: do-while)
		do {
			System.out.print("Introduce el nombre del fichero: ");
			nombreFichero = teclado.nextLine();      // Leemos la línea completa (puede tener espacios)
			fichero = new File(nombreFichero);       // Creamos el File con esa ruta
			if (!fichero.exists())                   // exists() devuelve false si no hay tal fichero
				System.out.println("El fichero " + nombreFichero + " no existe");
		} while (!fichero.exists());                 // Mientras no exista, volvemos a pedirlo
		System.out.print("Introduce la palabra a buscar: ");
		String palabra = teclado.nextLine();         // Palabra que queremos contar
		contar(nombreFichero, palabra);              // Delegamos el trabajo en una función
	}

	// Cuenta las líneas del fichero y las veces que aparece la palabra
	public static void contar(String nombreFichero, String palabra) {
		int lineas = 0;      // Contador de líneas
		int apariciones = 0; // Contador de apariciones de la palabra
		// try-with-resources: el lector se cierra solo al terminar el bloque
		try (BufferedReader lector = new BufferedReader(new FileReader(nombreFichero))) {
			String linea;
			// readLine devuelve null al llegar al final del fichero
			while ((linea = lector.readLine()) != null) {
				lineas++; // Una línea más
				// Troceamos la línea por todo lo que NO sea letra o número (espacios, comas, puntos...)
				// así "pera," o "pera." también se reconocen como la palabra pera
				String[] palabras = linea.split("[^\\p{L}\\p{N}]+");
				for (int i = 0; i < palabras.length; i++) {
					// equalsIgnoreCase compara sin distinguir mayúsculas: "Pera" cuenta como "pera"
					// Y comparamos la palabra COMPLETA: "peras" no cuenta
					if (palabras[i].equalsIgnoreCase(palabra))
						apariciones++;
				}
			}
			System.out.println("El fichero tiene " + lineas + " líneas");
			System.out.println("La palabra " + palabra + " aparece " + apariciones + " veces");
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage()); // Cualquier problema de E/S
		}
	}
}
