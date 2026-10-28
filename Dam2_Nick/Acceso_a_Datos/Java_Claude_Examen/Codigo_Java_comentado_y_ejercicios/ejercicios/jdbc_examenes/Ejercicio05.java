import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Examen JDBC - Ejercicio 5 (BD naciones)
 * Marcar un idioma como oficial en un país, validando todos los casos.
 */
public class Ejercicio05 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/naciones";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	static final String SQL_PAIS = "SELECT country_id FROM countries WHERE name = ?";
	static final String SQL_IDIOMA = "SELECT language_id FROM languages WHERE language = ?";
	// Relación país-idioma: si no hay fila, el idioma no se habla en ese país
	static final String SQL_RELACION = "SELECT official FROM country_languages WHERE country_id = ? AND language_id = ?";
	static final String SQL_UPDATE = "UPDATE country_languages SET official = 1 WHERE country_id = ? AND language_id = ?";

	public static void main(String[] args) {
		hacerOficial("Zambia", "Bemba"); // correcto
		hacerOficial("Vanuatu", "Bislama"); // ya era oficial
		hacerOficial("Latveria", "Bemba"); // país inexistente
		hacerOficial("Zambia", "klingon"); // idioma inexistente
		hacerOficial("Zambia", "Spanish"); // idioma no hablado en el país
	}

	public static void hacerOficial(String pais, String idioma) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			// Validación 1: el país existe
			PreparedStatement sqlPais = conexion.prepareStatement(SQL_PAIS);
			sqlPais.setString(1, pais);
			ResultSet datosPais = sqlPais.executeQuery();
			if (!datosPais.next()) {
				System.out.println(pais + " no es un país real o no está dado de alta en la base de datos");
			} else {
				// Validación 2: el idioma existe
				PreparedStatement sqlIdioma = conexion.prepareStatement(SQL_IDIOMA);
				sqlIdioma.setString(1, idioma);
				ResultSet datosIdioma = sqlIdioma.executeQuery();
				if (!datosIdioma.next()) {
					System.out.println("El " + idioma + " no es un idioma real o no está dado de alta en la base de datos");
				} else {
					// Guardamos los dos identificadores para usarlos en las siguientes consultas
					int idPais = datosPais.getInt("country_id");
					int idIdioma = datosIdioma.getInt("language_id");
					// Validación 3: el idioma se habla en ese país
					PreparedStatement sqlRelacion = conexion.prepareStatement(SQL_RELACION);
					sqlRelacion.setInt(1, idPais);
					sqlRelacion.setInt(2, idIdioma);
					ResultSet relacion = sqlRelacion.executeQuery();
					if (!relacion.next()) {
						System.out.println("El idioma " + idioma + " no se habla en " + pais + " y no puedo hacerlo oficial");
					} else if (relacion.getBoolean("official")) {
						// Ya era oficial: no hace falta modificar nada
						System.out.println("El " + idioma + " ya es idioma oficial en " + pais);
					} else {
						// Todo correcto: lo hacemos oficial
						PreparedStatement sqlUpdate = conexion.prepareStatement(SQL_UPDATE);
						sqlUpdate.setInt(1, idPais);
						sqlUpdate.setInt(2, idIdioma);
						sqlUpdate.executeUpdate();
						System.out.println("El " + idioma + " es ahora idioma oficial en " + pais);
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
