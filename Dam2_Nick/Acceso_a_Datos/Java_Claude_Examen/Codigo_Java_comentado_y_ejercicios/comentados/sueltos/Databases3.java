package tercerTrimestre;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Swing: librería gráfica de Java. JFrame = ventana, JTable = tabla visual, JScrollPane = barras de desplazamiento,
// DefaultTableModel = los datos que muestra la tabla.
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Databases3 {
	 // main solo llama a verJTable pasando los datos de conexión como parámetros (así la función es reutilizable).
	 public static void main(String[] args) {      
	        String database = "jdbc:mysql://localhost:3306/sakila";        
			String usr = "josemaria";
			String pswd = "abc123";
			// Ejemplo de como ver el resultado de un query en una tabla con swing
	       // La parte gráfica se hace en el método auxiliar.
	       verJTable(database,usr,pswd);
	    }
	       
	     // Recibe URL, usuario y clave y muestra el resultado de un SELECT en una ventana.
	     public static void verJTable(String database, String user, String password){
	        // Pedimos solo las tres columnas que vamos a mostrar.
	        String query01 = "SELECT actor_id, first_name, last_name FROM actor;";

        	// la parte sql es igual que si fuese por consola
	        // Conexión con try-with-resources.
	        try (Connection conexion = DriverManager.getConnection(database,user,password);) {

	            // Statement para lanzar la consulta.
	            Statement consulta = conexion.createStatement();
	            // Resultado de la consulta.
	            ResultSet ejecucion = consulta.executeQuery(query01);
	            
	           //Creamos una ventana
	            // Creamos la ventana con su título.
	            JFrame ventana = new JFrame("Contenido de la tabla actor");
	            
	            // Creamos una tabla con JTable
	            // El modelo guarda los datos; la tabla solo los dibuja (separación datos/vista).
	            DefaultTableModel modelo = new DefaultTableModel();
	            // JTable se construye a partir del modelo.
	            JTable tabla = new JTable(modelo);
	            
	            // Le metemos tres columnas a nuestra tabla, las que queremos mostrar
	            // Definimos las columnas (cabeceras) que verá el usuario.
	            modelo.addColumn("id");
	            modelo.addColumn("nombre");
	            modelo.addColumn("apellido");
	            
	            // Metemos la tabla en la ventana y le añadimos una barra de scroll
	            // Envolvemos la tabla en un JScrollPane para que salgan barras si hay muchas filas.
	            JScrollPane scroll = new JScrollPane(tabla);
	            // Añadimos el scroll (con la tabla dentro) al panel de contenido de la ventana.
	            ventana.getContentPane().add(scroll);
	                        
	            // Metemos el resultado del select en la tabla
	            // recorremos el ResultSet como hacemos normalmente
	            // Recorremos el ResultSet fila a fila, igual que en consola.
	            while(ejecucion.next()){
	            	// creamos una fila
	                // Una fila de la tabla es un array de Object (cada celda puede ser de un tipo distinto).
	                Object [] fila = new Object[3];
	                // llenamos esa fila con datos
	                // Rellenamos cada celda con el dato de su columna.
	                fila[0] = ejecucion.getInt("actor_id");
	                fila[1] = ejecucion.getString("first_name");
	                fila[2] = ejecucion.getString("last_name");
	                // añadimos la fila a la tabla
	                // addRow añade la fila al modelo y la tabla se actualiza sola.
	                modelo.addRow(fila); 
	            }
	            
	            // Últimos retoques de configuración de la ventana y, por último, la hacemos visible
	            // pack() ajusta el tamaño de la ventana al contenido.
	            ventana.pack();
	            // EXIT_ON_CLOSE: al cerrar la ventana termina el programa (si no, seguiría ejecutándose invisible).
	            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            // Hasta esta línea la ventana existe pero no se ve.
	            ventana.setVisible(true);
	       // Tratamos los errores de SQL mostrando información detallada.
	       } catch (SQLException ex) {
	        	// Podemos obtener información detallada acerca de las posibles excepciones
	            System.out.println("Ha ocurrido una excepción");
	            // getErrorCode() = código numérico del error propio del gestor (MySQL).
	            System.out.println("Código: " + ex.getErrorCode());
	            // getMessage() = descripción legible.
	            System.out.println("Mensaje: " + ex.getMessage());
	            // getSQLState() = código estándar SQL de 5 caracteres, igual en todos los gestores.
	            System.out.println("Estado: " + ex.getSQLState());
	        }
	    }
}
