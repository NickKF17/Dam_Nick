import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Scanner;

public class Ejercicio03 {

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		System.out.print("¿Cuántos números de Fibonacci quieres? ");
		String texto = teclado.nextLine().trim();
		try {
			int cantidad = Integer.parseInt(texto); // Falla si no es un entero
			if (cantidad < 1)
				System.out.println("Error: la cantidad debe ser al menos 1");
			else
				guardarFibonacci(cantidad);
		} catch (NumberFormatException e) {
			System.out.println("Error: '" + texto + "' no es un número entero");
		}
	}

	public static void guardarFibonacci(int cantidad) {
		// Los números de Fibonacci crecen muy deprisa: un long se desborda a partir del 93.
		// BigInteger no tiene límite de tamaño. Se suma con add() en vez de con +
		BigInteger anterior = BigInteger.ZERO; // 0
		BigInteger actual = BigInteger.ONE;    // 1
		String contenido = "";
		for (int i = 0; i < cantidad; i++) {
			if (i > 0)
				contenido += ", ";          // Separador entre números, pero no antes del primero
			contenido += anterior;          // Añadimos el número que toca (se convierte a texto solo)
			BigInteger siguiente = anterior.add(actual); // El siguiente es la suma de los dos últimos
			anterior = actual;               // Desplazamos la "ventana" de dos números
			actual = siguiente;
		}
		try (FileWriter escritor = new FileWriter("fibonacci.txt")) {
			escritor.write(contenido);
			System.out.println("Escrito fibonacci.txt: " + contenido.length() + " caracteres");
		} catch (Exception e) {
			System.out.println("Error al escribir: " + e.getMessage());
		}
	}
}
