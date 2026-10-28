import java.io.Serializable;

// Representa un partido político y sus votos. Serializable para poder guardarlo en el fichero binario
public class Partido implements Serializable {
	private String nombre;
	private int votos;

	public Partido(String nombre, int votos) {
		this.nombre = nombre;
		this.votos = votos;
	}

	public String getNombre() {
		return this.nombre;
	}

	public int getVotos() {
		return this.votos;
	}

	// Suma votos nuevos a los que ya tiene
	public void sumarVotos(int nuevos) {
		this.votos += nuevos;
	}
}
