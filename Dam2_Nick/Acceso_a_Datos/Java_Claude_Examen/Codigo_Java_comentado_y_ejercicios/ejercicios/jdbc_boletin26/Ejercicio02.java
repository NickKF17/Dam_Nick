import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Boletín 26 - Ejercicio 2 (BD classicmodels)
 * Cambiar el empleado (salesRepEmployeeNumber) asignado a un cliente.
 * Hay que mostrar el empleado antiguo y el nuevo, y contemplar:
 * cliente inexistente, empleado nuevo inexistente y cliente sin empleado.
 */
public class Ejercicio02 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	// LEFT JOIN: si el cliente no tiene empleado, la fila del cliente SÍ sale
	// pero las columnas del empleado vienen a NULL (con un JOIN normal no saldría)
	static final String SQL_CLIENTE = "SELECT c.salesRepEmployeeNumber, e.firstName, e.lastName FROM customers c LEFT JOIN employees e ON c.salesRepEmployeeNumber = e.employeeNumber WHERE c.customerName = ?";
	// Para comprobar que el código de empleado nuevo existe y obtener su nombre
	static final String SQL_EMPLEADO = "SELECT firstName, lastName FROM employees WHERE employeeNumber = ?";
	// La modificación propiamente dicha
	static final String SQL_UPDATE = "UPDATE customers SET salesRepEmployeeNumber = ? WHERE customerName = ?";

	public static void main(String[] args) {
		cambiarEmpleado("Kelly's Gift Shop", 1370); // tiene empleado (Peter Marsh)
		cambiarEmpleado("Warburg Exchange", 1370); // no tiene empleado asignado
		cambiarEmpleado("Morales", 1370); // el cliente no existe
		cambiarEmpleado("Kelly's Gift Shop", 666); // el empleado no existe
	}

	public static void cambiarEmpleado(String cliente, int nuevoEmpleado) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			// Paso 1: ¿existe el cliente? Y de paso, ¿quién es su empleado actual?
			PreparedStatement sqlCliente = conexion.prepareStatement(SQL_CLIENTE);
			sqlCliente.setString(1, cliente);
			ResultSet datosCliente = sqlCliente.executeQuery();
			if (!datosCliente.next()) {
				// next() devolvió false: la consulta no tiene filas, el cliente no existe
				System.out.println("Error: el cliente " + cliente + " no existe");
			} else {
				// Guardamos los datos del empleado antiguo antes de seguir
				// Si no tiene empleado, firstName viene como NULL y getString devuelve null
				String nombreAntiguo = datosCliente.getString("firstName");
				String apellidoAntiguo = datosCliente.getString("lastName");

				// Paso 2: ¿existe el empleado nuevo?
				PreparedStatement sqlEmpleado = conexion.prepareStatement(SQL_EMPLEADO);
				sqlEmpleado.setInt(1, nuevoEmpleado);
				ResultSet datosNuevo = sqlEmpleado.executeQuery();
				if (!datosNuevo.next()) {
					System.out.println("Error: el empleado con código " + nuevoEmpleado + " no existe");
				} else {
					// Paso 3: mostramos el antiguo (contemplando que sea null)
					if (nombreAntiguo == null)
						System.out.println("El cliente " + cliente + " no tenía ningún empleado asignado");
					else
						System.out.println("Empleado anterior: " + nombreAntiguo + " " + apellidoAntiguo);
					// Mostramos el nuevo
					System.out.println("Empleado nuevo: " + datosNuevo.getString("firstName") + " " + datosNuevo.getString("lastName"));

					// Paso 4: actualizamos. executeUpdate devuelve las filas modificadas
					PreparedStatement sqlUpdate = conexion.prepareStatement(SQL_UPDATE);
					sqlUpdate.setInt(1, nuevoEmpleado);
					sqlUpdate.setString(2, cliente);
					int filas = sqlUpdate.executeUpdate();
					System.out.println("Cliente actualizado (" + filas + " fila modificada)");
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
