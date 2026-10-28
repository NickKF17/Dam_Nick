package primerTrimestre;

// List.
import java.util.List;
import com.google.gson.annotations.SerializedName;

// Ningún cambio en esta clase
// Raíz del JSON, igual que Agenda pero con ContactoDup.
public class AgendaDup {
	// Enlaza con la clave "agenda".
	@SerializedName("agenda")
	// Lista de contactos. NOTA del mentor: el atributo se llama contactoDups y no contactos; funciona por @SerializedName.
	private List<ContactoDup> contactoDups = null;
	
	// Getter.
	public List<ContactoDup> getContactos() {
		return contactoDups;
	}
	
	// Setter.
	public void setContactos(List<ContactoDup> contactoDups) {
		this.contactoDups = contactoDups;
	}
}
