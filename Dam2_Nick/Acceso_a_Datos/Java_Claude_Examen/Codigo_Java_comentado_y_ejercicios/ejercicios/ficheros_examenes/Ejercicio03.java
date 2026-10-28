import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

// Ejercicio 3: leer paises.csv y mostrar cuatro listas con el formato "A, B y C"
public class Ejercicio03 {

	public static void main(String[] args) {
		mostrarPaises("datos/paises.csv");
	}

	public static void mostrarPaises(String fichero) {
		// Una lista por cada columna que vamos a mostrar
		ArrayList<String> nombres = new ArrayList<>();
		ArrayList<String> capitales = new ArrayList<>();
		ArrayList<String> monedas = new ArrayList<>();
		ArrayList<String> animales = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			// Leemos la primera línea y la descartamos: es la cabecera (si el fichero está vacío será null)
			String linea = lector.readLine();
			while ((linea = lector.readLine()) != null) {
				// split con -1 mantiene los campos vacíos del final, así "a,b,c," cuenta como 4 campos
				String[] campos = linea.split(",", -1);
				// Solo procesamos las líneas con exactamente 4 datos; el resto se ignoran
				if (campos.length == 4) {
					nombres.add(campos[0]);
					capitales.add(campos[1]);
					monedas.add(campos[2]);
					animales.add(campos[3]);
				}
			}
		} catch (Exception e) {
			System.out.println("Error al leer el fichero: " + e.getMessage());
			return;
		}
		if (nombres.size() == 0) {
			System.out.println("No hay datos de ningún país en el fichero");
		} else {
			System.out.println("Países en el fichero: " + nombres.size());
			System.out.println("Nombres: " + unirConY(nombres));
			System.out.println("Las capitales de los mismos son: " + unirConY(capitales));
			System.out.println("Sus monedas oficiales son: " + unirConY(monedas));
			System.out.println("Sus animales mas representativos son " + unirConY(animales));
		}
	}

	// Une los elementos con ", " y pone " y " antes del último. Con un solo elemento lo devuelve tal cual
	public static String unirConY(ArrayList<String> lista) {
		String resultado = "";
		for (int i = 0; i < lista.size(); i++) {
			if (i == 0)
				// El primero va sin separador
				resultado = lista.get(i);
			else if (i == lista.size() - 1)
				// El último va precedido de " y "
				resultado = resultado + " y " + lista.get(i);
			else
				// Los intermedios, precedidos de coma y espacio
				resultado = resultado + ", " + lista.get(i);
		}
		return resultado;
	}
}
