import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Ejercicio02 {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		int numero = 0;
		do {
			System.out.print("Introduce un número entre 1 y 10: ");
			try {
				numero = Integer.parseInt(teclado.nextLine().trim());
			} catch (NumberFormatException e) {
				numero = 0;
			}
			if (numero < 1 || numero > 10)
				System.out.println("Número no válido");
		} while (numero < 1 || numero > 10);
		mostrarTabla(numero);
	}

	public static void mostrarTabla(int numero) {
		String nombreFichero = "tabla-" + numero + ".txt";
		if (!new File(nombreFichero).exists()) { // Comprobamos antes si existe para dar un mensaje claro
			System.out.println("El fichero " + nombreFichero + " no existe");
		} else {
			try (BufferedReader lector = new BufferedReader(new FileReader(nombreFichero))) {
				String linea;
				while ((linea = lector.readLine()) != null)
					System.out.println(linea);
			} catch (Exception e) {
				System.out.println("Error al leer: " + e.getMessage());
			}
		}
	}
}
