package primerTrimestre;

// DOM: representa el XML como un árbol de nodos en memoria (Document, Element, Node, NodeList).
import org.w3c.dom.*;

// Para LEER (parsear) XML a un Document.
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
// Para ESCRIBIR el Document a fichero (Transformer) y configurar la salida.
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// CRUD de una agenda en XML usando DOM.
public class AgendaXML {

	// Secuencia de pruebas. 'throws Exception' delega todos los errores a la JVM.
	public static void main(String[] args) throws Exception{
		// Lista contactos.
		leerAgenda("agenda.xml");
		// Búsqueda de un contacto.
		buscarEnAgenda("Elena", "agenda.xml");
		buscarEnAgenda("Pepe", "agenda.xml");
		// Borrado.
		eliminarContacto("Elena", "agenda.xml");
		eliminarContacto("Pepe", "agenda.xml");
		// Alta.
		nuevoContacto("Leonor", "666554433", "agenda.xml");
		nuevoContacto("Sofía", "666554433", "agenda.xml");
		// Modificación.
		modificaTelefono("Sofía", "111222333", "agenda.xml");
		modificaTelefono("Andrés", "111222333", "agenda.xml");
	}
	
	// Muestra nombre y teléfono de cada contacto.
	public static void leerAgenda(String fichero) throws Exception{
		// Leemos el XML y lo almacenamos en el objeto doc
		// Carga todo el XML en memoria como un árbol.
		Document doc = leerXML(fichero);
		// creamos una lista iterable
		// getElementsByTagName devuelve todos los elementos <contacto> del documento.
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		// recorremos la lista de contactos
		// NodeList no es una lista de Java: se recorre con getLength() e item(i).
		for(int i=0; i<listaContactos.getLength(); i++) {
			//Cojo el elemento i y lo guardo en el objeto contacto
			// item(i) devuelve un Node genérico.
			Node nodo = listaContactos.item(i);
			// Cast a Element para poder buscar dentro de él.
			Element contacto = (Element)nodo;
			// Dentro de <contacto> buscamos <nombre>, cogemos el primero y leemos su texto.
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			// Igual con <telefono>.
			String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			System.out.println(nombre + " - " + telefono);
		}
	}
	
	// Devuelve el Element del contacto con ese nombre (o null).
	public static Element buscaContacto(String nombreBuscado, Document doc) {
		// null = no encontrado.
		Element respuesta = null;
		NodeList listaContactos = doc.getElementsByTagName("contacto");
		// La condición extra 'respuesta == null' corta el bucle al encontrarlo.
		for(int i=0; i<listaContactos.getLength() && respuesta == null; i++) {	
			Node nodo = listaContactos.item(i);
			Element contacto = (Element)nodo;
			String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
			// Comparación sin distinguir mayúsculas.
			if(nombre.equalsIgnoreCase(nombreBuscado))
				respuesta = contacto;
		}
		return respuesta;
	}
	
	// Muestra el teléfono de un contacto.
	public static void buscarEnAgenda(String nombreBuscado, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreBuscado, doc); 
		if(contacto!=null) {
			String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent();
			System.out.println("El telefono de " + nombreBuscado + " es " + telefono);
		}
		else
			System.out.println("El contacto " + nombreBuscado + " no está en tu agenda");
	}
	
	// Borra un contacto del XML.
	public static void eliminarContacto(String nombreBuscado, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreBuscado, doc);
		if(contacto!=null) {
			// El elemento raíz (<agenda>).
			Element raiz = doc.getDocumentElement();
			// Quita el nodo del árbol (en memoria).
			raiz.removeChild(contacto);
			System.out.println("El contacto " + nombreBuscado + " ha sido eliminado");		
			// Hay que volver a guardar el árbol en el fichero para que el cambio persista.
			grabarXML(doc, fichero);
		}
		else
			System.out.println("El contacto " + nombreBuscado + " no se puede eliminar porque no existe");
	}
	
	// Añade un contacto nuevo.
	public static void nuevoContacto(String nombreNuevo, String telefonoNuevo, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreNuevo, doc);
		if(contacto !=null)
			System.out.println("Ya existe un contacto llamado " + nombreNuevo);
		else {
			// Se crean los nodos con createElement...
			Element nuevoContacto = doc.createElement("contacto");
			Element elementoNombre = doc.createElement("nombre");
			Element elementoTelefono = doc.createElement("telefono");
			// ...se les pone texto...
			elementoNombre.setTextContent(nombreNuevo);
			elementoTelefono.setTextContent(telefonoNuevo);
			// ...se cuelgan unos de otros con appendChild...
			nuevoContacto.appendChild(elementoNombre);
			nuevoContacto.appendChild(elementoTelefono);
			Element raiz = doc.getDocumentElement();
			// ...y finalmente se cuelga el contacto de la raíz.
			raiz.appendChild(nuevoContacto);
			grabarXML(doc, fichero);
			System.out.println("Creado nuevo contacto: " + nombreNuevo);
		}
	}
	
	// Cambia el teléfono.
	public static void modificaTelefono(String nombreBuscado, String nuevoTelefono, String fichero) throws Exception{
		Document doc = leerXML(fichero);
		Element contacto = buscaContacto(nombreBuscado, doc);
		if(contacto!=null){
			// Obtenemos el nodo <telefono>.
			Element telefono = (Element)contacto.getElementsByTagName("telefono").item(0);
			// Cambiamos su texto.
			telefono.setTextContent(nuevoTelefono);
			grabarXML(doc,fichero);
			System.out.println("Telefono modificado en el contacto " + nombreBuscado);
		}
		else
			System.out.println("El contacto " + nombreBuscado + " no existe y no puedo modificarlo");
	}
	
	// Método auxiliar reutilizado por todos: parsea el fichero.
	public static Document leerXML(String fichero) throws Exception{
		// La fábrica crea el 'parser'.
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// El constructor de documentos.
		DocumentBuilder builder = factory.newDocumentBuilder();
		// parse lee el fichero y devuelve el árbol Document.
		return builder.parse(fichero);
	}
	
	// Método auxiliar: guarda un Document en un fichero.
	public static void grabarXML(Document doc, String fichero) throws Exception{
		// Fábrica de transformadores.
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		// El Transformer convierte el árbol en texto XML.
		Transformer transformer = transformerFactory.newTransformer();
		// Pide salida con sangrías.
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// Cuatro espacios de sangría.
		// NOTA del mentor: al borrar/añadir nodos DOM conserva los espacios en blanco antiguos y el XML puede acumular líneas en blanco (se ve en agenda.xml).
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		// Origen: el árbol DOM.
		DOMSource source = new DOMSource(doc);
		// Destino: el fichero.
		StreamResult result = new StreamResult(fichero);
		// Realiza la escritura.
		transformer.transform(source, result);
	}
}
