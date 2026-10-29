package AgendaJsonyXML;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GSON {

	public static void main(String[] args) {
		final String ruta = "agendaJSON.json";
		leerAgenda(ruta);

		Contactos c = new Contactos("Nino Nakano", "616312116", "03492765P");
		Contactos c2 = new Contactos("Miku Nakano", "676767676", "67676767S");
		Contactos c3 = new Contactos("Itsuki Nakano", "676767676", "67676767S");
		
		
		crearContacto(c, ruta);
		crearContacto(c2, ruta);
		crearContacto(c3, ruta);
		
		
		borrarContacto("PiniPepini", ruta);
		borrarContacto("AndresitoPenesito", ruta);
		
		
		modificarTelefono("Nino Nakano", "67676767", ruta);
	}

	public static Contactos buscarContacto(List<Contactos> contactos, String nombrebuscado) {
		Contactos contacto=null;
		/* Forma mas simple pero con break
		 for(Contacto c:contactos)
		 if(c.getNombre().equalsIgnoreCase(buscado.getNombre())){
		 contacto=c;
		 break;
		 }
		 */
		
		for (int i=0;i<contactos.size();i++)
			if(contactos.get(i).getNombre().equalsIgnoreCase(nombrebuscado))
				contacto=contactos.get(i);
		return contacto;
	}
	
	
	public static List<Contactos> cargarListaContactos(String ruta) {
		List<Contactos> contactos = null;
		try (Reader lector = new FileReader(ruta)) {
			Gson gson = new Gson();
			Agenda agenda = gson.fromJson(lector, Agenda.class);
			contactos = agenda.getContactos();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		return contactos;
	}

	public static void leerAgenda(String ruta) {
		List<Contactos> contactos = cargarListaContactos(ruta);
		if (contactos != null) {
			for (Contactos contacto : contactos)
				System.out.println(contacto);
		}
	}

	public static void crearContacto(Contactos nuevo, String ruta) {
		List<Contactos> contactos = cargarListaContactos(ruta);
		if (contactos != null) {
			if(buscarContacto(contactos, nuevo.getNombre())==null) {
				contactos.add(nuevo);
				guardarAgenda(contactos, ruta);
				System.out.println("Contacto grabado");}
			 else
				System.out.println("El contacto " + nuevo.getNombre() + " ya está");
		}
}
	
	public static void borrarContacto(String nombre, String ruta) {
		List<Contactos> contactos = cargarListaContactos(ruta);
		if (contactos != null) {
			Contactos encontrado=buscarContacto(contactos, nombre);
			if(encontrado!=null) {
				contactos.remove(encontrado);
				guardarAgenda(contactos, ruta);
				System.out.println("Contacto borrado");}
			 else
				System.out.println("El contacto " +nombre + " no existe");
		}
	}
	public static void modificarTelefono(String nombre,String telefono, String ruta) {
		List<Contactos> contactos = cargarListaContactos(ruta);
		if (contactos != null) {
			Contactos encontrado=buscarContacto(contactos, nombre);
			if(encontrado!=null) {
				contactos.remove(encontrado);
				contactos.add(new Contactos(encontrado.getNombre(),telefono,encontrado.getDni()));
				guardarAgenda(contactos, ruta);
				System.out.println("Contacto Modificado");}
			 else
				System.out.println("El contacto " +nombre + " no existe");
		}
	}
	public static void guardarAgenda(List<Contactos> contactos, String ruta) {
		try (Writer escritor = new FileWriter(ruta)) {
			Agenda agenda = new Agenda();
			agenda.setContactos(contactos);

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(agenda, escritor);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
