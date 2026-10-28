import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Ejercicio08 {

	public static void main(String[] args) {
		mostrar(leerAgenda("datos/agenda8.txt"));
		System.out.println();
		mostrar(leerAgenda("datos/agenda8_mal.txt"));
	}

	public static void mostrar(ArrayList<Contacto> agenda) {
		System.out.println("Contactos leídos: " + agenda.size());
		for (Contacto c : agenda)
			System.out.println(" - " + c);
	}

	public static boolean esTexto(String linea) {
		return linea.matches("[\\p{L} ]+");
	}

	public static boolean esEntero(String linea) {
		return linea.matches("\\d+");
	}

	// El tipo solo puede ser una de las tres opciones
	public static boolean esTipo(String linea) {
		return linea.equals("Familia") || linea.equals("Amigo") || linea.equals("Conocido");
	}

	// Ahora los registros tienen 3 o 4 líneas, así que no podemos avanzar "de 3 en 3":
	// vamos leyendo con un índice i que avanza según lo que encontramos.
	// Si hay un error el formato queda roto y dejamos de leer (no sabemos dónde empieza el siguiente)
	public static ArrayList<Contacto> leerAgenda(String fichero) {
		ArrayList<Contacto> contactos = new ArrayList<>();
		try {
			ArrayList<String> lineas = new ArrayList<>(Files.readAllLines(Path.of(fichero)));
			int i = 0;
			boolean error = false;
			while (i < lineas.size() && !error) {
				String nombre = lineas.get(i);
				String apellidos = "";
				String tipo = "";
				int edad = 0;
				if (!esTexto(nombre)) {
					error = true;
				} else {
					i++; // Pasamos a la línea siguiente al nombre
					if (i < lineas.size() && esTipo(lineas.get(i))) {
						// Tras el nombre viene directamente el tipo: no hay apellidos
						tipo = lineas.get(i);
						i++;
					} else if (i < lineas.size() && esTexto(lineas.get(i))) {
						// Es un apellido: ahora TIENE que venir el tipo
						apellidos = lineas.get(i);
						i++;
						if (i < lineas.size() && esTipo(lineas.get(i))) {
							tipo = lineas.get(i);
							i++;
						} else
							error = true;
					} else
						error = true;
					// Por último, la edad
					if (!error) {
						if (i < lineas.size() && esEntero(lineas.get(i))) {
							edad = Integer.parseInt(lineas.get(i));
							i++;
							contactos.add(new Contacto(nombre, apellidos, tipo, edad));
						} else
							error = true;
					}
				}
			}
			if (error)
				System.out.println("Formato incorrecto cerca de la línea " + (i + 1) + ": dejo de leer");
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
		}
		return contactos;
	}
}
