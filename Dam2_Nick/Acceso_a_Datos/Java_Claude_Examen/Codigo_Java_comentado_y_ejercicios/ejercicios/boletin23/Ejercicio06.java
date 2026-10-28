import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Ejercicio06 {

	public static void main(String[] args) {
		comprobar("datos/agenda_ok.txt");
		comprobar("datos/agenda_mal.txt");
		comprobar("datos/agenda_incompleta.txt");
	}

	public static void comprobar(String fichero) {
		System.out.println("Fichero " + fichero + ":");
		if (formatoCorrecto(fichero))
			System.out.println("  El formato es correcto");
		else
			System.out.println("  El formato NO es correcto");
	}

	// Un texto válido son solo letras y espacios (no puede ser un número)
	public static boolean esTexto(String linea) {
		return linea.matches("[\\p{L} ]+"); // Expresión regular: una o más letras/espacios
	}

	// Un entero son solo dígitos
	public static boolean esEntero(String linea) {
		return linea.matches("\\d+");
	}

	public static boolean formatoCorrecto(String fichero) {
		boolean correcto = true;
		try {
			ArrayList<String> lineas = new ArrayList<>(Files.readAllLines(Path.of(fichero)));
			// Cada registro son 3 líneas, así que el total debe ser múltiplo de 3 (y no estar vacío)
			if (lineas.size() == 0 || lineas.size() % 3 != 0)
				correcto = false;
			// Avanzamos de 3 en 3: i apunta siempre a la primera línea de cada registro
			for (int i = 0; i + 2 < lineas.size() && correcto; i += 3) {
				boolean nombreBien = esTexto(lineas.get(i));
				boolean apellidoBien = esTexto(lineas.get(i + 1));
				boolean edadBien = esEntero(lineas.get(i + 2));
				if (!nombreBien || !apellidoBien || !edadBien) {
					correcto = false;
					System.out.println("  Error en el registro que empieza en la línea " + (i + 1));
				}
			}
		} catch (Exception e) {
			System.out.println("  Error al leer: " + e.getMessage());
			correcto = false;
		}
		return correcto;
	}
}
