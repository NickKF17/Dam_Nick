import java.util.List;
import com.google.gson.annotations.SerializedName;

// Raíz del JSON de la ferretería, igual que la clase Agenda de clase
public class Ferreteria {
	// Nombre de la etiqueta raíz en el JSON
	@SerializedName("ferreteria")
	private List<Articulo> articulos;

	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
	}
}
