package tercerTrimestre;

// Clases de ESCRITURA: FileWriter (básica), BufferedWriter (con buffer), PrintWriter (con formato).
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
// StandardCharsets.UTF_8: para indicar la codificación y no tener problemas con tildes y ñ.
import java.nio.charset.StandardCharsets;
// Files, Path/Paths y StandardOpenOption: escritura moderna (NIO) de listas y Strings.
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
// NOTA del mentor: este import no se usa en el código (Eclipse lo marcaría como 'unused').
import java.util.Arrays;
import java.util.List;

// Seis formas de ESCRIBIR ficheros de texto.
public class Ficheros2 {

	// Se ejecutan todas seguidas sobre el mismo fichero: cada una sobrescribe lo anterior.
	public static void main(String[] args) {
		escritura1();
		escritura1bis();
		escritura2();
		escritura3();
		escritura4();
		escritura5();
	}

	// Escritura 1: FileWriter con cierre manual.
	public static void escritura1() {
		try {
			// si el fichero no existe se crea. Si existe se borra su contenido antes de
			// escribir
			// Sin segundo parámetro: crea el fichero o lo VACÍA si existe.
			FileWriter escritor = new FileWriter("/mnt/temp/java.txt");
			// write escribe el texto tal cual; el \n lo ponemos nosotros.
			escritor.write("Hola, mundo con FileWriter!\n");
			// Segunda línea, sin salto al final.
			escritor.write("Escribiendo una segunda línea.");
			System.out.println("Archivo escrito correctamente.");
			// close() vacía el buffer interno y libera el fichero: es lo que garantiza que los datos llegan al disco.
			escritor.close();
		} catch (Exception e) {
			System.err.println("Error al escribir el archivo: " + e.getMessage());
		}
		try {
			// el segundo parámetro, true, indica que se añade al final (append) si el
			// fichero ya existe
			// si el fichero no existe se crea
			// El true activa el modo APPEND: añade al final sin borrar.
			FileWriter escritor = new FileWriter("/mnt/temp/java.txt", true);
			// NOTA del mentor: aquí NO se cierra el escritor. Puede que el texto no llegue a escribirse y el fichero queda abierto. Es el error típico que resuelve try-with-resources (ver escritura1bis).
			escritor.write("\nEsta línea se añade al final.");
		} catch (Exception e) {
			System.err.println("Error al añadir al archivo: " + e.getMessage());
		}
	}

	// Escritura 1bis: lo mismo con try-with-resources (se cierra solo).
	public static void escritura1bis() {
		// Idem al anterior pero usamos try-with-resources para que el fichero se cierre
		// automáticamente
		// Modo sobrescribir.
		try (FileWriter escritor = new FileWriter("/mnt/temp/java.txt")) {
			escritor.write("Hola, mundo con FileWriter!\n");
			escritor.write("Escribiendo una segunda línea.");
			// Podríamos escribir caracteres sueltos o arrays de char.
			System.out.println("Archivo escrito correctamente.");
		} catch (Exception e) {
			System.err.println("Error al escribir el archivo: " + e.getMessage());
		}
		// Idem para añadir
		// Modo añadir.
		try (FileWriter escritor = new FileWriter("/mnt/temp/java.txt", true)) {
			escritor.write("\nEsta línea se añade al final.");
		} catch (Exception e) {
			System.err.println("Error al añadir al archivo: " + e.getMessage());
		}
	}

	// Escritura 2: BufferedWriter. Acumula en memoria y escribe al disco en bloques: más rápido con muchos datos.
	public static void escritura2() {
		// BufferedWriter es mas eficiente. sobre todo para grandes datos
		// Usa un buffer intermedio. Por lo mismo es mas crítico no olvidar cerrar el fichero
		// Podemos usar la estrategia de try-with-resource para no olvidarlo
		// BufferedWriter envuelve a un FileWriter.
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter("/mnt/temp/java.txt"))) {
			// BufferedWriter escritor = new BufferedWriter(new
			// FileWriter("/mnt/temp/java.txt");
			// Escribe la primera línea.
			escritor.write("Primera línea con Buffer.");
			// Escribe el salto de línea. Es mas portable porque si en el sistema en el que
			// se ejecuta se usa otro caracter diferente al \n lo tiene en cuenta
			// newLine() escribe el salto de línea propio del sistema operativo (\n en Linux, \r\n en Windows).
			escritor.newLine();
			// Segunda línea.
			escritor.write("Segunda línea. Es más eficiente.");
			escritor.newLine();
			// escritor.close();
			System.out.println("Archivo escrito eficientemente.");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		// y también podemos añadir
		// Para añadir, el true va en el FileWriter interior.
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter("/mnt/temp/java.txt", true))) {
			escritor.write("Última línea.");
			System.out.println("Añadido.");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	// Escritura 3: PrintWriter. Tiene println, print y printf, igual que System.out.
	public static void escritura3() {
		// PrintWriter nos permite grabar "con formato" de la misma forma que haríamos con printf
		// Podemos especificar el juego de caracteres que vamos a usar (opcional)
		// El segundo argumento fija la codificación UTF-8.
		try (PrintWriter escritor = new PrintWriter("/mnt/temp/java.txt", StandardCharsets.UTF_8)) {
			// println escribe y salta de línea.
			escritor.println("Línea 1: Hola con PrintWriter.");
			// print escribe sin salto de línea.
			escritor.print("Línea 2: Esto no tiene salto de línea. ");
			escritor.println("Pero esto sí lo añade.");
			// Formateo de datos
			// Variables para el ejemplo de formato.
			String nombre = "Ana";
			int edad = 30;
			double altura = 1.7589;
			// printf con formato: %s texto, %d entero, %.2f decimal con 2 decimales.
			escritor.printf("Usuario: %s, Edad: %d, Altura: %.2f m", nombre, edad, altura);
			// println() vacío: solo salto de línea.
			escritor.println(); // Salto de línea adicional

			System.out.println("Archivo escrito con formato correctamente.");

			// Podemos comprobar si hubo errores que no generen excepción así:
			// PrintWriter NO lanza excepciones al escribir: guarda el error y hay que consultarlo con checkError().
			if (escritor.checkError()) {
				System.err.println("Ocurrió un error durante la escritura.");
			}

		} catch (Exception e) {
			System.err.println("Error al abrir/crear el archivo: " + e.getMessage());
		}
		// Para añadir con PrintWriter usamos también PrintWriter
		// Para AÑADIR con PrintWriter se le pasa un FileWriter en modo append (3er parámetro true).
		try (PrintWriter escritor = new PrintWriter(
                new FileWriter("/mnt/temp/java.txt", StandardCharsets.UTF_8, true))) {
            
            escritor.println("Esta línea se añade al final.");
            // %n = salto de línea portable dentro de printf.
            escritor.printf("Número de línea: %d%n", 42);
            
            System.out.println("Texto añadido correctamente con PrintWriter");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}

	// Escritura 4: guardar una lista entera con Files.write.
	public static void escritura4() {
		// Ahora vamos a ver un método para grabar listas
		// Paths.get(...) es la forma clásica de crear un Path (equivale a Path.of).
		Path rutaArchivo = Paths.get("/mnt/temp/java.txt");
		// Lista con tres líneas. List.of crea una lista inmutable que copiamos en un ArrayList.
		ArrayList<String> lineas = new ArrayList<>(List.of("Primera Linea", "Segunda línea", "Tercera línea"));
		try {
			// Abre el fichero, escribe la lista (una línea por elemento) y luego cierra el fichero
			// Escribe cada elemento como una línea, abre y cierra el fichero él solo. Sobrescribe por defecto.
			Files.write(rutaArchivo, lineas, StandardCharsets.UTF_8);
			System.out.println("Lista escrita en archivo.");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		
		// y para añadir
		// Líneas a añadir.
		ArrayList<String> nuevasLineas = new ArrayList<>(List.of("Cuarta línea", "Quinta línea"));
		try {
            // Con opciones: CREATE (crear si no existe) + APPEND (añadir al final).
            Files.write(
            	rutaArchivo, 
                nuevasLineas, 
                StandardCharsets.UTF_8,
                // StandardOpenOption.CREATE crea el fichero si no existe. Si no lo ponemos generaría una excepción si el fichero no existe, pero si existe añade
                // Crea el fichero si no existe.
                StandardOpenOption.CREATE,   
                // Añade sin borrar. Sin CREATE daría error si el fichero no existiera.
                StandardOpenOption.APPEND
            );
            
            System.out.println("Líneas añadidas correctamente");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}

	// Escritura 5: guardar un String con Files.writeString.
	public static void escritura5() {
		// Similar al método anterior, pero para escribir Strings
		Path rutaArchivo = Paths.get("/mnt/temp/java.txt");
		// Texto con salto de línea \n incluido.
		String contenido = "Este es el contenido a escribir.\nSegunda línea.";
		try {
			// Abre el fichero, graba el string y lo cierra luego
			// Escribe el String completo y cierra.
			Files.writeString(rutaArchivo, contenido, StandardCharsets.UTF_8);
			System.out.println("Archivo escrito con Files.writeString");

			// Añadir más contenido (append). Funciona también igual que en el caso de las listas
			// Texto a añadir.
			String masContenido = "\nAñadiendo más texto.";
			// Mismas opciones CREATE + APPEND que con las listas.
			Files.writeString(rutaArchivo, masContenido, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			System.out.println("Texto añadido.");

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}
