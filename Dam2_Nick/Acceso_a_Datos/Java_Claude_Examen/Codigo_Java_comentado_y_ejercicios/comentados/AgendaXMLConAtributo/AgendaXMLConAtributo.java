package primerTrimestre;

// DOM: Document, Element, NodeList...
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// Lectura de un XML donde cada contacto puede tener VARIOS teléfonos y cada teléfono un atributo 'tipo'.
public class AgendaXMLConAtributo {

	// Todo el código está en main porque solo lee.
	public static void main(String[] args) throws Exception{
		// Fichero con atributos.
		String fichero = "agenda3.xml";

		// Parseo del XML.
		Document doc = leerXML(fichero);
		// Todos los <contacto>.
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		// getLength() = cuántos contactos hay.
		System.out.println("Total de contactos: " + listaContactos.getLength());
		// Recorre los contactos.
		for(int i=0; i<listaContactos.getLength(); i++) {
			// Cast directo del Node a Element.
			Element contacto = (Element)listaContactos.item(i);
			// Nombre del contacto.
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			System.out.println(nombre);
			//String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			// Ahora pedimos TODOS los <telefono> del contacto, no solo el primero.
			NodeList telefonos = contacto.getElementsByTagName("telefono");
			System.out.println("Teléfonos: ");
			// Bucle interior, con j, para los teléfonos.
			for (int j = 0; j < telefonos.getLength(); j++) {
				// Cada teléfono como Element.
				Element telefono = (Element) telefonos.item(j);
				// getAttribute lee el atributo tipo="...". NOTA del mentor: si no existe el atributo devuelve cadena vacía (ocurre con el teléfono 612121255).
				String tipo = telefono.getAttribute("tipo");
				// Imprime número y entre paréntesis el tipo.
				System.out.println(" - " + telefono.getTextContent() + "(" + tipo + ")");
			}
			System.out.println("");
		}
	}
	
	// Método auxiliar para parsear (igual que en AgendaXML).
	public static Document leerXML(String fichero) throws Exception{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(fichero);
	}
}
