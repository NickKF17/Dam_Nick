package primerTrimestre;

// List: interfaz de listas. ArrayList es una de sus implementaciones.
import java.util.List;
// Anotación de Gson que enlaza un atributo con el nombre de una clave del JSON.
import com.google.gson.annotations.SerializedName;

/*
 * Necesitamos una clase que represente el raiz de nuestro JSON Con la anotación
 * SerializedName le decimos el elemento raíz que tiene que buscar en nuestro
 * JSON
 */
// Representa el objeto RAÍZ del JSON: { "agenda": [ ... ] }.
public class Agenda {
	// El atributo de abajo se llenará con la clave "agenda" del JSON, aunque el atributo se llame 'contactos'.
	@SerializedName("agenda")
	// El año pasado trabajamos mas con ArrayList que es mas completa que List
	// Gson trabaja con List, pero si nos sentimos mas cómodos podemos seguir usando
	// a nuestra amiga:
	
	// ArrayList<Contacto> contactos = (ArrayList)agenda.contactos;
	
	// Pero, como ves en la línea de arriba, nos obliga a usar casts
	// Y pra lo que vamos a hacer no vamos a encontrar diferencia
	// Lista de contactos: Gson crea los objetos Contacto uno a uno.
	private List<Contacto> contactos;
	
	// Getter.
	public List<Contacto> getContactos() {
		return contactos;
	}
	
	// Setter.
	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}
}
