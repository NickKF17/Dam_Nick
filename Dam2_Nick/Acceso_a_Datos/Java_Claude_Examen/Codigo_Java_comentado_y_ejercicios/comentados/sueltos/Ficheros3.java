package tercerTrimestre;

// Streams binarios: Data*Stream para tipos primitivos y Object*Stream para objetos completos.
import java.io.DataInputStream;
import java.io.DataOutputStream;
// File*Stream: abren el fichero a nivel de bytes. Los Data/Object envuelven a estos.
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

// Ficheros BINARIOS: tipos primitivos, un objeto y una lista de objetos.
public class Ficheros3 {

	// Programa principal: prueba los tres tipos de uso en orden.
	public static void main(String[] args) {
		// Los ficheros binarios guardan información no legible ni modificable directamente
		// ventajas: menos espacio, mas velocidad, permite guardar datos complejos y persistencia de objetos de forma mas cómoda
		// Ruta del fichero binario (extensión .bin por convención, Java no la exige).
		String fichero = "/home/josemaria/binario.bin";
		// 1) Escribir datos primitivos sueltos.
		escribirFichero(fichero);
		// 2) Leerlos en el MISMO orden.
		leerFichero(fichero);
		
		// Persistencia de objetos
		// 3) Persistencia de un objeto.
		Pokemon pokemon = new Pokemon(6, "Charizard", "Fuego", "Volador");
		// Graba el objeto (sobrescribe el fichero).
		guardarPokemon(pokemon, fichero);
		// Recupera un objeto desde el fichero.
		Pokemon pokemonRecuperado = recuperarPokemon(fichero);
		// Si hubo error al leer, el método devuelve null: se comprueba antes de usarlo.
		if(pokemonRecuperado!=null)
			pokemonRecuperado.mostrar();
		
		// Guardar y recuperar listas de objetos
		// 4) Lista de objetos: creamos varios pokemon...
		Pokemon p1 = new Pokemon(1, "Bulbasaur", "Planta");
		Pokemon p2 = new Pokemon(6, "Charizard", "Fuego", "Volador");
		Pokemon p3 = new Pokemon(2, "Ivysaur", "Planta");
		Pokemon p4 = new Pokemon(25, "Pikachu", "Eléctrico");
		Pokemon p5 = new Pokemon(11, "Metapod", "Bicho");
		Pokemon p6 = new Pokemon(7, "Squirtle", "Agua");
		// ...los metemos en una lista...
		ArrayList<Pokemon> listaPokemons = new ArrayList<>(List.of(p1, p2, p3, p4, p5, p6));
		// ...y grabamos la lista entera como un único objeto.
		guardarListaPokemons(listaPokemons, fichero);
		// Recuperamos la lista completa.
		ArrayList<Pokemon> listaRecuperada = recuperarListaPokemons(fichero);
		// Recorremos y mostramos cada pokemon.
		for (Pokemon poke:listaRecuperada) {
			poke.mostrar();
		}
		
		// Si quisieramos añadir un pokemon a un fichero existente lo recuperamos, añadimos el fichero a la lista y volvemos a grabar
		// 5) Añadir a un fichero existente: no se puede 'añadir' a un fichero de objetos; hay que leer, modificar y volver a escribir todo.
		Pokemon p7 = new Pokemon(131,"Lapras","Agua","Hielo");
		listaRecuperada = recuperarListaPokemons(fichero);
		// Añadimos el nuevo a la lista en memoria.
		listaRecuperada.add(p7);
		// Volvemos a grabar la lista completa.
		guardarListaPokemons(listaRecuperada, fichero);
		listaRecuperada = recuperarListaPokemons(fichero);
		for (Pokemon poke:listaRecuperada) {
			poke.mostrar();
		}
	}
	
	// Escritura de primitivos.
	public static void escribirFichero(String fichero) {
		// Fíjate que usamos "output" para escribir e "input" para leer. Puede parecer contradictorio
		// pero piensa que el punto de vista es el de la "persona" que lee o graba
		// cuando escribes es output porque los datos salen de ti hacia el fichero
		// cuando lees es input porque los datos vienen a ti desde el fichero
		 // DataOutputStream envuelve un FileOutputStream y ofrece writeInt, writeDouble, etc.
		 try (DataOutputStream binario = new DataOutputStream(new FileOutputStream(fichero))) {
	            // Lo mas importante a la hora de escribir en un fichero binario es que importa el tipo de datos
			 	// que se escribe. En los de texto no, porque siempre escribimos texto. Aquí usamos métodos distintos
			 	// por cada tipo de datos
			 	// Por lo demás, la estructura es muy similar
	            // Entero: siempre 4 bytes.
	            binario.writeInt(42);             
	            // Double: 8 bytes.
	            binario.writeDouble(3.14159);     
	            // Boolean: 1 byte.
	            binario.writeBoolean(true);       
	            // String: se guarda con 2 bytes de longitud + el texto en UTF-8 modificado.
	            binario.writeUTF("Hola Mundo");   
	            // Char: 2 bytes.
	            binario.writeChar('A');           
	            
	            System.out.println("Datos escritos correctamente en " + fichero);         
	        } catch (Exception e) {
	            System.err.println("Error: " + e.getMessage());
	        }
	}
	
	// Lectura de primitivos.
	public static void leerFichero(String fichero) {
		// DataInputStream envuelve un FileInputStream.
		try (DataInputStream binario = new DataInputStream(new FileInputStream(fichero))) {
            
            // Tenemos que leer en el mismo orden y los mismos tipos que se escribieron
			// Prueba a intercambia el orden de las dos líneas siguientes y verás que lo que se lee
			// en ese caso es totalmente distinto a lo que escribiste
            // El orden y el tipo de lectura DEBEN coincidir con los de escritura: el fichero no guarda ninguna etiqueta, son solo bytes.
            int entero = binario.readInt();
            // Lee 8 bytes como double.
            double decimal = binario.readDouble();
            // Lee 1 byte como boolean.
            boolean bool= binario.readBoolean();
            // Lee la longitud y luego el texto.
            String texto = binario.readUTF();
            // Lee 2 bytes como char.
            char caracter = binario.readChar();
            
            // Mostramos lo leído.
            System.out.println("Datos leídos del fichero:");
            System.out.println("Entero: " + entero);
            System.out.println("Double: " + decimal);
            System.out.println("Booleano: " + bool);
            System.out.println("String: " + texto);
            System.out.println("Char: " + caracter);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	// Guardar UN objeto.
	public static void guardarPokemon(Pokemon pokemon, String fichero) {
		// guardar un objeto es sumamente fácil. Usamos siempre el mismo método y no hace falta usar un "cast"
        // ObjectOutputStream serializa (convierte en bytes) objetos completos. Pokemon debe implementar Serializable.
        try (ObjectOutputStream binario = new ObjectOutputStream(new FileOutputStream(fichero))) {
            // Un único método para cualquier objeto; guarda también los objetos a los que apunta (por ejemplo su evolución).
            binario.writeObject(pokemon);
            System.out.println("Pokemon guardado correctamente en " + fichero);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	// Recuperar UN objeto.
	public static Pokemon recuperarPokemon(String fichero) {
		// Valor por defecto si algo falla.
		Pokemon pokemon = null;
        // ObjectInputStream reconstruye el objeto a partir de los bytes.
        try (ObjectInputStream binario = new ObjectInputStream(new FileInputStream(fichero))) {
        	// Al leer, el cast al tipo de objeto es obligatorio
            // readObject devuelve Object: el cast a Pokemon es obligatorio.
            pokemon = (Pokemon) binario.readObject();
            System.out.println("Pokemon recuperado correctamente");
            
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        // Devolvemos el objeto, o null si hubo error.
        return pokemon;
    }
	
	// Guardar una lista: ArrayList también es Serializable y arrastra a todos sus elementos.
	public static void guardarListaPokemons(ArrayList<Pokemon> lista, String fichero) {
		// Un método muy práctico para guardar y recuperar objetos sin preocuparnos de cuantos son es guardarlos en una lista
		// La lista se trata como un único objeto independientemente de su tamaño
        try (ObjectOutputStream binario = new ObjectOutputStream(new FileOutputStream(fichero))) {
            // La lista entera se graba como UN objeto, sin importar cuántos pokemon tenga.
            binario.writeObject(lista);
            System.out.println("Lista de Pokemons guardada correctamente en " + fichero);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
	}
	
	// Recuperar una lista.
	public static ArrayList<Pokemon> recuperarListaPokemons(String fichero) {
		// null por defecto.
		ArrayList <Pokemon> lista = null;
        try (ObjectInputStream binario = new ObjectInputStream(new FileInputStream(fichero))) {        
        	// Al leer, la lista es también un único objeto de forma independiente a su tamaño
        	// No olvides el cast, que también hace falta
            // NOTA del mentor: el cast genera un aviso 'unchecked' porque Java no puede comprobar en tiempo de ejecución que dentro hay Pokemon. Es habitual y aceptable aquí.
            lista = (ArrayList<Pokemon>) binario.readObject();
            System.out.println("Lista de Pokemons recuperada correctamente");
            
        }catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        // Devolvemos la lista o null.
        return lista;
    }
}
