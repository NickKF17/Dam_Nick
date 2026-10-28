import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.util.ArrayList;

public class Ejercicio04 {

	public static void main(String[] args) {
		Darlavuelta("datos/fichero1.txt", "datos/fichero2.txt");
	}

	// Invierte el orden de las líneas y el contenido de cada una
	public static void Darlavuelta(String origen, String destino) {
		ArrayList<String> lineas = new ArrayList<>();
		// 1) Leemos todas las líneas en una lista
		try (BufferedReader lector = new BufferedReader(new FileReader(origen))) {
			String linea;
			while ((linea = lector.readLine()) != null)
				lineas.add(linea);
		} catch (Exception e) {
			System.out.println("Error al leer: " + e.getMessage());
			return; // Sin lectura no seguimos
		}
		// 2) Escribimos recorriendo la lista desde el final hasta el principio
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter(destino))) {
			for (int i = lineas.size() - 1; i >= 0; i--) {
				escritor.write(invertir(lineas.get(i)));
				escritor.newLine();
			}
			System.out.println("Fichero " + destino + " escrito");
		} catch (Exception e) {
			System.out.println("Error al escribir: " + e.getMessage());
		}
	}

	// Da la vuelta a un texto recorriéndolo de atrás hacia delante
	public static String invertir(String texto) {
		String resultado = "";
		for (int i = texto.length() - 1; i >= 0; i--)
			resultado += texto.charAt(i);
		return resultado;
	}
}
