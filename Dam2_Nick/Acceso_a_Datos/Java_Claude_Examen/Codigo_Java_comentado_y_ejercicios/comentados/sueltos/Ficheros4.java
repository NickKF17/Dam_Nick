package tercerTrimestre;

// RandomAccessFile: permite saltar a cualquier posición del fichero (acceso aleatorio) y leer/escribir allí.
import java.io.RandomAccessFile;
// HashMap: diccionario clave-valor (nombre -> edad) para cargar los datos iniciales.
import java.util.HashMap;

// Mini-agenda con REGISTROS DE TAMAÑO FIJO: crear, leer, modificar, añadir y borrar (lógicamente) registros.
public class Ficheros4 {

	// Usar variables globales está mal visto y hay que limitarlo lo mas posible
	// Usar constantes globales, sin embargo, resulta muy útil.
	// En este caso definimos aquí los tamaños de los datos que vamos a usar de forma
	// que podamos cambiarlo en un único punto si lo necesitamos
	// static final = constante de clase. Todos los nombres ocupan exactamente 20 caracteres.
	static final int TAMANYO_NOMBRE = 20; // caracteres. un caracter ocupa 2 bytes
	// Un int ocupa 4 bytes.
	static final int TAMANYO_EDAD = 4; // bytes. un entero ocupa 4 bytes
	// Tamaño de un registro = 20 chars * 2 bytes + 4 bytes = 44 bytes. Gracias a que es fijo podemos calcular dónde empieza cada registro.
	static final int TAMANYO_REGISTRO = TAMANYO_NOMBRE *2 + TAMANYO_EDAD;
	
	// Programa de prueba: ejecuta todas las operaciones y muestra qué pasa también en los casos de error.
	public static void main(String[] args) {
		// El acceso aleatorio a un fichero nos permite dirigirnos directamente a un dato concreto sin necesidad
		// de pasar por los demas. Lo mas importante para ello es que los "registros" de datos tengan un
		// tamaño fijo.

		// Vamos a crear una pequeña agenda en un fichero. Los registros se componen de nombre y edad
		// Cargamos los datos en un diccionario inicialmente
		// Fichero de datos (sin ruta: se crea en la carpeta del proyecto).
		String fichero = "registros.dat";
		// Datos de partida: nombre (clave) -> edad (valor).
		HashMap<String, Integer> agenda = new HashMap<>();
		// put añade una pareja clave-valor.
		// NOTA del mentor: un HashMap no garantiza el orden, así que el número de registro de cada persona puede variar de una ejecución a otra.
		agenda.put("Isabel", 35);
		agenda.put("Marcos", 51);
		agenda.put("José María", 57);
		agenda.put("Luis", 23);
		
		// Todas las excepciones se capturan aquí, en el main (los métodos hacen 'throws Exception').
		try {
			// creamos el fichero
			// Crea el fichero y escribe todos los registros.
			crearRegistro(fichero, agenda);
			// leemos el registro número 2
			// Lee directamente el registro 2 sin leer el 1.
			leerRegistro(fichero,2);
			// modificamos el contenido del registro número 2
			// Sobrescribe el registro 2.
			modificarRegistro(fichero, 2, "José Miguel", 56);
			// intentamos modificar un registro que no existe
			// Registro inexistente: debe avisar sin romper.
			modificarRegistro(fichero, 200, "Luis Miguel", 31);
			leerRegistro(fichero,2);
			// intentamos leer un registro que no existe
			// Lectura de registro inexistente.
			leerRegistro(fichero,500);
			// leemos todos los registros
			// Recorre el fichero de principio a fin.
			leerTodosLosRegistros(fichero);
			// Añadimos un registro nuevo
			// Añadir un registro nuevo al final (desactivado).
			//anyadeRegistro(fichero,"Armando", 35);
			leerRegistro(fichero,5);
			// Borramos el registro 3
			// Borrado LÓGICO: no se elimina físicamente, se marca con un '*'.
			borrarRegistro(fichero,3);
			// tratamos de borrar, leer o modificar un registro marcado como borrado
			borrarRegistro(fichero,3);
			leerRegistro(fichero,3);
			modificarRegistro(fichero, 3, "José Miguel", 56);
			leerTodosLosRegistros(fichero);
			
		// Un único catch para todo el programa.
		}catch(Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	// otra forma de tratar las excepciones: se capturan todas desde el programa principal
	// en los métodos añadimos la clausula throws Exception (o el nombre de la excepción concreta)
	// cuando se produzca esa excepción el método deja de ejecutarse y vuelve al programa principal
	// y se ejecuta allí el bloque catch
	// Crea el fichero. 'throws Exception': si falla, la excepción sube al main.
	public static void crearRegistro (String fichero, HashMap<String, Integer> agenda) throws Exception{
		// el modo rw permite leer y escribir y crea el fichero si no existe. No existe modo w como en los ficheros de texto
		// los modos rws y rwd son similares pero escriben directamente a disco y no a cache.
		// Modo "rw": lectura y escritura, crea el fichero si no existe. Se cierra solo.
		try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {

			// keySet() devuelve todas las claves del diccionario (los nombres).
			for (String nombre : agenda.keySet()) {
                // Escribe el nombre con tamaño fijo.
                escribirNombre(raf, nombre);
                // get(nombre) devuelve la edad; writeInt la graba en 4 bytes.
                raf.writeInt(agenda.get(nombre));  // Los enteros se graban con 4 bytes
            }      
            // Informamos del número de registros.
            System.out.println("Archivo creado con " + agenda.size() + " registros");
            // length nos devuelve el tamaño en bytes del archivo. Nos va a ayudar a ver que todo vaya bien
            // length() = tamaño del fichero en bytes; debería ser nº de registros * 44.
            System.out.println("Tamaño total del archivo: " + raf.length() + " bytes");
        }
	}
	
	// Método para escribir un nombre con tamaño fijo
    // Escribe un nombre rellenando con espacios hasta 20 caracteres.
    private static void escribirNombre(RandomAccessFile raf, String nombre) throws Exception {
    	// Crear un array de caracteres del tamaño fijo
        // Array de 20 caracteres.
        char[] chars = new char[TAMANYO_NOMBRE];     
        // Llenar con espacios o con el nombre
        // Recorremos las 20 posiciones.
        for (int i = 0; i < TAMANYO_NOMBRE; i++) {
            // Si aún quedan letras del nombre, las copiamos...
            if (i < nombre.length()) {
                // charAt(i) = i-ésimo carácter del String.
                // NOTA del mentor: si el nombre tiene más de 20 letras se recorta sin avisar.
                chars[i] = nombre.charAt(i);
            } else {
                // ...y si no, rellenamos con un espacio.
                chars[i] = ' ';  // Rellenar con espacios
            }
        }
        // Escribir cada caracter como char (2 bytes cada uno)
        // Escribimos los 20 chars.
        for (char c : chars) {
            // Cada char ocupa 2 bytes en el fichero.
            raf.writeChar(c);
        }
    }
    
    // Lee un registro concreto usando su número (el 1 es el primero).
    private static void leerRegistro(String fichero, int registro) throws Exception {
    	// el modo r es de solo lectura. provoca excepción si el fichero no existe
    	// Modo "r": solo lectura.
    	try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
            // Calcular la posicion donde empezamos a leer
    		// El registro 1 es el primero (y empieza en la posición 0)
            // Fórmula clave del acceso aleatorio: posición en bytes = (nº registro - 1) * tamaño del registro.
            // NOTA del mentor: la multiplicación se hace con int; en ficheros enormes podría desbordar.
            long offset = (registro-1) * TAMANYO_REGISTRO;
            // Si el offset cae fuera del fichero, el registro no existe.
            if(offset >=raf.length()) {
            	// Mensaje de error.
            	System.out.println("No existe el registro " + registro);
            	// Último registro = tamaño del fichero / tamaño del registro.
            	System.out.println("El registro mas alto es el " + raf.length()/TAMANYO_REGISTRO);
            }
            else {
            	// seek nos permite posicionarnos en un punto del fichero
            	// offset es un número de bytes a partir del principio del fichero
            	// seek coloca el puntero de lectura/escritura en esa posición.
            	raf.seek(offset);
            	// Lee 20 chars y devuelve el nombre sin espacios sobrantes.
            	String nombre = leerNombre(raf);
            	// Si empieza por * está borrado lógicamente.
            	// NOTA del mentor: si el nombre estuviera vacío, charAt(0) lanzaría una excepción.
            	if(nombre.charAt(0)=='*')
            		System.out.println("El registro " + registro + " está marcado para ser eliminado");
            	else {
            		// Tras leer el nombre el puntero ya está justo en la edad: se lee sin hacer seek.
            		int edad = raf.readInt();
            		// Mostramos el registro formateado.
            		System.out.printf("Registro %d: '%s', %d años%n", registro, nombre, edad);
            	}
            }
        }
    }
    
    // Lee un nombre de tamaño fijo.
    private static String leerNombre(RandomAccessFile raf) throws Exception {
        // Acumulador.
        String nombre ="";
        // Leer cada caracter
        for (int i = 0; i < TAMANYO_NOMBRE; i++) {
            // Lee un char (2 bytes).
            char c = raf.readChar();
            // Concatenar en un bucle es ineficiente (StringBuilder sería mejor), pero a este nivel es claro y válido.
            nombre = nombre + c;
        }
        // trim() quita los espacios del principio y final: elimina el relleno.
        return nombre.trim();  // trim() para eliminar los espacios en blanco
    }
    
    // Modificar un registro existente.
    private static void modificarRegistro(String fichero, int registro, String nombreNuevo, int edadNueva) throws Exception{
    	// Modo lectura+escritura.
    	try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            long offset = (registro-1) * TAMANYO_REGISTRO;
            if(offset>=raf.length()) {
            	System.out.println("No existe el registro " + registro);
            	System.out.println("El registro mas alto es el " + raf.length()/TAMANYO_REGISTRO);
            }
            else {
            	raf.seek(offset);
            	String nombre = leerNombre(raf);
            	// Solo se modifica si no está borrado.
            	if(nombre.charAt(0)!='*') {
                	// Volvemos al inicio del registro porque al leer el nombre hemos avanzado.
                	raf.seek(offset);
                	// Sobrescribimos el nombre...
                	escribirNombre(raf, nombreNuevo);
                	// ...y la edad.
                	raf.writeInt(edadNueva);
                	System.out.println("Registro " + registro + " modificado");
            	}
            	else
            		System.out.println("El registro " + registro + " no puede ser modificado porque está marcado para ser eliminado");
            }
        }
    }
    
    // Lista todos los registros activos.
    private static void leerTodosLosRegistros(String fichero) throws Exception {
    	try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
    		// calculamos el número de registros a partir del tamaño del fichero
    		// Nº de registros calculado a partir del tamaño.
    		int numRegistros = (int)raf.length()/TAMANYO_REGISTRO;
    		// Recorrido secuencial: el puntero avanza solo, no hace falta seek.
    		for(int i=0; i< numRegistros; i++) {
    			// Lee el nombre del registro i.
    			String nombre = leerNombre(raf);
   				// SIEMPRE se lee la edad, aunque esté borrado, para dejar el puntero al principio del siguiente registro.
   				int edad = raf.readInt();
   				// Solo mostramos los no borrados.
   				if(nombre.charAt(0)!='*')
   					System.out.printf("Registro %d: '%s', %d años%n", i+1, nombre, edad);
    		}
        }
    }
    
    // Añade un registro al final.
    private static void anyadeRegistro(String fichero, String nombre, int edad) throws Exception{
    	try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            // Nos colocamos justo al final del fichero.
            raf.seek(raf.length());
            escribirNombre(raf, nombre);
            raf.writeInt(edad);
            System.out.println("Registro añadido");
        }
    }    
    
    // Borrado lógico.
    private static void borrarRegistro(String fichero, int registro) throws Exception {
    	try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {
            long offset = (registro-1) * TAMANYO_REGISTRO;
            if(offset >=raf.length()) {
            	System.out.println("No existe el registro " + registro);
            	System.out.println("El registro mas alto es el " + raf.length()/TAMANYO_REGISTRO);
            }
            else {
            	raf.seek(offset);
            	String nombre = leerNombre(raf);
            	// usaremos un * para reemplazar el primer caracter como marca de borrado
           		if (nombre.charAt(0) == '*')
           			System.out.println("El registro " + registro + " ya había sido borrado");
           		else {
           			// Sustituimos la primera letra por '*'.
           			// NOTA del mentor: char + String funciona porque '*' + String concatena; substring(1) es el nombre sin su primera letra.
           			nombre = '*' + nombre.substring(1);
           			raf.seek(offset);
           			escribirNombre(raf,nombre);
           			System.out.println("Registro " + registro + " eliminado con éxito");
           		}
           		
            }
        }
    }
}
