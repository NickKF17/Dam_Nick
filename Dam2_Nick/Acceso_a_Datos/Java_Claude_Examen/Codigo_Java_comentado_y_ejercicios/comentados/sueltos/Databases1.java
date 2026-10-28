// Paquete al que pertenece la clase (carpeta lógica del proyecto en Eclipse).
package tercerTrimestre;

// Todas las clases JDBC viven en java.sql. Esto no necesita librerías externas...
// ...salvo el DRIVER (conector J), que se añade al Build Path y que Java carga solo.
import java.sql.Connection;
// DriverManager: fábrica de conexiones. Le das URL+usuario+clave y te devuelve una Connection.
import java.sql.DriverManager;
// PreparedStatement: consulta con parámetros (?) precompilada y segura.
import java.sql.PreparedStatement;
// ResultSet: tabla de resultados de un SELECT, que recorremos fila a fila.
import java.sql.ResultSet;
// SQLException: excepción específica de errores de base de datos (obligatorio tratarla).
import java.sql.SQLException;
// Statement: objeto para lanzar sentencias SQL 'normales' (sin parámetros).
import java.sql.Statement;

/*
 PARA TRABAJAR CON BASES DE DATOS MYSQL/MARIADB DESDE ECLIPSE:
 - Descargar el conector J (antes JDBC) de la página oficial de Oracle
    https://dev.mysql.com/downloads/connector/j/
    Elegir la opción de "plataforma independiente"
    Descargar el archivo con cualquiera de los dos formatos propuestos y descomprimirlo en local
    NOTA: aunque parezca que hay que crearse una cuenta, en la parte inferior de la página aparece una opción para descargar el archivo sin necesidad de hacerlo

- En el proyecto de Eclipse donde vamos a trabajar con el
    - Pulsar con el botón derecho sobre el nombre del proyecto
    - Elegir Build Path
    - Elegir Add External Archives
    - Navegar hasta donde hemos descomprimido el conector J y elegir el archivo .jar que estaba dentro
    IMPORTANTE: si borras el archivo del disco duro dejará de funcionar. 
    
    NOTA: si no nos aparece la opción de "Add External Archives" en Build Path elegir la perspectiva de Java y a continuación "Resetear" Perspectiva".
 */

// Clase principal del ejemplo. Cada 'ejemplo' es un método estático que recibe la conexión.
public class Databases1 {

	// Punto de entrada. Aquí solo abrimos la conexión y llamamos al ejemplo que queramos probar.
	public static void main(String[] args) {
		// En la cadena de conexión indicamos la ubicación del gestor de bases de datos
		// String url = "jdbc:mysql://localhost:3306/";
		// Y, opcionalmente, la base de datos con la que queremos trabajar.
		// Sería equivalente a ejecutar el comando USE después de hacer la conexión
		// URL JDBC = protocolo:gestor://servidor:puerto/basededatos. 3306 es el puerto por defecto de MySQL/MariaDB.
		String url = "jdbc:mysql://localhost:3306/sakila";
		// Usuario de la base de datos (NO el del sistema operativo).
		String usr = "josemaria";
		// Contraseña. En un proyecto real jamás se escribe en el código: iría en un fichero de configuración.
		String pswd = "abc123";

		// El objeto de la clase Connection es obligatorio para conectar a la base de
		// datos
		// podemos usar la estrategia de try-with-resource como en los ficheros
		// la conexión se cierra automáticamente al cerrarse el bloque try
		// Si no lo hacemos así tenemos que cerrar manualmente
		// try-with-resources: Connection implementa AutoCloseable, así que se cierra sola al salir del bloque, haya o no error.
		try (Connection conexion = DriverManager.getConnection(url, usr, pswd)) {
			// Si se ejecuta esta línea es que la conexión ha funcionado
			// Si no, saltaría al catch
			// Si llegamos aquí, getConnection NO lanzó excepción: usuario, clave y servidor son correctos.
			System.out.println("Conexión realizada con exito\n");
			
			//primerEjemplo(conexion);
			// Solo se ejecuta el segundo ejemplo; los demás están comentados. Descomenta el que quieras probar.
			segundoEjemplo(conexion);
			// tercerEjemplo(conexion);
			//cuartoEjemplo(conexion);
			
			// Si no usamos el try-with-resources al final de la ejecución hay que cerrar el
			// objeto de conexión
			// conexion.close();

		// Captura cualquier error de BD: servidor apagado, clave mala, SQL mal escrito, tabla inexistente...
		} catch (SQLException e) {
			// SQLException nos da mas información que Exception sobre los posibles errores
			// System.err es la salida de errores (en Eclipse sale en rojo). getMessage() da el motivo resumido.
			System.err.println("Error: " + e.getMessage());
			// printStackTrace() nos da mucha mas información pero también es mas "alarmante" para el usuario
			// Muestra la traza completa: útil para depurar, pero asusta al usuario final.
			e.printStackTrace();
		}
	}

	// Ejemplo 1: SELECT básico. 'throws SQLException' deja que sea el main quien trate el error.
	public static void primerEjemplo(Connection conexion) throws SQLException{
		// Statement es la clase que nos permite lanzar queryes
		// createStatement() crea el 'mensajero' que llevará nuestras SQL a la base de datos.
		Statement sql = conexion.createStatement();
		// Y ResultSet la que nos permite recoger los resultados
		// podemos hacer cualquier query con JOIN, WHERE, ORDER, etc!
		// executeQuery se usa SOLO con SELECT y devuelve un ResultSet con todas las filas.
		ResultSet resultado = sql.executeQuery("SELECT * FROM actor");
		// el método .next() devuelve false cuando el query ya no tenga mas resultados
		// mientras tanto, avanza a la siguiente línea de los resultados del query
		// cada vez que lo ejecutamos. Igual que readLine en un fichero!
		// next() avanza a la siguiente fila y devuelve false cuando no quedan más. Al principio el cursor está ANTES de la primera fila, por eso se usa while y no do-while.
		while (resultado.next()) {
			// Podemos recoger los resultados indicando el número de la columna (empezando por 1)
			// System.out.printf("ID: %d - %s, %s\n", resultado.getInt(1),
			// resultado.getString(3), resultado.getString(2));
			// O el nombre que devuelve la base de datos
			// Si lo hacemos con el número de la columna recuerda que es según el orden con que
			// aparecen en el query y no en la base de datos
			// getXxx("columna") lee la columna con el tipo adecuado: getInt para enteros, getString para textos.
			// Leer por nombre es más legible y no se rompe si cambia el orden de las columnas.
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
			// o con la posición que ocupa en el query, empezando por 0
		}
		// Otros get: getLong(), getDouble(), getFloat(), getBoolean(), getDate() y getTimeStamp()
	}
	
	// Ejemplo 2: ResultSet 'navegable' (se puede ir hacia atrás y saltar a una fila concreta).
	public static void segundoEjemplo(Connection conexion) throws SQLException{
		// Por defecto en los ResultSet sólo se puede avanzar hacía delante y no se pueden modificar los resultados
        // Pero se puede cambiar con los siguientes modificadores:
        // TYPE_FORWARD_ONLY: Sólo podemos ir hacía delante en el ResultSet
        // TYPE_SCROLL_INSENSITIVE: Podemos avanzar y retroceder y posicionarnos en el ResulSet
        // TYPE_SCROLL_SENSITIVE: Idem al anterior pero además las modificaciones hechas en la bb.dd. se reflejan en el ResultSet. 
        //		No implementado en MySQL/MariaDB
        // CONCUR_READ_ONLY: El ResultSet no puede modificarse
        // CONCUR_UPDATABLE: Podemos modificar el ResultSet y se actualiza el resultado en la base de datos
		// Las opciones por defecto si no ponemos nada son TYPE_FORWARD_ONLY y CONCUR_READ_ONLY
		
		// Segundo y tercer parámetro: tipo de cursor (SCROLL_INSENSITIVE = puedo moverme libremente)
		// y concurrencia (READ_ONLY = solo lectura).
		Statement sql = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		// Lanzamos la consulta. Ahora 'resultado' ya admite last(), previous(), absolute()...
		ResultSet resultado = sql.executeQuery("SELECT * FROM actor");
		// Nos posicionamos en la última linea
		// Colocamos el cursor en la ÚLTIMA fila.
		resultado.last();
		// getRow() nos dice el número de línea del total del resultado. Como es la última, sería como un size() en un ArrayList
		// getRow() devuelve el número de la fila actual; estando en la última, equivale al total de filas.
		System.out.println("El query ha devuelto " + resultado.getRow() + " líneas");
		// nos posicionamos después de la última
		// Cursor DESPUÉS de la última fila: punto de partida para recorrer hacia atrás.
		resultado.afterLast();
		// y ahora vamos retrocediendo con previous() en lugar de avanzando con next()
		// el bucle terminará ahora cuando nos situemos antes de la primera
		// podemos ir a esa posición con el método beforerFirst()
		// previous() retrocede una fila y devuelve false al llegar antes de la primera: recorrido inverso.
		while (resultado.previous()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
		// Otros métodos que nos permiten posicionarnos en el ResultSet:
		// absolute(n) Salta a la fila n, contando desde el principio (la 1 es la primera)
		// usando números negativos cuenta desde el final (la -1 es la última)
		// relative(n) salta n filas hacía adelante desde la posición actual. Con números negativos salta hacía atras.
	}
	
	// Ejemplo 3: MODIFICAR datos directamente desde el ResultSet (sin escribir un UPDATE).
	public static void tercerEjemplo(Connection conexion) throws SQLException{
		// vamos a modificar una línea
		// CONCUR_UPDATABLE permite cambiar el ResultSet y que el cambio llegue a la tabla.
		// OJO: el SELECT debe ser de una sola tabla y con clave primaria para que funcione.
		Statement sql = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		ResultSet resultado = sql.executeQuery("SELECT * FROM actor");
		// nos posicionamos en la última línea
		// resultado.last()
		// o en la tercera empezando por detrás
		// absolute(-3): tercera fila contando desde el final.
		resultado.absolute(-3);
		// o cinco por detrás de la anterior
		// relative(-5): retrocede 5 filas desde donde estamos ahora.
		resultado.relative(-5);
		// modificamos dos campos
		// updateXxx cambia el valor en memoria (en el ResultSet), todavía NO en la base de datos.
		resultado.updateString("first_name", "Inés");
		resultado.updateString("last_name", "Perado");
		// si no ejecutamos updateRow() los cambios se pierden
		// updateRow() confirma el cambio y lo envía a la base de datos. Sin esta línea, se pierde.
		resultado.updateRow();
		// nos colocamos antes de la primera línea
		// Volvemos ANTES de la primera fila para poder recorrer con next().
		resultado.beforeFirst();
		// y listamos
		// Listamos todo de nuevo para ver el cambio realizado.
		while (resultado.next()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
	}
	
	// Ejemplo 4: PreparedStatement (consultas con parámetros).
	public static void cuartoEjemplo(Connection conexion) throws SQLException{
		// PreparedStatement me permite parametrizar una query
		// Colocamos las incógnitas con ?
		// Se escribe el SQL con '?' en lugar de los valores. Se envía a preparar UNA vez.
		PreparedStatement sql = conexion.prepareStatement("SELECT * FROM actor WHERE first_name = ?");
		// Y aplicamos valores así
		// setXxx(posición, valor): la posición empieza en 1 y cada ? se rellena por orden.
		sql.setString(1, "MARY");
		// Con PreparedStatement executeQuery() va SIN argumentos: el SQL ya se dio al prepararlo.
		ResultSet resultado = sql.executeQuery();
		while (resultado.next()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
		
		// Podemos poner tantas como queramos
		// Reutilizamos la variable con otra consulta de dos parámetros.
		sql = conexion.prepareStatement("SELECT * FROM actor WHERE first_name = ? AND last_name = ?");
		sql.setString(1, "MARY");
		sql.setString(2, "KEITEL");
		resultado = sql.executeQuery();
		while (resultado.next()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
			}
		
		// Si queremos modificar el comportamiento del PreparedStatement ponemos los modificadores después del query
		// También aquí se pueden indicar el tipo de cursor y la concurrencia, detrás del SQL.
		sql = conexion.prepareStatement("SELECT * FROM actor WHERE first_name = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		sql.setString(1, "MARY");
		resultado = sql.executeQuery();
		// Ponemos el cursor al final para recorrer hacia atrás con previous().
		resultado.afterLast();
		while (resultado.previous()) {
			System.out.printf("ID: %d - %s, %s\n", resultado.getInt("actor_id"), resultado.getString("last_name"), resultado.getString("first_name"));
		}
		
	}
}
