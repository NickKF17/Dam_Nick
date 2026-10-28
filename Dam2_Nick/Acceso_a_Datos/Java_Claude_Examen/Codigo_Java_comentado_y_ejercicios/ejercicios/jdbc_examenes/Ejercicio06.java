import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Examen JDBC - Ejercicio 6 (BD pokemondb)
 * Listar los pokemon de un tipo, indicando todos sus tipos.
 *
 * ATENCIÓN - SUPUESTOS: en el PDF solo se ven las tablas pokemon y evoluciona_de.
 * Los nombres de la tabla tipo y de la tabla que relaciona pokemon con tipo
 * NO aparecen, así que los he SUPUESTO. Compruébalos en tu base de datos con
 *    DESCRIBE tipo;   SHOW TABLES;   DESCRIBE <tabla de relación>;
 * y cambia SOLO las constantes de abajo si son distintos.
 */
public class Ejercicio06 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/pokemondb";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	// ---- SUPUESTOS (ajústalos a tu base de datos) ----
	static final String TABLA_TIPO = "tipo"; // tipo(id_tipo, nombre, generacion)
	static final String COL_TIPO_ID = "id_tipo"; // clave primaria de tipo
	static final String COL_TIPO_NOMBRE = "nombre"; // nombre del tipo ("Dragón")
	static final String TABLA_RELACION = "pokemon_tipo"; // tabla pokemon <-> tipo
	static final String COL_REL_POKEMON = "numero_pokedex"; // FK hacia pokemon
	static final String COL_REL_TIPO = "id_tipo"; // FK hacia tipo
	// ---------------------------------------------------

	// ¿Existe el tipo? Cogemos su id
	static final String SQL_TIPO = "SELECT " + COL_TIPO_ID + " FROM " + TABLA_TIPO + " WHERE " + COL_TIPO_NOMBRE + " = ?";
	// Pokemon que tienen ese tipo (JOIN entre pokemon y la tabla de relación)
	static final String SQL_POKEMONS = "SELECT p.numero_pokedex, p.nombre, p.peso, p.altura FROM pokemon p JOIN " + TABLA_RELACION + " r ON r." + COL_REL_POKEMON + " = p.numero_pokedex WHERE r." + COL_REL_TIPO + " = ? ORDER BY p.numero_pokedex";
	// TODOS los tipos de un pokemon concreto (para mostrar también los adicionales)
	static final String SQL_TIPOS_DE = "SELECT t." + COL_TIPO_NOMBRE + " FROM " + TABLA_RELACION + " r JOIN " + TABLA_TIPO + " t ON t." + COL_TIPO_ID + " = r." + COL_REL_TIPO + " WHERE r." + COL_REL_POKEMON + " = ?";

	public static void main(String[] args) {
		pokemonsDeTipo("Dragón");
		pokemonsDeTipo("Profesor"); // tipo inexistente
		pokemonsDeTipo("Acero"); // tipo existente (tras el INSERT) sin pokemon
	}

	public static void pokemonsDeTipo(String tipo) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			PreparedStatement sqlTipo = conexion.prepareStatement(SQL_TIPO);
			sqlTipo.setString(1, tipo);
			ResultSet datosTipo = sqlTipo.executeQuery();
			if (!datosTipo.next()) {
				System.out.println(tipo + " no es un tipo reconocido en esta Pokedex");
			} else {
				PreparedStatement sqlPokemons = conexion.prepareStatement(SQL_POKEMONS);
				sqlPokemons.setInt(1, datosTipo.getInt(COL_TIPO_ID));
				ResultSet pokemons = sqlPokemons.executeQuery();
				// Preparamos una sola vez la consulta de tipos y la reutilizamos
				PreparedStatement sqlTipos = conexion.prepareStatement(SQL_TIPOS_DE);
				boolean hayAlguno = false;
				while (pokemons.next()) {
					// Cabecera solo la primera vez, cuando sabemos que hay al menos uno
					if (!hayAlguno) {
						System.out.println("Los pokemon de tipo " + tipo + " son:");
						hayAlguno = true;
					}
					int numero = pokemons.getInt("numero_pokedex");
					System.out.println(numero + " – " + pokemons.getString("nombre") + ".");
					System.out.println("Peso: " + pokemons.getDouble("peso") + ". Altura: " + pokemons.getDouble("altura"));
					// Buscamos todos los tipos de este pokemon
					sqlTipos.setInt(1, numero);
					ResultSet tipos = sqlTipos.executeQuery();
					String lista = "";
					while (tipos.next()) {
						if (!lista.equals(""))
							lista += ", ";
						lista += tipos.getString(1);
					}
					System.out.println("Tipos: " + lista);
				}
				if (!hayAlguno)
					System.out.println("No existe ningún Pokemon de tipo " + tipo + " en esta Pokedex");
			}
			System.out.println();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
