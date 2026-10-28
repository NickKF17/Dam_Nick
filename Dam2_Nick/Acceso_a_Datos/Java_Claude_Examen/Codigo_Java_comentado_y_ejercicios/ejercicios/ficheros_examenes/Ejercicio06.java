import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeMap;

// Ejercicio 6: cortafuegos básico. Una IP con más de 2 apariciones queda bloqueada
public class Ejercicio06 {

	// Número máximo de accesos permitidos por IP
	static final int LIMITE = 2;

	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		System.out.print("Introduce el nombre del fichero de IPs: ");
		String nombre = teclado.nextLine();
		analizarIPs(nombre);
	}

	public static void analizarIPs(String nombre) {
		File fichero = new File(nombre);
		if (!fichero.exists()) {
			System.out.println("ADVERTENCIA: el fichero " + nombre + " no existe");
			return;
		}
		// TreeMap guarda las claves ordenadas, así la salida sale ordenada sin esfuerzo
		TreeMap<String, Integer> intentos = new TreeMap<>();
		try (BufferedReader lector = new BufferedReader(new FileReader(fichero))) {
			String linea;
			while ((linea = lector.readLine()) != null) {
				// Si ya estaba, sumamos 1; si no, empieza en 1
				if (intentos.containsKey(linea))
					intentos.put(linea, intentos.get(linea) + 1);
				else
					intentos.put(linea, 1);
			}
		} catch (Exception e) {
			System.out.println("ADVERTENCIA: no se puede leer el fichero: " + e.getMessage());
			return;
		}
		if (intentos.size() == 0) {
			System.out.println("ADVERTENCIA: el fichero no tiene contenido");
			return;
		}
		// Contamos cuántas hay de cada tipo para saber si hay que mostrar el bloque
		int permitidas = 0;
		int bloqueadas = 0;
		for (String ip : intentos.keySet())
			if (intentos.get(ip) > LIMITE)
				bloqueadas++;
			else
				permitidas++;
		if (permitidas > 0) {
			System.out.println("IP con acceso permitido:");
			for (String ip : intentos.keySet())
				if (intentos.get(ip) <= LIMITE)
					System.out.println("- " + ip);
		}
		if (bloqueadas > 0) {
			System.out.println("IPs con acceso restringido:");
			for (String ip : intentos.keySet())
				if (intentos.get(ip) > LIMITE)
					// Accesos superiores = total de apariciones - los permitidos
					System.out.println("- " + ip + ". Accesos superiores a lo permitido: " + (intentos.get(ip) - LIMITE));
		}
	}
}
