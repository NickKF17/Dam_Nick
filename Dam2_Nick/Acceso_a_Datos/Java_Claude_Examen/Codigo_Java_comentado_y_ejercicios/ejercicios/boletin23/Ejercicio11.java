import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Ejercicio11 {

	public static void main(String[] args) {
		String fichero = "datos/login.txt"; // Ubicación del fichero de usuarios
		if (args.length > 0)                // Para probar otros ficheros sin tocar el código
			fichero = args[0];
		Scanner teclado = new Scanner(System.in);
		System.out.print("Usuario: ");
		String usuario = teclado.nextLine();
		System.out.print("Contraseña: ");
		String password = teclado.nextLine();
		System.out.println(login(fichero, usuario, password));
	}

	// Devuelve un mensaje explicando el resultado del login
	public static String login(String fichero, String usuario, String password) {
		String resultado;
		try {
			ArrayList<String> lineas = new ArrayList<>(Files.readAllLines(Path.of(fichero)));
			if (lineas.size() == 0) {
				resultado = "Error: el fichero de usuarios está vacío";
			} else {
				boolean usuarioEncontrado = false;
				boolean passwordCorrecta = false;
				for (int i = 0; i < lineas.size() && !usuarioEncontrado; i++) {
					String linea = lineas.get(i);
					int posicion = linea.indexOf(':');                // El separador
					String usuarioFichero = linea.substring(0, posicion);
					String passwordFichero = linea.substring(posicion + 1);
					if (usuarioFichero.equals(usuario)) {             // Usuarios no repetidos: paramos al encontrarlo
						usuarioEncontrado = true;
						passwordCorrecta = passwordFichero.equals(password);
					}
				}
				if (!usuarioEncontrado)
					resultado = "Error: usuario no encontrado";
				else if (!passwordCorrecta)
					resultado = "Error: contraseña incorrecta";
				else
					resultado = "Login correcto. ¡Bienvenido " + usuario + "!";
			}
		} catch (Exception e) {
			resultado = "Error: fichero inexistente o imposible acceder a él (" + e.getMessage() + ")";
		}
		return resultado;
	}
}
