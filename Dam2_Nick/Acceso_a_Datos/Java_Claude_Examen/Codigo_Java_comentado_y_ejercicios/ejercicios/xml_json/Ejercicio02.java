import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// Ejercicio 2: pasar un fichero de texto (3 líneas por artículo) a JSON
public class Ejercicio02 {

	public static void main(String[] args) {
		stockAJson("datos/stock.txt", "datos/stock.json");
	}

	public static void stockAJson(String ficheroTexto, String ficheroJSON) {
		List<Articulo> articulos = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(ficheroTexto))) {
			String referencia;
			// Leemos de 3 en 3 líneas: si la referencia es null hemos llegado al final
			while ((referencia = lector.readLine()) != null) {
				String descripcion = lector.readLine();
				// El stock llega como texto: lo convertimos a entero
				int stock = Integer.parseInt(lector.readLine());
				articulos.add(new Articulo(referencia, descripcion, stock));
			}
		} catch (Exception e) {
			System.out.println("Error al leer el fichero de texto: " + e.getMessage());
			return;
		}
		// Metemos la lista en la clase raíz y la grabamos igual que hacíamos con la agenda
		Ferreteria ferreteria = new Ferreteria();
		ferreteria.setArticulos(articulos);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (Writer escritor = new FileWriter(ficheroJSON)) {
			gson.toJson(ferreteria, escritor);
			System.out.println("JSON creado con " + articulos.size() + " artículos");
		} catch (Exception e) {
			System.out.println("Error al guardar el JSON: " + e.getMessage());
		}
	}
}
