package primerTrimestre;

// Lectura de fichero.
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

// Gson.
import com.google.gson.Gson;

// Lee un JSON donde cada contacto tiene una LISTA de teléfonos.
public class AgendaJSONConDup {
	
	/*
	 * Aunque el estándar JSON permite etiquetas duplicadas, el comportamiento al leerlo no está definido y depende del lenguaje de programación que uses:
	 * Puede que se quede con el primer valor, puede que con el último (Jackson) o que genere una excepción (Gson)
	 * La forma mas cómoda de permitir duplicados es usar listas para estos campos. Ver agenda2.json
	 */
	
	// Si te fijas, es el mismo código que en la versión sin duplicados. La diferencia se trata en la clase ContactoDup
	// Todo el proceso en main.
	public static void main(String[] args) {
		// JSON con listas.
		String rutaArchivo = "agenda2.json";
		// Lectura con try-with-resources.
		try (Reader lector = new FileReader(rutaArchivo)) {
			// Gson por defecto.
			Gson gson = new Gson();
			// Convierte el JSON en objeto AgendaDup; Gson sabe rellenar la List<String> con el array.
			AgendaDup agenda = gson.fromJson(lector, AgendaDup.class);
			// Lista de contactos.
			List<ContactoDup> contactos = agenda.getContactos();
			// JSON vacío o mal formado.
			if(contactos == null)
				System.out.println("Error al leer los contactos de la agenda");
			else {
				// Cabecera.
				System.out.println("AGENDA");
				System.out.println("Total de contactos: " + contactos.size());
				// Imprime cada contacto con su toString.
				for (ContactoDup c : contactos)
					System.out.println(c);
			} 
		// Captura errores de fichero o de formato JSON.
		} catch (Exception e) {
			System.out.println("Error al leer el archivo: " + e.getMessage());
		}
	}
}
