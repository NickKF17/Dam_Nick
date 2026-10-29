package examen1;


public class peliculas {
	    private String titulo;
	    
	    private String anio;
	    private String fase;
	    private String director;
public peliculas(String titulo, String anio,String fase,String director ) {
	this.titulo=titulo;
	this.anio=anio;
	this.fase=fase;
	this.director=director;
}
@Override
public String toString() {
	return "Nombre: " + this.titulo + "\nFase: " + this.fase + "\nAnio: " + this.anio+ "\nDirector: " + this.director;
}
}
