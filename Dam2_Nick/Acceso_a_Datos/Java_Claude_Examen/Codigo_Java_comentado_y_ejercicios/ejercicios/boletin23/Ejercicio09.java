import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Ejercicio09 {

	public static void main(String[] args) {
		procesarNotas("datos/Redes.txt");
	}

	// Une los elementos de una lista separados por coma y espacio
	public static String unir(ArrayList<String> lista) {
		String texto = "";
		for (int i = 0; i < lista.size(); i++) {
			if (i > 0)
				texto += ", ";
			texto += lista.get(i);
		}
		return texto;
	}

	public static void procesarNotas(String fichero) {
		// Dos listas paralelas: la posición i de cada una corresponde al mismo alumno
		ArrayList<String> nombres = new ArrayList<>();
		ArrayList<double[]> notas = new ArrayList<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				int posicion = linea.indexOf(':');               // Dónde están los dos puntos
				String nombre = linea.substring(0, posicion);     // Todo lo anterior es el nombre
				String[] trozos = linea.substring(posicion + 1).split(","); // Lo posterior son las notas
				double[] valores = new double[trozos.length];
				for (int i = 0; i < trozos.length; i++)
					valores[i] = Double.parseDouble(trozos[i].trim()); // trim quita el espacio tras la coma
				nombres.add(nombre.trim());
				notas.add(valores);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return; // Sin datos no hay nada que mostrar
		}
		if (nombres.size() == 0) {
			System.out.println("El fichero no tiene alumnos");
			return;
		}
		// El nombre del módulo es el nombre del fichero sin la ruta ni el .txt
		String modulo = new File(fichero).getName().replace(".txt", "");
		System.out.println("Módulo: " + modulo);

		// 1) Alumnos con todo aprobado
		System.out.println("Alumnos/as con todo aprobado:");
		for (int i = 0; i < nombres.size(); i++) {
			boolean todoAprobado = true;
			for (int j = 0; j < notas.get(i).length; j++)
				if (notas.get(i)[j] < 5)
					todoAprobado = false;
			if (todoAprobado)
				System.out.println(nombres.get(i));
		}

		// 2) Suspensos por resultado de aprendizaje
		System.out.println();
		System.out.println("Resultados de aprendizaje y alumnos suspensos:");
		int numeroRA = notas.get(0).length; // Cuántas notas tiene el primer alumno
		for (int ra = 0; ra < numeroRA; ra++) {
			ArrayList<String> suspensos = new ArrayList<>();
			for (int i = 0; i < nombres.size(); i++)
				if (notas.get(i)[ra] < 5)
					suspensos.add(nombres.get(i));
			if (suspensos.size() == 0)
				System.out.println("RA" + (ra + 1) + ": Todos aprobados");
			else
				System.out.println("RA" + (ra + 1) + ": " + unir(suspensos));
		}
	}
}
