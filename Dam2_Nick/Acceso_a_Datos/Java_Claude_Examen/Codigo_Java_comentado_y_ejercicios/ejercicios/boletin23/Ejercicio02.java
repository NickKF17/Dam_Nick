import java.io.BufferedReader;
import java.io.FileReader;

public class Ejercicio02 {

	public static void main(String[] args) {
		estadisticas("datos/fichero1.txt"); // Llamada tal y como pide el enunciado
	}

	// Muestra líneas, líneas en blanco, caracteres (sin espacios) y espacios
	public static void estadisticas(String nombreFichero) {
		int lineas = 0;       // Total de líneas
		int enBlanco = 0;     // Líneas vacías
		int caracteres = 0;   // Caracteres que no son espacio
		int espacios = 0;     // Espacios
		try (BufferedReader lector = new BufferedReader(new FileReader(nombreFichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				lineas++;
				if (linea.trim().isEmpty()) // trim quita espacios de los extremos; si queda vacía, es línea en blanco
					enBlanco++;
				// Recorremos la línea carácter a carácter
				for (int i = 0; i < linea.length(); i++) {
					if (linea.charAt(i) == ' ') // charAt(i) devuelve el carácter en la posición i
						espacios++;
					else
						caracteres++;
				}
			}
			System.out.println("Número de líneas: " + lineas);
			System.out.println("Líneas en blanco: " + enBlanco);
			System.out.println("Cantidad de caracteres sin contar los espacios: " + caracteres);
			System.out.println("Cantidad de espacios: " + espacios);
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
	}
}
