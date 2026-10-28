import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Ejercicio07 {

	public static void main(String[] args) {
		ArrayList<Contacto> agenda = leerAgenda("datos/agenda7.txt");
		System.out.println("Contactos correctos: " + agenda.size());
		for (Contacto c : agenda) // Recorremos la lista y mostramos cada objeto (usa su toString)
			System.out.println(" - " + c);
	}

	public static boolean esTexto(String linea) {
		return linea.matches("[\\p{L} ]+");
	}

	public static boolean esEntero(String linea) {
		return linea.matches("\\d+");
	}

	// Lee el fichero y devuelve una lista solo con los contactos válidos
	public static ArrayList<Contacto> leerAgenda(String fichero) {
		ArrayList<Contacto> contactos = new ArrayList<>(); // Lista vacía donde iremos añadiendo
		try {
			ArrayList<String> lineas = new ArrayList<>(Files.readAllLines(Path.of(fichero)));
			// Recorremos de 3 en 3, pero solo si quedan 3 líneas completas
			for (int i = 0; i + 2 < lineas.size(); i += 3) {
				String nombre = lineas.get(i);
				String apellido = lineas.get(i + 1);
				String edad = lineas.get(i + 2);
				if (esTexto(nombre) && esTexto(apellido) && esEntero(edad)) {
					// Integer.parseInt convierte "53" en el entero 53
					contactos.add(new Contacto(nombre, apellido, Integer.parseInt(edad)));
				} else {
					System.out.println("Registro incorrecto (línea " + (i + 1) + "), se ignora");
				}
			}
			if (lineas.size() % 3 != 0)
				System.out.println("Quedan líneas sueltas al final: registro incompleto, se ignora");
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
		return contactos;
	}
}
