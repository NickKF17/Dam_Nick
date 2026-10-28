package tercerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// LocalDateTime: fecha y hora actuales. DateTimeFormatter: para convertirla a texto con el formato que pide MySQL.
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Databases2 {

	// main: abre la conexión y decide qué operación ejecutar (solo borrarRegistros está activa).
	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/sakila";
		String usr = "josemaria";
		String pswd = "abc123";

		// Conexión con try-with-resources: se cierra sola.
		try (Connection conexion = DriverManager.getConnection(url, usr, pswd)) {
			System.out.println("Conexión realizada con exito\n");
			
			//insertarRegistro(conexion);
			//creacion(conexion);
			// Solo una operación activa a la vez: insertar, crear, borrar o ver info.
			borrarRegistros(conexion);
			//verInfo(conexion);
			
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	// INSERT con PreparedStatement.
	public static void insertarRegistro(Connection conexion) throws SQLException{
		// vamos a hacer un insert en la tabla actor de sakila
		// insert into actor values(NULL,'José María', 'Morales', '2026-04-16 12:00:00');
		// NULL en la primera columna: actor_id es AUTO_INCREMENT y MySQL pone el siguiente número él solo.
		// Los 3 ? son nombre, apellido y fecha de última modificación.
		PreparedStatement sql = conexion.prepareStatement("INSERT INTO actor VALUES (NULL, ?, ?, ?)");
		// Rellenamos el primer ? con el nombre.
		sql.setString(1, "José María");
		sql.setString(2, "Morales");
		// Fecha y hora del momento de ejecutar.
		LocalDateTime fechayHora = LocalDateTime.now();
		// NOTA del mentor: aquí hay un error sutil. 'YYYY' significa 'año del calendario semanal' y puede dar un año erróneo a finales de diciembre / principios de enero.
		// Lo correcto es 'yyyy' (minúsculas). No lo cambiamos para respetar el código original.
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
		// format() convierte la fecha en un String con el patrón indicado.
		String last_update = fechayHora.format(formato);
		// Pasamos la fecha como texto; MySQL la convierte a DATETIME.
		sql.setString(3, last_update);
		// cuando la sentencia sql ejecutada no devuelve un resultado (ResultSet) usamos executeUpdate
		// en lugar de executeQuery
		// cuando la sentencia es un insert, delete o update devuelve un entero con el número de filas
		// afectadas por la instrucción
		// cuando la sentencia es un create, alter, drop o similar devuelve siempre 0
		// executeUpdate se usa con INSERT/UPDATE/DELETE/CREATE/DROP... (todo lo que NO devuelve ResultSet).
		sql.executeUpdate();
	}
	
	// Crear base de datos y tabla desde Java (sentencias DDL).
	public static void creacion(Connection conexion) throws SQLException{
		// vamos ahora a crear, si no existe, una base de datos y una tabla
		 // Cada sentencia SQL va en su propio String y se ejecuta por separado.
		 String query1 = "CREATE DATABASE IF NOT EXISTS agenda;";
	     String query2 = "USE agenda";
	     // Tabla personas: teléfono como clave primaria y nombre. IF NOT EXISTS evita error si ya existía.
	     String query3 = "CREATE TABLE IF NOT EXISTS personas(telefono INT(9) PRIMARY KEY, nombre VARCHAR(50));";
	     
	     // Para SQL fijo, sin parámetros, basta un Statement.
	     Statement consulta = conexion.createStatement();
         // Crea la BD si no existía.
         consulta.executeUpdate(query1);
         // USE selecciona la BD para esa conexión.
         consulta.executeUpdate(query2);
         // Crea la tabla dentro de la BD seleccionada.
         consulta.executeUpdate(query3);
	}
	
	// DELETE: elimina las filas que cumplan la condición.
	public static void borrarRegistros(Connection conexion) throws SQLException{
	     Statement consulta = conexion.createStatement();
         // NOTA del mentor: aquí el valor 'Morales' está escrito dentro del SQL. Si viniera del usuario habría que usar PreparedStatement con ? para evitar la inyección SQL.
         // Si no hay WHERE se borra TODA la tabla.
         consulta.executeUpdate("DELETE FROM actor WHERE last_name='Morales'");
	}
	
	// Con SQL también se consulta información de la BD (SHOW TABLES, SHOW DATABASES, DESCRIBE...).
	public static void verInfo(Connection conexion) throws SQLException{
		// podemos tambien ver las bases de datos, las tablas y cualquier otro listado de información
		String query01 = "SHOW TABLES;";
		Statement consulta = conexion.createStatement();
        // SHOW TABLES devuelve un ResultSet como cualquier SELECT.
        ResultSet ejecucion = consulta.executeQuery(query01);
        // Recorremos los nombres de las tablas.
        while(ejecucion.next()){
            // getRow() = nº de fila actual; getString(1) = primera (y única) columna. %n es el salto de línea portable.
            System.out.printf("%d - %s%n",ejecucion.getRow(), ejecucion.getString(1));
        }
	}

}
