import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

// Ejercicio 2: guardar en binario los personajes que tienen anime y recuperarlos
// Reutilizamos las funciones públicas del ejercicio 1 (mismo código, sin repetirlo)
public class Ejercicio02 {

	public static void main(String[] args) {
		String ficheroAnimes = "datos/animes.txt";
		String ficheroPersonajes = "datos/personajes.txt";
		String ficheroBinario = "datos/personajes.dat";
		escribirPersonajes(ficheroAnimes, ficheroPersonajes, ficheroBinario);
		leerPersonajes(ficheroBinario);
	}

	// Escribe en el binario solo los personajes "completos" (con anime). Los animes sin personajes no generan objetos
	public static void escribirPersonajes(String ficheroAnimes, String ficheroPersonajes, String ficheroBinario) {
		ArrayList<Personaje> lista = new ArrayList<>();
		try {
			// Reutilizamos leerAnimes del ejercicio 1
			TreeMap<Integer, String> animes = Ejercicio01.leerAnimes(ficheroAnimes);
			for (Integer codigo : animes.keySet()) {
				String titulo = animes.get(codigo);
				// Reutilizamos personajesDeAnime del ejercicio 1
				ArrayList<String> nombres = Ejercicio01.personajesDeAnime(codigo, ficheroPersonajes);
				// Ordenamos igual que en el ejercicio 1 para obtener la misma salida
				Collections.sort(nombres);
				// Si no hay personajes, el for no se ejecuta y ese anime se omite
				for (String nombre : nombres)
					lista.add(new Personaje(titulo, nombre));
			}
		} catch (Exception e) {
			System.out.println("Error al leer los ficheros de texto: " + e.getMessage());
		}
		// Guardamos la lista entera como un único objeto (la lista y sus personajes son Serializable)
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(ficheroBinario))) {
			salida.writeObject(lista);
			System.out.println("Guardados " + lista.size() + " personajes en " + ficheroBinario);
		} catch (Exception e) {
			System.out.println("Error al escribir el binario: " + e.getMessage());
		}
	}

	// Lee el binario y muestra "Nombre (Anime)" por línea
	public static void leerPersonajes(String ficheroBinario) {
		try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(ficheroBinario))) {
			// Al leer, el cast es obligatorio. El compilador avisa (unchecked) pero es correcto aquí
			ArrayList<Personaje> lista = (ArrayList<Personaje>) entrada.readObject();
			for (Personaje p : lista)
				// println llama automáticamente a toString()
				System.out.println(p);
		} catch (Exception e) {
			System.out.println("Error al leer el binario: " + e.getMessage());
		}
	}
}
