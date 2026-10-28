// Clase sencilla para guardar los datos de un contacto de la agenda (ejercicios 7 y 8)
public class Contacto {
	private String nombre;
	private String apellidos; // Puede ser "" (ejercicio 8 permite no tener apellidos)
	private String tipo;      // Familia, Amigo o Conocido ("" en el ejercicio 7)
	private int edad;

	// Constructor del ejercicio 7 (sin tipo)
	public Contacto(String nombre, String apellidos, int edad) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.tipo = "";
		this.edad = edad;
	}

	// Constructor del ejercicio 8 (con tipo). Es una sobrecarga: mismo nombre, otros parámetros
	public Contacto(String nombre, String apellidos, String tipo, int edad) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.tipo = tipo;
		this.edad = edad;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getTipo() {
		return tipo;
	}

	public int getEdad() {
		return edad;
	}

	// Cómo se muestra el objeto cuando hacemos System.out.println(contacto)
	public String toString() {
		String texto = nombre;
		if (!apellidos.isEmpty())
			texto += " " + apellidos;
		if (!tipo.isEmpty())
			texto += " [" + tipo + "]";
		return texto + ", " + edad + " años";
	}
}
