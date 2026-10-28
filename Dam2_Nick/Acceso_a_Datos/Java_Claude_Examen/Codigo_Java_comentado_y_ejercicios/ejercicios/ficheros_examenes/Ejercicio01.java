import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

// Ejercicio 1: relacionar animes.txt y personajes.txt mediante el número que actúa como "clave"
public class Ejercicio01 {

	public static void main(String[] args) {
		// Rutas de los dos ficheros en variables para poder cambiarlas en un único sitio
		String ficheroAnimes = "datos/animes.txt";
		String ficheroPersonajes = "datos/personajes.txt";
		mostrarAnimesYPersonajes(ficheroAnimes, ficheroPersonajes);
	}

	// Función principal del ejercicio: construye la salida por consola
	public static void mostrarAnimesYPersonajes(String ficheroAnimes, String ficheroPersonajes) {
		try {
			// Paso 1: leer los animes en un diccionario código -> título
			TreeMap<Integer, String> animes = leerAnimes(ficheroAnimes);
			// Paso 2: por cada anime (recorremos las claves del diccionario)
			for (Integer codigo : animes.keySet()) {
				// Título del anime con esa clave
				System.out.println(animes.get(codigo));
				// Lista de personajes de ese anime (función modular reutilizable)
				ArrayList<String> personajes = personajesDeAnime(codigo, ficheroPersonajes);
				if (personajes.size() == 0) {
					// Anime sin personajes
					System.out.println("- No hay personajes");
				} else {
					// Ordenamos alfabéticamente para que la salida coincida con la del enunciado
					Collections.sort(personajes);
					for (String personaje : personajes)
						System.out.println("- " + personaje);
				}
				// Línea en blanco para separar bloques
				System.out.println();
			}
			// Paso 3: personajes cuyo código no está en el fichero de animes
			ArrayList<String> sinAnime = personajesSinAnime(animes, ficheroPersonajes);
			// Solo mostramos el bloque si hay alguno
			if (sinAnime.size() > 0) {
				System.out.println("Personajes sin anime");
				for (String personaje : sinAnime)
					System.out.println("- " + personaje);
			}
		} catch (Exception e) {
			// Fichero inexistente o ilegible
			System.out.println("Error al leer los ficheros: " + e.getMessage());
		}
	}

	// Lee animes.txt y devuelve un diccionario ordenado por clave (TreeMap) código -> título
	public static TreeMap<Integer, String> leerAnimes(String fichero) throws Exception {
		TreeMap<Integer, String> animes = new TreeMap<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				// Ignoramos líneas en blanco por seguridad
				if (!linea.trim().equals("")) {
					// El número es lo que hay antes del primer espacio; el título, lo que hay después
					int posicion = linea.indexOf(' ');
					int codigo = Integer.parseInt(linea.substring(0, posicion));
					String titulo = linea.substring(posicion + 1);
					animes.put(codigo, titulo);
				}
			}
		}
		return animes;
	}

	// Devuelve los nombres de los personajes cuyo código coincide con el recibido
	public static ArrayList<String> personajesDeAnime(int codigoBuscado, String fichero) throws Exception {
		ArrayList<String> lista = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				if (!linea.trim().equals("")) {
					int posicion = linea.indexOf(' ');
					int codigo = Integer.parseInt(linea.substring(0, posicion));
					// Si el código es el buscado guardamos el nombre (lo que hay tras el primer espacio)
					if (codigo == codigoBuscado)
						lista.add(linea.substring(posicion + 1));
				}
			}
		}
		return lista;
	}

	// Devuelve los personajes cuyo código no existe como clave en el diccionario de animes
	public static ArrayList<String> personajesSinAnime(TreeMap<Integer, String> animes, String fichero) throws Exception {
		ArrayList<String> lista = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				if (!linea.trim().equals("")) {
					int posicion = linea.indexOf(' ');
					int codigo = Integer.parseInt(linea.substring(0, posicion));
					// containsKey dice si la clave existe en el diccionario
					if (!animes.containsKey(codigo))
						lista.add(linea.substring(posicion + 1));
				}
			}
		}
		return lista;
	}
}
