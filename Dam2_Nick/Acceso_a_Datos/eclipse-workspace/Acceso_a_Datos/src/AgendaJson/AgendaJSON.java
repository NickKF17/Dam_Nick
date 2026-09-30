package AgendaJson;

import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class AgendaJSON {
public static void main(String[] args){
	final String ruta="agendaJSON.json";
	leerAgenda(ruta);
}

private static void leerAgenda(String ruta) {
		List<Contacto> contactos= cargarLista(ruta);
		for(Contacto c: contactos)
			c.mostrar();
}


private static List<Contacto> cargarLista(String ruta) {
	List<Contacto> contactos = null;
try(Reader lector =new FileReader(ruta)){
	Gson gson= new Gson();
	Agenda agenda=gson.fromJson(lector, Agenda.class);
	contactos= agenda.getContactos();
}catch(Exception e){
	System.out.println(e.getMessage());
	}
return contactos ;
}}


