import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Ejercicio 1: convertir la agenda XML de clase en la agenda JSON de clase
// Reutilizamos de clase: AgendaXML.leerXML (leer el XML), Contacto y Agenda (estructura JSON)
// y AgendaJSON.guardarAgenda (grabar el JSON; en esta copia la hemos hecho public)
public class Ejercicio01 {

	public static void main(String[] args) {
		xmlAJson("datos/agenda.xml", "datos/agenda.json");
	}

	public static void xmlAJson(String ficheroXML, String ficheroJSON) {
		try {
			// 1. Leemos el XML con la función de clase
			Document doc = AgendaXML.leerXML(ficheroXML);
			NodeList listaContactos = doc.getElementsByTagName("contacto");
			// 2. Creamos la lista de objetos Contacto que Gson sabe convertir
			List<Contacto> contactos = new ArrayList<>();
			for (int i = 0; i < listaContactos.getLength(); i++) {
				Element contacto = (Element) listaContactos.item(i);
				String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
				String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
				// El DNI no existe en el XML: lo dejamos en blanco
				contactos.add(new Contacto(nombre, telefono, ""));
			}
			// 3. Grabamos el JSON con la función de clase
			AgendaJSON.guardarAgenda(ficheroJSON, contactos);
			System.out.println("Convertidos " + contactos.size() + " contactos a " + ficheroJSON);
		} catch (Exception e) {
			System.out.println("Error en la conversión: " + e.getMessage());
		}
	}
}
