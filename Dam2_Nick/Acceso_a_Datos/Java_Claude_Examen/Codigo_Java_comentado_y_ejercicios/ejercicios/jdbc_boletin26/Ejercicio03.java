import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Boletín 26 - Ejercicio 3 (BD classicmodels)
 * Listar los productos con stock igual o inferior a un número, indicando el
 * stock exacto y en cuántos pedidos aparece cada producto (tabla orderdetails).
 */
public class Ejercicio03 {

	// Datos de conexión: cámbialos por los tuyos
	static final String URL = "jdbc:mysql://localhost:3306/classicmodels";
	static final String USR = "josemaria";
	static final String PSWD = "abc123";

	// LEFT JOIN para que también salgan los productos que no están en ningún pedido
	// (con COUNT(od.orderNumber) cuentan 0, porque COUNT ignora los NULL)
	// GROUP BY: una fila por producto, y COUNT cuenta las líneas de pedido de cada uno
	static final String SQL_STOCK = "SELECT p.productName, p.quantityInStock, COUNT(od.orderNumber) AS pedidos FROM products p LEFT JOIN orderdetails od ON p.productCode = od.productCode WHERE p.quantityInStock <= ? GROUP BY p.productCode, p.productName, p.quantityInStock ORDER BY p.quantityInStock";

	public static void main(String[] args) {
		productosConPocoStock(500); // cinco productos
		productosConPocoStock(15); // solo 1960 BSA Gold Star DBD34, en 28 pedidos
		productosConPocoStock(10); // ninguno
	}

	public static void productosConPocoStock(int limite) {
		try (Connection conexion = DriverManager.getConnection(URL, USR, PSWD)) {
			PreparedStatement sql = conexion.prepareStatement(SQL_STOCK);
			// Valor del ?: el límite de stock (un entero)
			sql.setInt(1, limite);
			ResultSet resultado = sql.executeQuery();
			// Contador para saber si hubo algún producto
			int encontrados = 0;
			System.out.println("Productos con stock igual o inferior a " + limite + ":");
			while (resultado.next()) {
				encontrados++;
				// "pedidos" es el alias que le dimos al COUNT en la consulta
				System.out.printf("- %s (stock: %d). Aparece en %d pedidos%n", resultado.getString("productName"),
						resultado.getInt("quantityInStock"), resultado.getInt("pedidos"));
			}
			// Si no ha entrado ni una vez en el bucle, informamos
			if (encontrados == 0)
				System.out.println("No hay ningún producto con stock igual o inferior a " + limite);
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
