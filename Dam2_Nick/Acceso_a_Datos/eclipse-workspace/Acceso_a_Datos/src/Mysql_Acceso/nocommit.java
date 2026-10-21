package Mysql_Acceso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class nocommit {
	public static void main(String[] args) {
		

	
	try {
		Connection conn =PoolConexiones.getConnection();
		System.out.println("Conexion exitosa");
		conn.setAutoCommit(false); //Con esta linea desactivamos los commits automaticos
		Statement sql =conn.createStatement();
		sql.executeUpdate("Delete From alumnos where email='algoxxx@email.com'"); //Borramos un alumno por su correo
		ResultSet resultado=sql.executeQuery("Select * From alumnos"); //Listamos los alumnos ahora que lo hemos borrado no aparecera, si hacemos conn.commit() se borrara, si no lo hacemos no.
		while(resultado.next()) {
			System.out.println(resultado.getString("email"));
		}
		conn.commit();//Con esta linea guardamos los cambios que hemos hecho..
}catch(SQLException e){
	e.printStackTrace();
}
}}