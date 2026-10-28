import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Examen JDBC - Ejercicio 4 (BD naciones)
 * Listar los idiomas de un país distinguiendo los oficiales de los demás.
 * Tablas: countries(country_id, name...), languages(language_id, language),
 *         country_languages(country_id, language_id, official)
 */
public class Ejercicio04 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/naciones";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	static final String SQL_PAIS = "SELECT country_id FROM countries WHERE name = ?";
	// JOIN con languages para obtener el nombre del idioma, y traemos la columna official
	static final String SQL_IDIOMAS = "SELECT l.language, cl.official FROM country_languages cl JOIN languages l ON l.language_id = cl.language_id WHERE cl.country_id = ?";

	public static void main(String[] args) {
		mostrarIdiomas("Zimbabwe");
		mostrarIdiomas("Zambia"); // sin idioma oficial
		mostrarIdiomas("Samoa"); // dos oficiales
		mostrarIdiomas("Vanuatu"); // todos oficiales
		mostrarIdiomas("Latveria"); // no existe
	}

	public static void mostrarIdiomas(String pais) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			PreparedStatement sqlPais = conexion.prepareStatement(SQL_PAIS);
			sqlPais.setString(1, pais);
			ResultSet datosPais = sqlPais.executeQuery();
			if (!datosPais.next()) {
				System.out.println(pais + " no es un país real o no está dado de alta en la base de datos");
			} else {
				PreparedStatement sqlIdiomas = conexion.prepareStatement(SQL_IDIOMAS);
				sqlIdiomas.setInt(1, datosPais.getInt("country_id"));
				ResultSet idiomas = sqlIdiomas.executeQuery();
				// Dos textos acumuladores: uno para oficiales y otro para el resto
				String oficiales = "";
				String otros = "";
				while (idiomas.next()) {
					String idioma = idiomas.getString("language");
					// getBoolean sirve tanto si la columna es BOOLEAN como TINYINT 0/1
					if (idiomas.getBoolean("official")) {
						// Ponemos la coma solo si ya había algún idioma antes
						if (!oficiales.equals(""))
							oficiales += ", ";
						oficiales += idioma;
					} else {
						if (!otros.equals(""))
							otros += ", ";
						otros += idioma;
					}
				}
				// Mostramos cada bloque según tenga o no contenido
				if (oficiales.equals(""))
					System.out.println("No hay idioma oficial en " + pais);
				else
					System.out.println("Idioma oficial en " + pais + ": " + oficiales);
				if (otros.equals(""))
					System.out.println("No hay otros idiomas");
				else
					System.out.println("Otros idiomas: " + otros);
			}
			System.out.println();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
