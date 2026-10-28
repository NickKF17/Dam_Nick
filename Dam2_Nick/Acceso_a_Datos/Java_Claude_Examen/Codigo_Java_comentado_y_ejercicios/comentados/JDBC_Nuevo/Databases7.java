package primerTrimestre;

// Clases JDBC.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Lo mismo que antes, pero obteniendo la conexión del pool.
public class Databases7 {

	public static void main(String[] args) {
		// Ahora creamos nuestra conexión a través de la clase PoolConexiones que hemos creado
		// Como ves, no necesitamos crear ningún objeto de este tipo para llamar al metod getConnection()
		// y podemos usar try-with-resources
		// Método estático: no hace falta crear objetos. try-with-resources devuelve la conexión al pool.
		try(Connection con = PoolConexiones.getConnection()){
			System.out.println("Conexión realizada correctamente");
			
			// Usaremos prepareStatement para mejorar la eficiencia
			// Consulta.
			String query = "SELECT * FROM alumnos";
			// Sentencia preparada reutilizable, con cursor navegable y actualizable.
			PreparedStatement sql = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			// El resto es igual a lo que hemos visto hasta ahora
			// NOTA del mentor: con PreparedStatement executeQuery debe llamarse SIN argumentos. Pasar el String es redundante y el driver de MySQL lo permite pero no es lo correcto.
			ResultSet res = sql.executeQuery(query);
			// Primera fila.
			res.first();

			// Cambio.
			res.updateString("nombre", "Aurora");
			// Guarda.
			res.updateRow();
			
			// NOTA del mentor: mismo detalle; además aquí se ejecuta con un Statement implícito.
			res = sql.executeQuery("SELECT * FROM alumnos");
			// Listado.
			while (res.next())
				System.out.println(res.getString("nombre") + " - " + res.getString("email"));

		// Errores.
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
