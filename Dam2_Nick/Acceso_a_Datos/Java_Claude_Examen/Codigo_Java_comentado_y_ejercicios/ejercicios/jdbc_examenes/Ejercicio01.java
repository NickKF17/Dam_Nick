import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Examen JDBC - Ejercicio 1 (BD classicmodels)
 * Dada una ciudad, listar los empleados de su oficina con su email.
 */
public class Ejercicio01 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	// Buscamos la oficina por ciudad
	static final String SQL_OFICINA = "SELECT officeCode FROM offices WHERE city = ?";
	// Empleados de una oficina, ordenados como en el ejemplo del enunciado
	static final String SQL_EMPLEADOS = "SELECT lastName, firstName, email FROM employees WHERE officeCode = ?";

	public static void main(String[] args) {
		empleadosDeCiudad("Paris");
		empleadosDeCiudad("Madrid"); // no hay oficina
	}

	public static void empleadosDeCiudad(String ciudad) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			PreparedStatement sqlOficina = conexion.prepareStatement(SQL_OFICINA);
			sqlOficina.setString(1, ciudad);
			ResultSet oficina = sqlOficina.executeQuery();
			if (!oficina.next()) {
				// Sin filas: la ciudad no tiene oficina
				System.out.println("No existe oficina en " + ciudad);
			} else {
				// officeCode es un VARCHAR en classicmodels, por eso usamos getString/setString
				String codigo = oficina.getString("officeCode");
				// ResultSet "scrollable" (TYPE_SCROLL_INSENSITIVE) para poder usar last() y getRow()
				// y así saber cuántas filas hay ANTES de listarlas (como en Databases1)
				PreparedStatement sqlEmpleados = conexion.prepareStatement(SQL_EMPLEADOS,
						ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				sqlEmpleados.setString(1, codigo);
				ResultSet empleados = sqlEmpleados.executeQuery();
				// Nos vamos a la última fila; si no hay ninguna, last() devuelve false
				if (!empleados.last()) {
					System.out.println("La oficina de " + ciudad + " no tiene empleados");
				} else {
					// getRow() en la última fila = número total de filas (como un size())
					System.out.println("Hay " + empleados.getRow() + " empleados en la oficina de " + ciudad + ". Sus datos son:");
					// Volvemos al principio (antes de la primera) y listamos con next()
					empleados.beforeFirst();
					while (empleados.next()) {
						System.out.println(empleados.getString("lastName") + ", " + empleados.getString("firstName")
								+ " (" + empleados.getString("email") + ")");
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
