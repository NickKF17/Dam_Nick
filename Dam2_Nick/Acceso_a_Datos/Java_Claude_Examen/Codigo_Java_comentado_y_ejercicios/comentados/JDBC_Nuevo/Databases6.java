package primerTrimestre;

// Clases JDBC.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Igual que Databases5 pero con control MANUAL de transacciones.
public class Databases6 {

	public static void main(String[] args) {
		// URL.
		String jdbcURL = "jdbc:mysql://localhost:3306/dam2";
		String usuario = "profe";
		String password = "abc123";

		// try-with-resources.
		try(Connection conn = DriverManager.getConnection(jdbcURL, usuario, password)) {
			// Los commit tienen que ser manuales
			// Desactiva el autocommit: los cambios quedan pendientes hasta hacer commit().
			conn.setAutoCommit(false);
			
			System.out.println("Conexión realizada correctamente");

			// ResultSet actualizable.
			Statement sql = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String query = "SELECT * FROM alumnos";

			ResultSet res = sql.executeQuery(query);
			// Primera fila.
			res.first();

			// Cambio en memoria.
			res.updateString("nombre", "Juan");
			// Se envía a la BD dentro de la transacción (aún no definitivo).
			res.updateRow();
			
			// Dentro de la misma conexión vemos el cambio aunque no esté confirmado.
			res = sql.executeQuery("SELECT * FROM alumnos");
			while (res.next())
				System.out.println(res.getString("nombre") + " - " + res.getString("email"));
			
			// si no ejecutamos commit los cambios en la base de datos no se guardan
			// aunque los hayamos visto en el listado anterior
			// pero al cerrar la base de datos se descartan y se vuelve a lo anterior
			// Sin commit(), al cerrar la conexión el cambio se descarta (rollback implícito).
			// conn.commit();

		// Errores.
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
