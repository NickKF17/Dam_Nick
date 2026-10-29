package examen1;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ucm {
	@SerializedName("ucm")
	private List<peliculas> peliculas;

	public List<peliculas> getPeliculas() {
		return peliculas;
	}

	public void setPeliculas(List<peliculas> lista) {
		peliculas = lista;
	}
}
