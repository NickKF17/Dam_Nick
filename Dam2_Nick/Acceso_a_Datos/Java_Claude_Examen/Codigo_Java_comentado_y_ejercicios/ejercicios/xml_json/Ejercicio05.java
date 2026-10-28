import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Ejercicio 5: dar de alta un pokemon en XML o JSON. Sin teclado: todo por parámetros
// Dos versiones de altaPokemon (sobrecarga): con un tipo y con dos tipos
public class Ejercicio05 {

	public static void main(String[] args) {
		// Partimos de los ficheros de los ejercicios 3 y 4
		altaPokemon("xml", "datos/pokemons.xml", 25, "Pikachu", 6.0, 0.4, "Electrico");
		altaPokemon("json", "datos/pokemons.json", 6, "Charizard", 90.5, 1.7, "Fuego", "Volador");
		// Formato desconocido
		altaPokemon("csv", "datos/pokemons.csv", 1, "Bulbasaur", 6.9, 0.7, "Planta");
	}

	// Versión con un solo tipo: delega en la de dos tipos
	public static void altaPokemon(String formato, String fichero, int codigo, String nombre, double peso, double altura,
			String tipo) {
		altaPokemon(formato, fichero, codigo, nombre, peso, altura, tipo, null);
	}

	// Versión con dos tipos. Si tipo2 es null el pokemon solo tiene un tipo
	public static void altaPokemon(String formato, String fichero, int codigo, String nombre, double peso, double altura,
			String tipo1, String tipo2) {
		// Unimos los tipos en una cadena, igual que en los ejercicios anteriores
		String tipos = tipo1;
		if (tipo2 != null)
			tipos = tipo1 + ", " + tipo2;
		PokemonDex nuevo = new PokemonDex(codigo, nombre, peso, altura, tipos);
		if (formato.equalsIgnoreCase("xml"))
			altaEnXml(fichero, nuevo);
		else if (formato.equalsIgnoreCase("json"))
			altaEnJson(fichero, nuevo);
		else
			System.out.println("Formato no válido: " + formato + ". Usa xml o json");
	}

	private static void altaEnXml(String fichero, PokemonDex nuevo) {
		try {
			// Reutilizamos leerXML de la agenda y la creación del elemento del ejercicio 3
			Document doc = AgendaXML.leerXML(fichero);
			Element raiz = doc.getDocumentElement();
			raiz.appendChild(Ejercicio03.crearElementoPokemon(doc, nuevo));
			AgendaXML.grabarXML(doc, fichero);
			System.out.println("Pokemon " + nuevo.getNombre() + " añadido al XML");
		} catch (Exception e) {
			System.out.println("Error al añadir en XML: " + e.getMessage());
		}
	}

	private static void altaEnJson(String fichero, PokemonDex nuevo) {
		// Cargamos la lista existente, añadimos y volvemos a guardar todo
		List<PokemonDex> lista = Ejercicio04.cargarPokedex(fichero);
		if (lista != null) {
			lista.add(nuevo);
			Ejercicio04.guardarPokedex(fichero, lista);
			System.out.println("Pokemon " + nuevo.getNombre() + " añadido al JSON");
		}
	}
}
