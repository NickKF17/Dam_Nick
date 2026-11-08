package es.ut2.apprinc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Pide al usuario los datos por teclado, validandolos. Lee en UTF-8 para que
 * los nombres con tilde o dieresis lleguen bien.
 *
 * Parametros del constructor:
 *   in -> flujo de entrada del que se lee (normalmente System.in)
 *
 * Salidas: un objeto DatosEntrada con datos validos. Si se acaba la entrada
 * antes de tener todos los datos lanza IllegalStateException.
 */
public class EntradaUsuario {

    private final BufferedReader lector;

    /**
     * Crea el lector de datos.
     *
     * @param in flujo de entrada (teclado)
     */
    public EntradaUsuario(InputStream in) {
        this.lector = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    /**
     * Pide los cuatro datos en el orden: nombre, ruta, numero de transferencias, hilos.
     *
     * @return los datos introducidos (ya validados)
     */
    public DatosEntrada pedirDatos() {
        String nombre = pedirTexto("Dime el nombre del fichero a generar", Constantes.NOMBRE_POR_DEFECTO);
        String ruta = pedirTexto("Dime la ruta donde lo guardo (acabala en barra)", Constantes.RUTA_POR_DEFECTO);
        int transferencias = pedirEnteroPositivo("Dime el numero de transferencias a generar");
        int hilos = pedirEnteroPositivo("Dime el numero de hilos con el que procesar el fichero");
        return new DatosEntrada(nombre, ruta, transferencias, hilos);
    }

    /**
     * Pide un texto; si el usuario solo pulsa enter se usa el valor por defecto.
     *
     * @param pregunta pregunta que se muestra
     * @param defecto  valor que se usa si no se escribe nada
     * @return el texto escrito o el valor por defecto
     */
    private String pedirTexto(String pregunta, String defecto) {
        System.out.println(pregunta + " [por defecto: " + defecto + "]:");
        String texto = leerLinea().trim();
        if (texto.isEmpty()) {
            System.out.println("-> Se usa el valor por defecto: " + defecto);
            return defecto;
        }
        return texto;
    }

    /**
     * Pide un numero entero mayor que 0, repitiendo la pregunta hasta que sea valido.
     *
     * @param pregunta pregunta que se muestra
     * @return el numero introducido (mayor que 0)
     */
    private int pedirEnteroPositivo(String pregunta) {
        while (true) {
            System.out.println(pregunta + ":");
            String texto = leerLinea().trim();
            try {
                int numero = Integer.parseInt(texto);
                if (numero > 0) {
                    return numero;
                }
                System.out.println("-> Error: el valor debe ser mayor que 0.");
            } catch (NumberFormatException e) {
                System.out.println("-> Error: '" + texto + "' no es un numero entero valido.");
            }
        }
    }

    /**
     * Lee una linea del teclado.
     *
     * @return la linea leida (sin salto de linea)
     * @throws IllegalStateException si se ha acabado la entrada o falla la lectura
     */
    private String leerLinea() {
        try {
            String linea = lector.readLine();
            if (linea == null) {
                throw new IllegalStateException("se ha terminado la entrada de datos");
            }
            return linea;
        } catch (IOException e) {
            throw new IllegalStateException("error leyendo la entrada: " + e.getMessage());
        }
    }
}
