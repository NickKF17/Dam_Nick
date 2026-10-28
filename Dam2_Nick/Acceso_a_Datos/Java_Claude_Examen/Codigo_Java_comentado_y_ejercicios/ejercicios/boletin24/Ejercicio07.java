import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Ejercicio07 {

	public static void main(String[] args) {
		String fichero = "datos/login.txt"; // El mismo fichero que usa el login del boletín 23
		Scanner teclado = new Scanner(System.in);
		System.out.print("Introduce el nombre del usuario: ");
		String usuario = teclado.nextLine();
		System.out.print("Introduce la contraseña: ");
		String password1 = teclado.nextLine();
		System.out.print("Vuelve a introducir la contraseña de nuevo: ");
		String password2 = teclado.nextLine();
		if (!password1.equals(password2))
			System.out.println("Las contraseñas no son iguales. No se puede grabar la nueva cuenta");
		else if (usuario.isEmpty() || password1.isEmpty() || usuario.contains(":") || password1.contains(":"))
			System.out.println("Usuario y contraseña no pueden estar vacíos ni contener el carácter ':'");
		else if (existeUsuario(fichero, usuario))
			System.out.println("El usuario " + usuario + " ya existe. No se puede grabar");
		else
			grabar(fichero, usuario, password1);
	}

	// Mira si el usuario ya está en el fichero (si el fichero no existe, no está)
	public static boolean existeUsuario(String fichero, String usuario) {
		boolean existe = false;
		try {
			for (String linea : Files.readAllLines(Path.of(fichero)))
				if (linea.substring(0, linea.indexOf(':')).equals(usuario))
					existe = true;
		} catch (Exception e) {
			existe = false; // Fichero inexistente: se creará al grabar
		}
		return existe;
	}

	public static void grabar(String fichero, String usuario, String password) {
		// El segundo parámetro true = añadir al final sin borrar lo que había
		try (FileWriter escritor = new FileWriter(fichero, true)) {
			escritor.write(usuario + ":" + password + "\n");
			System.out.println("Cuenta de usuario grabada correctamente");
		} catch (Exception e) {
			System.out.println("Error al grabar: " + e.getMessage());
		}
	}
}
