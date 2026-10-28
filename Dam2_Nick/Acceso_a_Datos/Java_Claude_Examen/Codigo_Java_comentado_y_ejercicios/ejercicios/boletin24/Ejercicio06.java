import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class Ejercicio06 {

	public static void main(String[] args) {
		String ficheroSoluciones = "datos/soluciones.txt";
		String ficheroRespuestas = "datos/respuestas.txt";
		String ficheroNotas = "datos/notas.txt";
		if (args.length > 0)                   // Para probar con otro fichero de soluciones
			ficheroSoluciones = args[0];
		corregir(ficheroSoluciones, ficheroRespuestas, ficheroNotas);
	}

	// Una lista de respuestas válida: letras A-D separadas por ", "
	public static boolean listaValida(String lista) {
		return lista.matches("[ABCD](, [ABCD])*");
	}

	// Lee todas las líneas de un fichero en una lista
	public static ArrayList<String> leerLineas(String fichero) throws Exception {
		ArrayList<String> lineas = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null)
				lineas.add(linea);
		}
		return lineas;
	}

	public static void corregir(String ficheroSoluciones, String ficheroRespuestas, String ficheroNotas) {
		try {
			ArrayList<String> lineasSoluciones = leerLineas(ficheroSoluciones);
			// Fallo en soluciones: informamos y no hacemos nada más
			if (lineasSoluciones.size() != 1 || !listaValida(lineasSoluciones.get(0))) {
				System.out.println("Error: el fichero de soluciones no tiene un formato correcto");
				return;
			}
			String[] soluciones = lineasSoluciones.get(0).split(", ");
			ArrayList<String> lineasRespuestas = leerLineas(ficheroRespuestas);
			try (PrintWriter escritor = new PrintWriter(new FileWriter(ficheroNotas))) {
				for (int i = 0; i < lineasRespuestas.size(); i++) {
					String linea = lineasRespuestas.get(i);
					int posicion = linea.indexOf(": ");
					boolean correcta = false;
					if (posicion > 0 && listaValida(linea.substring(posicion + 2))) {
						String[] respuestas = linea.substring(posicion + 2).split(", ");
						correcta = respuestas.length == soluciones.length; // Mismo número de preguntas
						if (correcta) {
							String nombre = linea.substring(0, posicion);
							escritor.println(nombre + ": " + calcularNota(soluciones, respuestas));
						}
					}
					if (!correcta)
						System.out.println("Línea " + (i + 1) + " de respuestas incorrecta, se ignora");
				}
			}
			System.out.println("Notas escritas en " + ficheroNotas);
		} catch (Exception e) {
			System.out.println("Error con los ficheros: " + e.getMessage());
		}
	}

	// +1 por acierto, -0,3 por fallo
	public static double calcularNota(String[] soluciones, String[] respuestas) {
		double nota = 0;
		for (int i = 0; i < soluciones.length; i++) {
			if (soluciones[i].equals(respuestas[i]))
				nota += 1;
			else
				nota -= 0.3;
		}
		return Math.round(nota * 10) / 10.0; // Un decimal
	}
}
