import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Boletín 26 - Ejercicio 1 (BD classicmodels)
 * Dado un apellido, listar los empleados que lo tienen y, de cada uno,
 * los empleados que le reportan (columna reportsTo).
 */
public class Ejercicio01 {

	// Datos de conexión. CÁMBIALOS por los de tu servidor (usuario y contraseña)
	static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	// Consulta 1: los empleados con ese apellido (el ? se rellena después)
	static final String SQL_EMPLEADOS = "SELECT employeeNumber, firstName, lastName FROM employees WHERE lastName = ?";
	// Consulta 2: los empleados cuyo jefe (reportsTo) es el empleado que le pasemos
	static final String SQL_REPORTAN = "SELECT firstName, lastName FROM employees WHERE reportsTo = ?";

	public static void main(String[] args) {
		// Probamos con los tres casos de la nota del enunciado
		listarReportan("Patterson"); // tres empleados, con 4, 3 y 0 subordinados
		listarReportan("Morales"); // no existe ningún empleado con ese apellido
	}

	public static void listarReportan(String apellido) {
		// try-with-resources: la conexión se cierra sola al terminar el bloque
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			// Preparamos la primera consulta y le damos valor al ? (posición 1)
			PreparedStatement sqlJefes = conexion.prepareStatement(SQL_EMPLEADOS);
			sqlJefes.setString(1, apellido);
			ResultSet jefes = sqlJefes.executeQuery();
			// Bandera para saber si hemos encontrado a alguien con ese apellido
			boolean encontrado = false;
			// Preparamos UNA vez la segunda consulta y la reutilizamos en cada vuelta
			PreparedStatement sqlReportan = conexion.prepareStatement(SQL_REPORTAN);
			// Recorremos cada empleado con ese apellido
			while (jefes.next()) {
				// Si entramos aquí al menos una vez, existe algún empleado
				encontrado = true;
				System.out.println("Empleado: " + jefes.getString("firstName") + " " + jefes.getString("lastName"));
				// Buscamos a quienes le reportan usando su número de empleado
				sqlReportan.setInt(1, jefes.getInt("employeeNumber"));
				ResultSet subordinados = sqlReportan.executeQuery();
				// Bandera para detectar el caso "no le reporta nadie"
				boolean tieneSubordinados = false;
				while (subordinados.next()) {
					tieneSubordinados = true;
					System.out.println("   - " + subordinados.getString("firstName") + " " + subordinados.getString("lastName"));
				}
				// Si el bucle no dio ninguna vuelta, nadie le reporta
				if (!tieneSubordinados)
					System.out.println("   (A este empleado no le reporta nadie)");
			}
			// Si ninguna vuelta del bucle principal se ejecutó, el apellido no existe
			if (!encontrado)
				System.out.println("Error: no existe ningún empleado con el apellido " + apellido);
		} catch (SQLException e) {
			// getMessage() nos dice qué ha fallado (conexión, SQL mal escrito...)
			System.err.println("Error: " + e.getMessage());
		}
	}
}
