import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Ejercicio 5: recuento de votos guardado en un fichero binario (lista de objetos Partido)
public class Ejercicio05 {

	// Constante global: nombre del fichero (cambiarlo es cuestión de un único punto)
	static final String FICHERO = "datos/votos.dat";

	public static void main(String[] args) {
		anyadirVotos("PA", 134);
		anyadirVotos("PB", 98);
		anyadirVotos("PD", 177);
		anyadirVotos("PC", 220);
		anyadirVotos("PC", 236);
		anyadirVotos("PX", 10);
		verResultados(1730);
	}

	public static void anyadirVotos(String nombrePartido, int votos) {
		// Recuperamos la lista del fichero (o la creamos si es la primera vez)
		ArrayList<Partido> partidos = cargarPartidos();
		// Buscamos el partido por su nombre
		Partido encontrado = null;
		for (Partido p : partidos)
			if (p.getNombre().equals(nombrePartido))
				encontrado = p;
		if (encontrado == null) {
			System.out.println("El partido " + nombrePartido + " no concurre a las elecciones");
		} else {
			encontrado.sumarVotos(votos);
			guardarPartidos(partidos);
			System.out.println("Nuevos votos para el partido " + nombrePartido + ": " + votos + ".");
			System.out.println("Votos hasta el momento:");
			for (Partido p : partidos)
				System.out.println("Partido " + p.getNombre() + ": " + p.getVotos() + " votos");
		}
	}

	public static void verResultados(int censo) {
		ArrayList<Partido> partidos = cargarPartidos();
		// Total de votos escrutados
		int total = 0;
		for (Partido p : partidos)
			total += p.getVotos();
		// Casteamos a double para que la división no sea entera
		double porcentaje = (double) total / censo * 100;
		// %.0f muestra el número sin decimales
		System.out.println("Resultados con un " + String.format("%.0f", porcentaje) + "% de escrutinio:");
		ordenarPorVotos(partidos);
		for (Partido p : partidos)
			System.out.println("Partido " + p.getNombre() + ": " + p.getVotos() + " votos");
	}

	// Ordenación por selección de mayor a menor número de votos
	public static void ordenarPorVotos(ArrayList<Partido> partidos) {
		for (int i = 0; i < partidos.size() - 1; i++) {
			// Buscamos la posición del que más votos tiene desde i hasta el final
			int posMayor = i;
			for (int j = i + 1; j < partidos.size(); j++)
				if (partidos.get(j).getVotos() > partidos.get(posMayor).getVotos())
					posMayor = j;
			// Intercambiamos el elemento de la posición i con el mayor encontrado
			Partido auxiliar = partidos.get(i);
			partidos.set(i, partidos.get(posMayor));
			partidos.set(posMayor, auxiliar);
		}
	}

	// Lee la lista del fichero. Si no existe, devuelve una lista nueva con los 4 partidos a cero
	public static ArrayList<Partido> cargarPartidos() {
		ArrayList<Partido> partidos = new ArrayList<>();
		File fichero = new File(FICHERO);
		if (fichero.exists()) {
			try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(FICHERO))) {
				partidos = (ArrayList<Partido>) entrada.readObject();
			} catch (Exception e) {
				System.out.println("Error al leer el fichero de votos: " + e.getMessage());
			}
		} else {
			partidos.add(new Partido("PA", 0));
			partidos.add(new Partido("PB", 0));
			partidos.add(new Partido("PC", 0));
			partidos.add(new Partido("PD", 0));
		}
		return partidos;
	}

	// Graba la lista completa (sobrescribe el fichero; crea si no existe)
	public static void guardarPartidos(ArrayList<Partido> partidos) {
		try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(FICHERO))) {
			salida.writeObject(partidos);
		} catch (Exception e) {
			System.out.println("Error al guardar el fichero de votos: " + e.getMessage());
		}
	}
}
