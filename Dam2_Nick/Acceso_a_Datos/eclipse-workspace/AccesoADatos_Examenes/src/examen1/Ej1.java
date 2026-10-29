package examen1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Ej1 {
public static void main(String[] args) throws Exception{
	String fichero="pokedex.xml";
	String ficherotxt="pokemon.txt";
	grabartxt(fichero,ficherotxt);
	leertxt(ficherotxt);
	
	
}
//Creo este metodo que lee el fichero de texto para saber cuantos pokemons he procesado
	public static void leertxt(String ficherotxt) {
		try {
			BufferedReader lector =new BufferedReader(new FileReader("pokemon.txt"));
			
			
			String linea;
			int numPokemons=0;
			while((linea = lector.readLine())!=null) {
			if(linea.startsWith("#"))
				numPokemons++;
				
			}
			System.out.println("Pokemon procesados: "+numPokemons);
		}catch (Exception e) {
			System.out.println("Error con el fichero");
		}
	}
		public static void grabartxt(String fichero,String ficherotxt) throws ParserConfigurationException, SAXException, IOException {
			
			//Leemos el XML y lo almacenamos e el objeto doc	
			DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
			DocumentBuilder builder= factory.newDocumentBuilder();
			Document doc = builder.parse(fichero);
			//Creamos una lista iterable
			NodeList listaPokemon=doc.getElementsByTagName("pokemon");
		
			
			try(BufferedWriter bw=new BufferedWriter(new FileWriter(ficherotxt))) {
				bw.write("=== POKEDEX DEL PROFESOR OAK ===");
				bw.newLine();
			
				//Recorremos la lista de Pokemons
				for(int i=0;i<listaPokemon.getLength();i++) {
					
					//Cojo el elemento i y lo guardo en el objeto pokemon
					Node nodo=listaPokemon.item(i);
					Element pokemon =(Element) nodo;
					
					
					String nombre=pokemon.getElementsByTagName("nombre").item(0).getTextContent();
					String tipo=pokemon.getElementsByTagName("tipo").item(0).getTextContent();
					String id=pokemon.getAttribute("id");
					
					int idint=Integer.parseInt(id);
					String nivel=pokemon.getAttribute("nivel");
					
					if(idint>=100)
					bw.write("#"+id+" "+nombre+" | "+tipo.toUpperCase()+" | "+"Nivel "+nivel);
						else if(idint>=10)
							bw.write("#0"+id+" "+nombre+" | "+tipo.toUpperCase()+" | "+"Nivel "+nivel);
						else
							bw.write("#00"+id+" "+nombre+" | "+tipo.toUpperCase()+" | "+"Nivel "+nivel);
					
					bw.newLine();
				}
				
				int numPokemon=listaPokemon.getLength();
				bw.write("=== Total: "+numPokemon+" Pokemon ===");
			} catch (Exception e) {
		System.out.println(e.getMessage());
			}
			
		
}}

		



