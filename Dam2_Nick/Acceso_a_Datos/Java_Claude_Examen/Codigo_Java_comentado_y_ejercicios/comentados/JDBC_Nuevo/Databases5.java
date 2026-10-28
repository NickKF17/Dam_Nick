package primerTrimestre;

// Clases JDBC.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Modificar una fila con ResultSet actualizable (sobre la BD dam2).
public class Databases5 {

	public static void main(String[] args) {
		// URL de la BD dam2.
		String jdbcURL = "jdbc:mysql://localhost:3306/dam2";
		// Usuario.
		String usuario = "profe";
		// Contraseña.
		String password = "abc123";

		// También podemos usar un try-with-resources
		// try-with-resources.
		try(Connection conn = DriverManager.getConnection(jdbcURL, usuario, password)) {
			System.out.println("Conexión realizada correctamente");

			// SCROLL_INSENSITIVE + UPDATABLE: se puede navegar y modificar.
			Statement sql = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// Consulta.
			String query = "SELECT * FROM alumnos";

			// Ejecutamos.
			ResultSet res = sql.executeQuery(query);
			// Colocamos el cursor en la primera fila.
			res.first();

			// Cambiamos el nombre en memoria.
			res.updateString("nombre", "Pepe");
			// Lo enviamos a la BD (con autocommit activo se guarda ya).
			res.updateRow();
			
			// Repetimos la consulta para ver el resultado real.
			res = sql.executeQuery("SELECT * FROM alumnos");
			// Listado.
			while (res.next())
				System.out.println(res.getString("nombre") + " - " + res.getString("email"));

		// Errores de BD.
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
