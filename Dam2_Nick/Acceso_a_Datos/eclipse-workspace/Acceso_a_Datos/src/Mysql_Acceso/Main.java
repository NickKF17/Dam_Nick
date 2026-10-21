package Mysql_Acceso;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	public static void main(String[] args) {
		
		try {
			Connection conn =PoolConexiones.getConnection();
			System.out.println("Conexion exitosa");
			Statement sql =conn.createStatement();
			ResultSet resultado =sql.executeQuery("Select * from alumnos");
			System.out.println("--------Lista de Alumnos--------");
			while(resultado.next()) {
				System.out.printf("Nombre: %s %s Edad: %d Email: %s\n",resultado.getString(1),resultado.getString(2),resultado.getInt(3),resultado.getString(4));
			}
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}