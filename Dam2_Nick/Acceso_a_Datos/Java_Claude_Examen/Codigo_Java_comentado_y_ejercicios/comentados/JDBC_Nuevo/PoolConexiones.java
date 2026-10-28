package primerTrimestre;

// Clases JDBC.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// para usar los pool de conexiones de Apache descargamos las librerías de aquí:
// https://commons.apache.org/proper/commons-pool/download_pool.cgi
// https://commons.apache.org/proper/commons-dbcp/download_dbcp.cgi
// https://commons.apache.org/proper/commons-logging/download_logging.cgi
// descomprimimos y extraemos los jar principales
// los añadimos a nuestro build path (properties->java build path->libraries->classpath->add external jars
// BasicDataSource: pool de conexiones de Apache (librería externa). NOTA del mentor: los imports de Statement y DriverManager no se usan aquí.
import org.apache.commons.dbcp2.BasicDataSource;

// Clase de utilidad que reparte conexiones de un pool: se abren varias de antemano y se reutilizan.
public class PoolConexiones {
	
	// Único pool de la clase (static = compartido).
	private static BasicDataSource dataSource;
	
	// esto es un bloque de inicialización estática
	// nos garantiza que se ejecuta solo una vez, en el momento en que Java lee la clase
	// por primera vez y antes de crear ningún objeto de la misma, incluso
	// es parecido a un Singleton (un patrón de diseño muy conocido) pero no exactamente...
    // Bloque estático: se ejecuta una sola vez al cargar la clase.
    static {
    	// creamos el objeto y definimos la conexión
        // Creamos el pool.
        dataSource = new BasicDataSource();
        // Datos de conexión.
        dataSource.setUrl("jdbc:mysql://localhost:3306/dam2");
        dataSource.setUsername("profe");
        dataSource.setPassword("abc123");
        
        // Configuración del pool
        // 5 conexiones abiertas desde el inicio.
        dataSource.setInitialSize(5); // Creamos 5 conexiones iniciales que permanecen a la espera
        // Máximo 20; las peticiones extra esperan.
        dataSource.setMaxTotal(20);   // No podrán existir mas de 20 conexiones simultaneas. Si pedimos una mas queda a la espera de que se libere alguna
        // Cachea PreparedStatements.
        dataSource.setPoolPreparedStatements(true); // Activa el almacenamiento en caché de sentencias
        // Límite de sentencias en caché.
        dataSource.setMaxOpenPreparedStatements(50); // Define cuántas sentencias preparadas se guardarán en caché por cada conexión.
    }

    // Entrega una conexión del pool. Al hacer close() (o try-with-resources) NO se cierra de verdad: vuelve al pool.
    public static Connection getConnection() throws SQLException {
        // Pide una conexión libre.
        return dataSource.getConnection();
    }
}
