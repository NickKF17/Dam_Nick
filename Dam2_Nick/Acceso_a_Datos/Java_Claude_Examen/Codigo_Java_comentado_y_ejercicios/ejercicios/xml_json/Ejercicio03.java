import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Ejercicio 3: pasar el listado de pokemons de texto a XML
public class Ejercicio03 {

	public static void main(String[] args) {
		textoAXml("datos/pokemons.txt", "datos/pokemons.xml");
	}

	public static void textoAXml(String ficheroTexto, String ficheroXML) {
		try {
			ArrayList<PokemonDex> pokemons = leerPokemons(ficheroTexto);
			// Creamos un documento XML vacío (en la agenda lo leíamos de un fichero)
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.newDocument();
			// Elemento raíz
			Element raiz = doc.createElement("pokedex");
			doc.appendChild(raiz);
			for (PokemonDex p : pokemons)
				raiz.appendChild(crearElementoPokemon(doc, p));
			// Reutilizamos grabarXML de la agenda de clase
			AgendaXML.grabarXML(doc, ficheroXML);
			System.out.println("XML creado con " + pokemons.size() + " pokemons");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// Lee el fichero de texto y devuelve la lista de pokemons (la reutilizan los ejercicios 4 y 5)
	public static ArrayList<PokemonDex> leerPokemons(String fichero) throws Exception {
		ArrayList<PokemonDex> lista = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				if (!linea.trim().equals("")) {
					// Los datos se separan con coma y espacio
					String[] partes = linea.split(", ");
					int codigo = Integer.parseInt(partes[0]);
					double peso = Double.parseDouble(partes[2]);
					double altura = Double.parseDouble(partes[3]);
					// Del cuarto dato en adelante son los tipos (uno o dos): los unimos en una cadena
					String tipo = partes[4];
					if (partes.length > 5)
						tipo = tipo + ", " + partes[5];
					lista.add(new PokemonDex(codigo, partes[1], peso, altura, tipo));
				}
			}
		}
		return lista;
	}

	// Construye el elemento <pokemon> con sus hijos. También lo reutiliza el ejercicio 5
	public static Element crearElementoPokemon(Document doc, PokemonDex p) {
		Element pokemon = doc.createElement("pokemon");
		pokemon.appendChild(crearHijo(doc, "codigo", "" + p.getCodigo()));
		pokemon.appendChild(crearHijo(doc, "nombre", p.getNombre()));
		pokemon.appendChild(crearHijo(doc, "peso", "" + p.getPeso()));
		pokemon.appendChild(crearHijo(doc, "altura", "" + p.getAltura()));
		pokemon.appendChild(crearHijo(doc, "tipo", p.getTipo()));
		return pokemon;
	}

	// Crea un elemento <etiqueta>texto</etiqueta>
	private static Element crearHijo(Document doc, String etiqueta, String texto) {
		Element elemento = doc.createElement(etiqueta);
		elemento.setTextContent(texto);
		return elemento;
	}
}
