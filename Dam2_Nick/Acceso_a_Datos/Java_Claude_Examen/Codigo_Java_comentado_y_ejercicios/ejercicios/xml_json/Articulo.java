// Un artículo de la ferretería. Los nombres de los atributos serán los nombres de las etiquetas JSON
public class Articulo {
	private String referencia;
	private String descripcion;
	private int stock;

	public Articulo(String referencia, String descripcion, int stock) {
		this.referencia = referencia;
		this.descripcion = descripcion;
		this.stock = stock;
	}
}
