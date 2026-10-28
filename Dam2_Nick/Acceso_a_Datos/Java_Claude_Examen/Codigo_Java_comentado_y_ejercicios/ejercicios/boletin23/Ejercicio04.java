import java.nio.file.Files;
import java.nio.file.Path;

public class Ejercicio04 {

	public static void main(String[] args) {
		probar("datos/igual1.txt", "datos/igual2.txt");
		probar("datos/igual1.txt", "datos/distinto.txt");
		probar("datos/igual1.txt", "datos/noexiste.txt");
	}

	// Pequeña función para no repetir el if/else en el main
	public static void probar(String f1, String f2) {
		if (compararFicheros(f1, f2))
			System.out.println("El contenido de los ficheros es el mismo");
		else
			System.out.println("El contenido de los ficheros no es el mismo");
	}

	// Devuelve true si los dos ficheros tienen exactamente el mismo contenido
	public static boolean compararFicheros(String nombre1, String nombre2) {
		boolean iguales = false;
		try {
			// Leemos cada fichero entero en un String (incluye los saltos de línea)
			String contenido1 = Files.readString(Path.of(nombre1));
			String contenido2 = Files.readString(Path.of(nombre2));
			// equals compara el contenido de los Strings (nunca usar == para esto)
			iguales = contenido1.equals(contenido2);
		} catch (Exception e) {
			// Si uno no existe no podemos decir que sean iguales: devolvemos false
			System.out.println("Error al leer: " + e.getMessage());
		}
		return iguales;
	}
}
