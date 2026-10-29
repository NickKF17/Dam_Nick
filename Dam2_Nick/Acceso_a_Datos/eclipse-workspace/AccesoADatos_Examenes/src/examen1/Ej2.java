package examen1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import com.google.gson.Gson;







public class Ej2 {
public static void main(String[] args) throws FileNotFoundException, IOException {
	final String ruta = "marvel.json";
	leerGson(ruta);

	
	
	
        String admin = "profe";
        String password = "abc123";
        String server = "jdbc:mysql://localhost:3306/";
        
        try (Connection conexion = DriverManager.getConnection(server, admin, password)) {
        	
        	System.out.println("Conexion exitosa");
        	
        	
            Statement consulta = conexion.createStatement();
            
            consulta.executeUpdate("CREATE DATABASE IF NOT EXISTS Marvel");
            consulta.executeUpdate("USE Marvel");
            String queryTabla = "CREATE TABLE IF NOT EXISTS peliculas (" +
                                "titulo VARCHAR(150) Primary Key," +
                                "anyo INT NOT NUll, " +
                                "fase INT NOT NUll, " +
                                "director VARCHAR(100) Not Null)";
            
            consulta.executeUpdate(queryTabla);
        } catch (Exception e) {
            System.out.println("Error al preparar BBDD: " + e.getLocalizedMessage());
}
}
public static List<peliculas> cargarListaPeliculas(String ruta) {
	List<peliculas> peliculas = null;
	try (Reader lector = new FileReader(ruta)) {
		Gson gson = new Gson();
		ucm ucme = gson.fromJson(lector, ucm.class);
		peliculas = ucme.getPeliculas();
	} catch (Exception e) {
		System.out.println("Error: " + e.getMessage());
	}

	return peliculas;
}
public static void leerGson(String ruta) {
	List<peliculas> pelis = cargarListaPeliculas(ruta);
	if (pelis != null) {
		for (peliculas peli : pelis)
			System.out.println(peli);
	}
}
}

