import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ejercicio05 {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		String contenido = "";   // Aquí vamos montando las líneas del fichero
		double total = 0;        // Coste acumulado
		int articulos = 0;       // Número de artículos
		String seguir;
		do {
			System.out.print("Introduce un artículo: ");
			String articulo = teclado.nextLine();
			double cantidad = pedirNumero(teclado, "Introduce la cantidad: ");
			double precio = pedirNumero(teclado, "Introduce el precio: ");
			contenido += formatear(cantidad) + " " + articulo + "\n";
			total += cantidad * precio; // Coste de esta línea = cantidad x precio
			articulos++;
			System.out.print("Quieres seguir introduciendo artículos en la lista (si/no): ");
			seguir = teclado.nextLine().trim();
		} while (seguir.equalsIgnoreCase("si"));
		total = Math.round(total * 100) / 100.0; // 2 decimales
		try (PrintWriter escritor = new PrintWriter(new FileWriter("compra.txt"))) {
			escritor.print(contenido);
			escritor.println("Total artículos en la lista: " + articulos);
			escritor.println("Precio de la compra: " + total);
			System.out.println("Tu lista de la compra se encuentra en el fichero compra.txt");
		} catch (Exception e) {
			System.out.println("Error al escribir: " + e.getMessage());
		}
	}

	// Pide un número decimal y repite si lo escrito no es válido
	public static double pedirNumero(Scanner teclado, String mensaje) {
		double numero = 0;
		boolean valido = false;
		while (!valido) {
			System.out.print(mensaje);
			try {
				numero = Double.parseDouble(teclado.nextLine().trim());
				valido = true;
			} catch (NumberFormatException e) {
				System.out.println("Eso no es un número");
			}
		}
		return numero;
	}

	// Si el número no tiene decimales (2.0) lo mostramos como entero (2)
	public static String formatear(double numero) {
		String texto;
		if (numero == (int) numero) // Comparamos con su parte entera
			texto = "" + (int) numero;
		else
			texto = "" + numero;
		return texto;
	}
}
