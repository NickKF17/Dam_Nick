import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Ejercicio08 {

	// Expresión regular de una línea correcta:
	//  apellidos (letras y espacios) + ", " + nombre (letras y espacios) + ";"
	//  + puesto (letras, números y espacios) + ";" + salario (entero o con decimales)
	static final String FORMATO = "[\\p{L}]+( [\\p{L}]+)*, [\\p{L}]+( [\\p{L}]+)*;"
			+ "[\\p{L}\\p{N}]+( [\\p{L}\\p{N}]+)*;\\d+(\\.\\d+)?";

	public static void main(String[] args) {
		String origen = "datos/origen.txt";
		String destino = "datos/salida.txt";
		int correctas = 0;
		try (BufferedReader lector = new BufferedReader(new FileReader(origen));
				PrintWriter escritor = new PrintWriter(new FileWriter(destino))) {
			String linea;
			System.out.println("Líneas erróneas:");
			while ((linea = lector.readLine()) != null) {
				if (linea.matches(FORMATO)) { // matches comprueba la línea ENTERA contra la expresión
					escritor.println(linea);
					correctas++;
				} else
					System.out.println(" - " + linea);
			}
			System.out.println("Líneas correctas grabadas en " + destino + ": " + correctas);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
