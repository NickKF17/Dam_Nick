// Paquete.
package primerTrimestre;
/*
 * Necesitamos una segunda clase que represente los hijos del JSON Los
 * nombres de los elementos del JSON deben de coincidir con los atributos de la
 * clase de esta forma Gson los asigna de forma automática
 */
// Clase POJO (solo atributos y métodos simples) que representa un contacto. Gson la rellena automáticamente.
public class Contacto {
	// Los atributos deben llamarse igual que las claves del JSON para que Gson los asocie solo.
	private String nombre;
	private String telefono;
	private String dni;

	// Constructor con todos los datos.
	public Contacto(String n, String t, String d) {
		// Guardamos los parámetros en los atributos.
		this.nombre = n;
		this.telefono = t;
		this.dni = d;
	}
	
	// Getter: acceso de lectura al nombre (el atributo es private).
	public String getNombre() {
		return this.nombre;
	}
	
	// Setter: permite cambiar el teléfono.
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	// Cómo se imprime un contacto con println.
	public String toString() {
		return "Nombre: " + this.nombre + "\nDNI: " + this.dni + "\nTeléfono: " + this.telefono + "\n"; 
	}
}
