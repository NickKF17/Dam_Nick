import java.io.Serializable;

// Clase para el ejercicio 2. Implementa Serializable para poder grabar objetos en un fichero binario
public class Personaje implements Serializable {
	// Atributos privados: título del anime y nombre del personaje
	private String anime;
	private String nombre;

	// Constructor: recibe los dos datos
	public Personaje(String anime, String nombre) {
		this.anime = anime;
		this.nombre = nombre;
	}

	public String getAnime() {
		return this.anime;
	}

	public String getNombre() {
		return this.nombre;
	}

	// Formato pedido por el enunciado: "Nombre (Anime)"
	public String toString() {
		return this.nombre + " (" + this.anime + ")";
	}
}
