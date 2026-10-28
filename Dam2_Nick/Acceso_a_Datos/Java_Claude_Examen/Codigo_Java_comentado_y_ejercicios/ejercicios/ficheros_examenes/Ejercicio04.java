import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

// Ejercicio 4: reescribir el CSV sin la columna "Moneda" (tercera columna)
public class Ejercicio04 {

	public static void main(String[] args) {
		// Trabajamos sobre una copia (paises4.csv) para no estropear el fichero de datos original
		quitarMoneda("datos/paises4.csv");
	}

	public static void quitarMoneda(String fichero) {
		// Guardamos en memoria las líneas resultantes. IMPORTANTE: primero leer del todo y después escribir,
		// porque abrir un FileWriter vacía el fichero antes de que lo hayamos leído
		ArrayList<String> resultado = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			// La primera línea es la cabecera: se conserva (también sin moneda)
			String cabecera = lector.readLine();
			if (cabecera == null) {
				// Fichero vacío: no hay nada que hacer y no lo tocamos
				System.out.println("El fichero está vacío. No se modifica");
				return;
			}
			resultado.add(sinMoneda(cabecera));
			String linea;
			while ((linea = lector.readLine()) != null) {
				String[] campos = linea.split(",", -1);
				// Las líneas con estructura distinta de 4 campos se ignoran y no se vuelven a escribir
				if (campos.length == 4)
					resultado.add(sinMoneda(linea));
			}
		} catch (Exception e) {
			System.out.println("Error al leer el fichero: " + e.getMessage());
			return;
		}
		// Ahora sí: sobrescribimos el mismo fichero con las líneas guardadas
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter(fichero))) {
			for (String linea : resultado) {
				escritor.write(linea);
				// newLine escribe el salto de línea propio del sistema
				escritor.newLine();
			}
			System.out.println("Fichero " + fichero + " reescrito sin la moneda");
		} catch (Exception e) {
			System.out.println("Error al escribir el fichero: " + e.getMessage());
		}
	}

	// Recibe una línea de 4 campos y devuelve otra con los campos 0, 1 y 3
	// Si la cabecera no tuviera 4 campos la devolvemos sin tocar
	public static String sinMoneda(String linea) {
		String[] campos = linea.split(",", -1);
		String devolver = linea;
		if (campos.length == 4)
			devolver = campos[0] + "," + campos[1] + "," + campos[3];
		return devolver;
	}
}
