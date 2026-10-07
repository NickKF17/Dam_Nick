package AgendaJsonyXML;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MigrarXMLJSON {
    final static String ruta = "agendaXML.json";

    public static void main(String[] args) {
        try {
            leerAgendaXML("agenda.xml");
        } catch (Exception e) {
            System.err.println("Error procesando el archivo XML: " + e.getMessage());
        }
    }

    private static void leerAgendaXML(String fichero) throws Exception {
        List<Contacto> contactos = cargarListaContactos(ruta);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(fichero);

        NodeList listaContactos = doc.getElementsByTagName("contacto");

        for (int i = 0; i < listaContactos.getLength(); i++) {
            Node nodo = listaContactos.item(i);
            
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                Element contacto = (Element) nodo;

                String nombre = contacto.getElementsByTagName("nombre").item(0).getTextContent();
                String telefono = contacto.getElementsByTagName("telefono").item(0).getTextContent().trim();
                System.out.println(nombre + " - " + telefono);
                int tel=Integer.parseInt(telefono);
                contactos.add(new Contacto(nombre, tel ,"0"));
           
            }
        }

        // Guardar en JSON solo una vez al terminar la lectura completa
        guardarAgenda(contactos, ruta);
    }

    public static void guardarAgenda(List<Contacto> contactos, String ruta) {
        try (Writer escritor = new FileWriter(ruta)) {
            Agenda agenda = new Agenda();
            agenda.setContactos(contactos);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(agenda, escritor);
        } catch (Exception e) {
            System.err.println("Error al guardar JSON: " + e.getMessage());
        }
    }

    public static List<Contacto> cargarListaContactos(String ruta) {
        List<Contacto> contactos = new ArrayList<>();
        try (Reader lector = new FileReader(ruta)) {
            Gson gson = new Gson();
            Agenda agenda = gson.fromJson(lector, Agenda.class);
            if (agenda != null && agenda.getContactos() != null) {
                contactos = agenda.getContactos();
            }
        } catch (Exception e) {
            // Si el archivo no existe o está vacío, se retorna una lista vacía inicializada
            System.out.println("No se pudo cargar la agenda previa. Se creará una nueva.");
        }

        return contactos;
    }
}