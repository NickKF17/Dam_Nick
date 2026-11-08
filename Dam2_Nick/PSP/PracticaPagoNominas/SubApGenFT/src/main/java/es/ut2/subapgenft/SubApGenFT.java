package es.ut2.subapgenft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Random;

/**
 * SubApGenFT - Sub-aplicacion de Generacion del Fichero de Transferencias.
 * Simula la recepcion del fichero de transferencias que la empresa envia al banco.
 *
 * Flujo: espera 1-5 s aleatorios -> sortea 1-100 (si es <= 30 simula fichero con
 * errores y termina con codigo 1) -> lee de la entrada estandar la ruta, el
 * nombre del fichero y el numero de transferencias -> genera el fichero.
 *
 * Parametros del constructor: ninguno (solo tiene main, no se instancia).
 * Entrada estandar (3 lineas): ruta, nombre del fichero, numero de transferencias.
 * Salidas (codigos de salida): 0 correcto, 1 fichero con errores, 2 error de E/S,
 * 3 datos de entrada invalidos, 4 fichero ya existente, 5 interrumpido
 * (ver Constantes).
 */
public class SubApGenFT {

    /**
     * Punto de entrada. Ejecuta el programa y termina con el codigo de salida.
     *
     * @param args no se usan; los datos llegan por la entrada estandar
     */
    public static void main(String[] args) {
        // Salida en UTF-8 para que ApPrinc la interprete bien (tildes, euro...)
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.exit(ejecutar());
    }

    /**
     * Realiza todo el trabajo de la sub-aplicacion.
     *
     * @return el codigo de salida del programa
     */
    private static int ejecutar() {
        Random random = new Random();

        // 1) Espera aleatoria entre 1 y 5 segundos
        try {
            int espera = Constantes.ESPERA_MIN_MS
                    + random.nextInt(Constantes.ESPERA_MAX_MS - Constantes.ESPERA_MIN_MS + 1);
            Thread.sleep(espera);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[SubApGenFT] Interrumpida durante la espera inicial");
            return Constantes.SALIDA_INTERRUMPIDO;
        }

        // 2) Simulacion de fichero generado con errores (30 % de probabilidad)
        int sorteo = 1 + random.nextInt(100); // 1..100
        if (sorteo <= Constantes.UMBRAL_ERROR) {
            System.out.println("[SubApGenFT] Fichero generado con errores (sorteo = " + sorteo + ")");
            return Constantes.SALIDA_FICHERO_CON_ERRORES;
        }

        // 3) Lectura de parametros desde la entrada estandar (UTF-8)
        String ruta;
        String nombre;
        int numTransferencias;
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            ruta = leerLinea(entrada);
            nombre = leerLinea(entrada);
            numTransferencias = Integer.parseInt(leerLinea(entrada).trim());
            if (numTransferencias <= 0) {
                throw new IllegalArgumentException("El numero de transferencias debe ser mayor que 0");
            }
        } catch (IOException | IllegalArgumentException e) { // NumberFormatException es un IllegalArgumentException
            System.out.println("[SubApGenFT] Datos de entrada invalidos: " + e.getMessage());
            return Constantes.SALIDA_DATOS_INVALIDOS;
        }

        // 4) Generacion del fichero (solo si no existe)
        GeneradorTransferencias generador = new GeneradorTransferencias(Paths.get(ruta), nombre, numTransferencias);
        if (generador.ficheroYaExiste()) {
            System.out.println("[SubApGenFT] El fichero '" + nombre + "' ya existe; no se genera");
            return Constantes.SALIDA_FICHERO_YA_EXISTE;
        }
        try {
            generador.generar();
            System.out.println("[SubApGenFT] Fichero '" + nombre + "' generado con " + numTransferencias + " transferencias");
            return Constantes.SALIDA_OK;
        } catch (IOException e) {
            System.out.println("[SubApGenFT] Error de entrada/salida: " + e.getMessage());
            return Constantes.SALIDA_ERROR_ES;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[SubApGenFT] Interrumpida durante la generacion");
            return Constantes.SALIDA_INTERRUMPIDO;
        }
    }

    /**
     * Lee una linea de la entrada y comprueba que existe y no esta vacia.
     *
     * @param entrada lector de la entrada estandar
     * @return la linea leida
     * @throws IOException              si falla la lectura
     * @throws IllegalArgumentException si no hay linea o esta vacia
     */
    private static String leerLinea(BufferedReader entrada) throws IOException {
        String linea = entrada.readLine();
        if (linea == null || linea.isBlank()) {
            throw new IllegalArgumentException("falta un dato en la entrada");
        }
        return linea;
    }
}
