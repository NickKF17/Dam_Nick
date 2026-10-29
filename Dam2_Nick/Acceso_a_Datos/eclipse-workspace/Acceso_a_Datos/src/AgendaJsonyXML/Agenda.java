package AgendaJsonyXML;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Agenda {
	@SerializedName("agenda")
	private List<Contactos> contactos;

	public List<Contactos> getContactos() {
		return contactos;
	}

	public void setContactos(List<Contactos> lista) {
		contactos = lista;
	}
}
