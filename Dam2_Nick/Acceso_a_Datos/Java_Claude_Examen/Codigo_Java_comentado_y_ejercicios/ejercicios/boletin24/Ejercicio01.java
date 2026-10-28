import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ejercicio01 {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		int numero = 0;
		// Pedimos el número hasta que sea un entero entre 1 y 10
		do {
			System.out.print("Introduce un número entre 1 y 10: ");
			String texto = teclado.nextLine();
			try {
				numero = Integer.parseInt(texto.trim()); // Lanza NumberFormatException si no es un entero
			} catch (NumberFormatException e) {
				numero = 0; // Valor no válido para que el bucle repita
			}
			if (numero < 1 || numero > 10)
				System.out.println("Número no válido");
		} while (numero < 1 || numero > 10);
		guardarTabla(numero);
	}

	public static void guardarTabla(int numero) {
		String nombreFichero = "tabla-" + numero + ".txt"; // tabla-3.txt, tabla-7.txt...
		// PrintWriter con printf nos deja alinear columnas igual que en pantalla
		try (PrintWriter escritor = new PrintWriter(new FileWriter(nombreFichero))) {
			for (int i = 1; i <= 10; i++) {
				// %2d reserva 2 posiciones y alinea a la derecha: así "1" y "10" quedan alineados
				// El número elegido también usa %2d para que el 10 no desplace las columnas
				escritor.printf("%2d x %2d = %3d%n", numero, i, numero * i);
			}
			System.out.println("Tabla guardada en " + nombreFichero);
		} catch (Exception e) {
			System.out.println("Error al escribir: " + e.getMessage());
		}
	}
}
