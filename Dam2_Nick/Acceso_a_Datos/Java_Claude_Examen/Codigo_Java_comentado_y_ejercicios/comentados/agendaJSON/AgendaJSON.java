package primerTrimestre;

// Gson: librería de Google para convertir JSON <-> objetos Java.
import com.google.gson.Gson;
// GsonBuilder: permite configurar Gson (por ejemplo JSON bonito).
import com.google.gson.GsonBuilder;
// NOTA del mentor: import sin usar en esta clase.
import com.google.gson.annotations.SerializedName;

// FileReader/FileWriter para leer y escribir el fichero; Reader/Writer son sus tipos genéricos.
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

// CRUD de una agenda guardada en JSON: Leer, Crear, Modificar, Borrar.
public class AgendaJSON {
	// Ejecuta una secuencia de operaciones de prueba.
	public static void main(String[] args) {
		// Nombre del fichero JSON.
		String rutaArchivo = "agenda.json";
		// Muestra todos los contactos.
		leerAgenda(rutaArchivo);
		// Creamos un contacto nuevo...
		Contacto contacto = new Contacto("Maripili", "999888777", "11223344G");
		// ...y lo añadimos al JSON.
		crearContacto(rutaArchivo, contacto);
		// Lo borramos (queda el fichero como al principio).
		borrarContacto(rutaArchivo, "Maripili");
		// Cambiamos el teléfono de Leonor.
		// NOTA del mentor: si 'Leonor' no existe, se informa de que no existe.
		modificarTelefono(rutaArchivo, "Leonor", "888777666");
	}

	// Método reutilizable: lee el fichero y devuelve la lista de contactos (o null si hay error).
	public static List<Contacto> cargarAgenda(String rutaArchivo) {
		// null = error o sin datos.
		List<Contacto> contactos = null;
		// try-with-resources para cerrar el fichero.
		try (Reader lector = new FileReader(rutaArchivo)) {
			// Objeto Gson con la configuración por defecto.
			Gson gson = new Gson();
			// Gson convierte el JSON directamente en un objeto Agenda
			// fromJson: convierte el texto JSON en un objeto Agenda automáticamente (deserializar).
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			// Y de esta forma obtenemos una lista de contactos
			// Sacamos la lista del objeto raíz.
			contactos = agenda.getContactos();
		} catch (Exception e) {
			// Mensaje de error si falla la lectura.
			System.err.println("Error al leer el archivo: " + e.getMessage());
		}
		// Doble seguridad: si el JSON estaba vacío, contactos sigue siendo null.
		if(contactos == null)
			System.out.println("Error al leer los contactos de la agenda");
		// Devolvemos la lista.
		return contactos;
	}

	// Escribe la lista completa en el fichero JSON (serializar).
	private static void guardarAgenda(String rutaArchivo, List<Contacto> contactos) {
		// Montamos el objeto raíz.
		Agenda agenda = new Agenda();
		// Le asignamos la lista.
		agenda.setContactos(contactos);
		//Gson gson = new Gson();
		// Mejor así para que el JSON se guarde bien indentado y legible
		// setPrettyPrinting() genera JSON con sangrías, legible.
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// FileWriter sin append: reescribe el fichero entero.
		try (Writer escritor = new FileWriter(rutaArchivo)) {
			// graba físicamente el json en el archivo físico
			// toJson: convierte el objeto en JSON y lo escribe en el fichero.
			gson.toJson(agenda, escritor);
		} catch (Exception e) {
			System.err.println("Error al guardar el archivo: " + e.getMessage());
		}
	}
	
	// Busca por nombre y devuelve el contacto o null.
	public static Contacto buscarContacto(List<Contacto> contactos, String nombreBuscado) {
        // Resultado por defecto.
        Contacto contacto = null;
        // Recorre todos.
        for (Contacto c : contactos)
            // equalsIgnoreCase compara ignorando mayúsculas/minúsculas.
            // NOTA del mentor: no corta al encontrar; devuelve el último que coincide. Si no hay duplicados da igual.
            if (c.getNombre().equalsIgnoreCase(nombreBuscado))
                contacto = c;
        // si el contacto no existe va a devolver un null
        // Devuelve null si no existe.
        return contacto;
    }

	// READ: muestra la agenda.
	public static void leerAgenda(String rutaArchivo) {
		// Reutiliza cargarAgenda.
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		// Solo mostramos si la carga fue correcta.
		if (contactos != null) {
			System.out.println("AGENDA");
			// el método size me da el número de contactos de la lista
			// size() = nº de elementos de la lista.
			System.out.println("Total de contactos: " + contactos.size());
			// println(c) llama a toString().
			for (Contacto c : contactos)
				System.out.println(c);
		} 
	}

	// CREATE: añade si no existe.
	public static void crearContacto(String rutaArchivo, Contacto contacto) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		if (contactos != null)
			// Evita duplicados: solo añade si no existe un contacto con ese nombre.
			if(buscarContacto(contactos,contacto.getNombre()) == null){
				// Añade a la lista en memoria.
				contactos.add(contacto);
				// Persistimos la lista modificada en el fichero.
				guardarAgenda(rutaArchivo, contactos);
				System.out.println("Contacto creado");
			}
			else
				System.out.println("Ya existe un contacto con ese nombre");
	}
	
	// UPDATE: cambia el teléfono.
	public static void modificarTelefono(String rutaArchivo, String nombreBuscado, String nuevoTelefono) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		// Localiza al contacto.
		// NOTA del mentor: aquí no se comprueba contactos != null; si el fichero falla, buscarContacto lanzaría NullPointerException.
		Contacto encontrado = buscarContacto(contactos,nombreBuscado);
        if (encontrado!=null){
        	// Cambia el dato en el objeto (que está dentro de la lista).
        	encontrado.setTelefono(nuevoTelefono);
            guardarAgenda(rutaArchivo, contactos);
            System.out.println("Teléfono actualizado");
        }
        else
        	System.out.println("No existe ese contacto");
    }
	
	// DELETE: elimina un contacto.
	public static void borrarContacto(String rutaArchivo, String nombreBuscado) {
		List<Contacto> contactos = cargarAgenda(rutaArchivo);
		if (contactos != null) {
			Contacto encontrado = buscarContacto(contactos,nombreBuscado);
			if(encontrado != null){
				// remove(objeto) lo quita de la lista.
				contactos.remove(encontrado);
				guardarAgenda(rutaArchivo, contactos);
				System.out.println("Contacto eliminado");
			}
			else
				System.out.println("No existe ese contacto");
		}
	}
}
