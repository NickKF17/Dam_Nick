import java.io.BufferedReader;
import java.io.FileReader;

public class Ejercicio10 {

	public static void main(String[] args) {
		analizarNumeros("datos/numeros.txt");
		System.out.println();
		analizarNumeros("datos/noexiste.txt");
	}

	public static void analizarNumeros(String fichero) {
		int validos = 0;
		int invalidos = 0;
		double suma = 0;
		double minimo = 0;
		double maximo = 0;
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				// Expresión regular: signo opcional, dígitos y, opcionalmente, punto con más dígitos
				if (linea.trim().matches("-?\\d+(\\.\\d+)?")) {
					double numero = Double.parseDouble(linea.trim());
					if (validos == 0) { // Primer dato válido: inicializa mínimo y máximo
						minimo = numero;
						maximo = numero;
					} else {
						if (numero < minimo)
							minimo = numero;
						if (numero > maximo)
							maximo = numero;
					}
					suma += numero;
					validos++;
				} else
					invalidos++; // No lo tenemos en cuenta pero lo contamos
			}
			System.out.println("Número de datos válidos: " + validos);
			System.out.println("Número de datos inválidos: " + invalidos);
			if (validos > 0) {
				double media = Math.round(suma / validos * 1000) / 1000.0; // 3 decimales
				System.out.println("Mínimo: " + minimo);
				System.out.println("Máximo: " + maximo);
				System.out.println("Media aritmética: " + media);
			}
		} catch (Exception e) {
			System.out.println("No se puede trabajar con el fichero: " + e.getMessage());
		}
	}
}
