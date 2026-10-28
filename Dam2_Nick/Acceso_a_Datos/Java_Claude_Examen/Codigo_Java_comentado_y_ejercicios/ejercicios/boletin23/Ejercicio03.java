import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Ejercicio03 {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		System.out.print("Introduce el nombre del fichero: ");
		String nombreFichero = teclado.nextLine();
		int palabras = contarPalabras(nombreFichero);
		if (palabras >= 0) // Usamos -1 como señal de "hubo error"
			System.out.println("El fichero contiene " + palabras + " palabras");
	}

	// Devuelve el número de palabras o -1 si no se ha podido leer el fichero
	public static int contarPalabras(String nombreFichero) {
		int total = 0;
		try (BufferedReader lector = new BufferedReader(new FileReader(nombreFichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				// Separamos por un espacio. Si hay varios seguidos salen trozos vacíos ""
				String[] trozos = linea.split(" ");
				for (int i = 0; i < trozos.length; i++) {
					if (!trozos[i].isEmpty()) // Solo contamos los trozos que tengan algo: son las palabras reales
						total++;
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
			total = -1; // Avisamos al que nos llamó de que falló
		}
		return total;
	}
}
