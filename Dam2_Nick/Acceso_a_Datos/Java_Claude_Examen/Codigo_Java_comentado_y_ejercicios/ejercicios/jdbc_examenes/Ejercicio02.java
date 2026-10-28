import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Examen JDBC - Ejercicio 2 (BD classicmodels)
 * Mover todos los empleados de la oficina de una ciudad a la de otra.
 */
public class Ejercicio02 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	// Código de oficina a partir de la ciudad
	static final String SQL_OFICINA = "SELECT officeCode FROM offices WHERE city = ?";
	// Contar empleados de una oficina (la usamos antes y después de mover)
	static final String SQL_CONTAR = "SELECT COUNT(*) FROM employees WHERE officeCode = ?";
	// Mover: los empleados de la oficina origen pasan a la destino
	static final String SQL_MOVER = "UPDATE employees SET officeCode = ? WHERE officeCode = ?";

	public static void main(String[] args) {
		moverEmpleados("Tokyo", "Paris");
		moverEmpleados("Tokyo", "Madrid"); // destino inexistente
		moverEmpleados("Tokyo", "Paris"); // Tokyo ya está vacía
	}

	// Método auxiliar: devuelve el código de la oficina o null si no existe
	// Evita repetir el mismo código para origen y destino
	public static String codigoOficina(Connection conexion, String ciudad) throws SQLException {
		PreparedStatement sql = conexion.prepareStatement(SQL_OFICINA);
		sql.setString(1, ciudad);
		ResultSet resultado = sql.executeQuery();
		String codigo = null;
		if (resultado.next())
			codigo = resultado.getString("officeCode");
		return codigo;
	}

	// Método auxiliar: cuenta los empleados de una oficina
	public static int contarEmpleados(Connection conexion, String codigoOficina) throws SQLException {
		PreparedStatement sql = conexion.prepareStatement(SQL_CONTAR);
		sql.setString(1, codigoOficina);
		ResultSet resultado = sql.executeQuery();
		// COUNT(*) siempre devuelve exactamente una fila
		resultado.next();
		// La columna no tiene nombre cómodo, así que la leemos por posición (1)
		return resultado.getInt(1);
	}

	public static void moverEmpleados(String origen, String destino) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			System.out.println("Moviendo todos los empleados de " + origen + " a " + destino + ".");
			String codOrigen = codigoOficina(conexion, origen);
			String codDestino = codigoOficina(conexion, destino);
			// Validamos primero las dos ciudades
			if (codOrigen == null)
				System.out.println("No existe oficina en " + origen);
			else if (codDestino == null)
				System.out.println("No existe oficina en " + destino);
			else {
				int aMover = contarEmpleados(conexion, codOrigen);
				if (aMover == 0)
					System.out.println("La oficina de " + origen + " no tiene empleados");
				else {
					System.out.println("Se van a mover " + aMover + " empleados de " + origen + " a " + destino);
					PreparedStatement sqlMover = conexion.prepareStatement(SQL_MOVER);
					// Primer ? = nuevo código (destino); segundo ? = código antiguo (origen)
					sqlMover.setString(1, codDestino);
					sqlMover.setString(2, codOrigen);
					// executeUpdate devuelve cuántas filas ha cambiado
					sqlMover.executeUpdate();
					// Volvemos a contar en destino para mostrar el resultado real
					System.out.println("La oficina de " + destino + " tiene ahora " + contarEmpleados(conexion, codDestino) + " empleados");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
