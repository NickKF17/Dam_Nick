import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio09 {

	public static void main(String[] args) {
		String fichero = "datos/empleados.txt";
		Scanner teclado = new Scanner(System.in);
		try {
			ArrayList<String> lineas = new ArrayList<>(Files.readAllLines(Path.of(fichero)));
			ArrayList<String> nuevas = new ArrayList<>(); // Líneas con la edad ya añadida
			for (String linea : lineas) {
				String[] campos = linea.split(";");        // apellidos, nombre | puesto | salario
				String[] partes = campos[0].split(", ");   // partes[0] = apellidos, partes[1] = nombre
				int edad = pedirEdad(teclado, partes[1] + " " + partes[0]);
				nuevas.add(linea + ";" + edad);
			}
			// Solo escribimos cuando hemos terminado de leer: así no perdemos el fichero si algo falla
			Files.write(Path.of(fichero), nuevas);
			System.out.println("Fichero actualizado");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// Pregunta la edad hasta recibir un entero >= 18 y < 67
	public static int pedirEdad(Scanner teclado, String nombreCompleto) {
		int edad = 0;
		boolean valida = false;
		while (!valida) {
			System.out.print(nombreCompleto + ". ¿Cuál es su edad? ");
			try {
				edad = Integer.parseInt(teclado.nextLine().trim());
				if (edad >= 18 && edad < 67)
					valida = true;
				else
					System.out.println("La edad debe estar entre 18 y 66");
			} catch (NumberFormatException e) {
				System.out.println("Eso no es un número entero");
			}
		}
		return edad;
	}
}
