import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Examen JDBC - Ejercicio 3 (BD pokemondb)
 * Dado el nombre de un pokemon, mostrar en qué evoluciona y de cuál proviene.
 * Tablas: pokemon(numero_pokedex, nombre, peso, altura)
 *         evoluciona_de(pokemon_evolucionado, pokemon_origen)
 */
public class Ejercicio03 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/pokemondb";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	static final String SQL_POKEMON = "SELECT numero_pokedex, nombre FROM pokemon WHERE nombre = ?";
	// Evoluciones: filas donde ESTE pokemon es el origen; unimos con pokemon para sacar el nombre del evolucionado
	static final String SQL_EVOLUCIONES = "SELECT p.numero_pokedex, p.nombre FROM evoluciona_de e JOIN pokemon p ON p.numero_pokedex = e.pokemon_evolucionado WHERE e.pokemon_origen = ? ORDER BY p.numero_pokedex";
	// Procedencia: filas donde ESTE pokemon es el evolucionado; el nombre es el del origen
	static final String SQL_ORIGEN = "SELECT p.numero_pokedex, p.nombre FROM evoluciona_de e JOIN pokemon p ON p.numero_pokedex = e.pokemon_origen WHERE e.pokemon_evolucionado = ?";

	public static void main(String[] args) {
		mostrarEvoluciones("Bulbasaur"); // evoluciona, no viene de nadie
		mostrarEvoluciones("Ivysaur"); // evoluciona y viene de otro
		mostrarEvoluciones("Venusaur"); // viene de otro pero no evoluciona
		mostrarEvoluciones("Mew"); // ni una cosa ni otra
		mostrarEvoluciones("Eevee"); // tres evoluciones
		mostrarEvoluciones("Sylveon"); // no existe
	}

	public static void mostrarEvoluciones(String nombre) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			PreparedStatement sqlPokemon = conexion.prepareStatement(SQL_POKEMON);
			sqlPokemon.setString(1, nombre);
			ResultSet pokemon = sqlPokemon.executeQuery();
			if (!pokemon.next()) {
				System.out.println("El pokemon " + nombre + " no está en la pokedex o no es de la primera generación");
			} else {
				int numero = pokemon.getInt("numero_pokedex");
				System.out.println(pokemon.getString("nombre") + " (#" + numero + ")");

				// Parte 1: ¿en qué evoluciona?
				PreparedStatement sqlEvol = conexion.prepareStatement(SQL_EVOLUCIONES);
				sqlEvol.setInt(1, numero);
				ResultSet evoluciones = sqlEvol.executeQuery();
				// Vamos construyendo el texto: puede haber varias evoluciones
				String texto = "";
				while (evoluciones.next())
					texto += " " + evoluciones.getString("nombre") + " (#" + evoluciones.getInt("numero_pokedex") + ")";
				// Si el texto sigue vacío, no hay evoluciones
				if (texto.equals(""))
					System.out.println("- no evoluciona en ningún otro");
				else
					System.out.println("- evoluciona en" + texto);

				// Parte 2: ¿de quién proviene?
				PreparedStatement sqlOrigen = conexion.prepareStatement(SQL_ORIGEN);
				sqlOrigen.setInt(1, numero);
				ResultSet origen = sqlOrigen.executeQuery();
				if (origen.next())
					System.out.println("- evoluciona de " + origen.getString("nombre") + " (#" + origen.getInt("numero_pokedex") + ")");
				else
					System.out.println("- no evoluciona de ningún otro");
			}
			System.out.println();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
