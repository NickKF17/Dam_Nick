import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Ejercicio 4: igual que el 3 pero la salida es JSON. Reutilizamos Ejercicio03.leerPokemons
public class Ejercicio04 {

	public static void main(String[] args) {
		textoAJson("datos/pokemons.txt", "datos/pokemons.json");
	}

	public static void textoAJson(String ficheroTexto, String ficheroJSON) {
		try {
			// Misma lectura que en el ejercicio anterior
			ArrayList<PokemonDex> pokemons = Ejercicio03.leerPokemons(ficheroTexto);
			guardarPokedex(ficheroJSON, pokemons);
			System.out.println("JSON creado con " + pokemons.size() + " pokemons");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	// Graba la lista en JSON bien indentado (la reutiliza el ejercicio 5)
	public static void guardarPokedex(String fichero, List<PokemonDex> pokemons) {
		Pokedex pokedex = new Pokedex();
		pokedex.setPokemons(pokemons);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer escritor = new FileWriter(fichero)) {
			gson.toJson(pokedex, escritor);
		} catch (Exception e) {
			System.out.println("Error al guardar el JSON: " + e.getMessage());
		}
	}

	// Lee un JSON de pokemons existente (lo usa el ejercicio 5 para poder añadir)
	public static List<PokemonDex> cargarPokedex(String fichero) {
		List<PokemonDex> lista = null;
		try (Reader lector = new FileReader(fichero)) {
			Pokedex pokedex = new Gson().fromJson(lector, Pokedex.class);
			lista = pokedex.getPokemons();
		} catch (Exception e) {
			System.out.println("Error al leer el JSON: " + e.getMessage());
		}
		return lista;
	}
}
