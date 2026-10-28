package primerTrimestre;

// Para la lista de teléfonos.
import java.util.List;

// Ahora telefonos es una lista. Los cambios son mínimos respecto a la anterior clase
// Hemos eliminado todos los métodos que no usamos porque la funcionalidad ahora es mas reducida
// Si quisiéramos un CRUD como en el anterior ejemplo tendríamos que añadirlos de nuevo adaptándonos
// a que ahora usamos una lista para almacenar los teléfonos
// Contacto con varios teléfonos.
public class ContactoDup {
	private String nombre;
	// Antes era un String; ahora una lista, y en el JSON un array ["...","..."].
	private List<String> telefonos;
	private String dni;

	// Constructor.
	public ContactoDup(String n, List<String> t, String d) {
		this.nombre = n;
		this.telefonos = t;
		this.dni = d;
	}
	
	// Getter.
	public String getNombre() {
		return this.nombre;
	}
	
	// Construye el texto del contacto.
	public String toString() {
		// Empezamos el texto con nombre y DNI.
		String contacto = "Nombre: " + this.nombre + "\nDNI: " + this.dni + "\nTeléfonos: \n" ;
		// Añade una línea por cada teléfono.
		for(String tlf: this.telefonos)
			// Concatena " - teléfono" y salto de línea.
			contacto+=" - " + tlf + "\n";
		// Devuelve el texto.
		return contacto;
}
}
