package es.ut2.apprinc;

/**
 * Constantes de ApPrinc: valores por defecto, nombres de ficheros, rutas de los
 * programas que lanza y codigos de salida.
 *
 * Parametros del constructor: ninguno (clase de utilidad, no se instancia).
 *
 * Salidas que produce el programa (codigos de salida de ApPrinc):
 *   0 -> termino correctamente
 *   1 -> fallo el proceso de procesamiento del fichero de transferencias (SubApProcFT)
 *   2 -> fallo el proceso de generacion del fichero de transferencias (SubApGenFT)
 *   3 -> error propio de ApPrinc (no se pudo leer la entrada, preparar la ruta o lanzar un programa)
 */
public final class Constantes {

    // Valores por defecto si el usuario solo pulsa enter
    public static final String NOMBRE_POR_DEFECTO = "transfer_data";
    public static final String RUTA_POR_DEFECTO = "./home/pruebas/";

    // Ficheros que pueden quedar de ejecuciones anteriores y se limpian al empezar
    public static final String FICHERO_BLOQUEO = "transferencias.bloqueo";
    public static final String FICHERO_INTERNAS = "transferencias_internas";
    public static final String FICHERO_EXTERNAS = "transferencias_externas";
    public static final String FICHERO_SIN_SALDO = "transferencias_sin_saldo";

    /**
     * Ubicacion de los jar de las sub-aplicaciones, relativa al directorio desde el que
     * se ejecuta ApPrinc (la carpeta del proyecto ApPrinc). Se pueden cambiar pasando
     * 2 argumentos al programa: ruta del jar de SubApProcFT y ruta del jar de SubApGenFT.
     */
    public static final String JAR_PROCESAR = "../SubApProcFT/target/SubApProcFT.jar";
    public static final String JAR_GENERAR = "../SubApGenFT/target/SubApGenFT.jar";

    /** Prefijo de las lineas de informe que envia SubApProcFT (no se muestran tal cual). */
    public static final String PREFIJO_INFORME = "@@INFORME@@";

    /** Tiempo que se espera tras lanzar SubApProcFT para comprobar que no ha fallado al arrancar. */
    public static final int ESPERA_ARRANQUE_MS = 500;

    // Codigos de salida (ver comentario de la clase)
    public static final int SALIDA_OK = 0;
    public static final int SALIDA_FALLO_PROCESAR = 1;
    public static final int SALIDA_FALLO_GENERAR = 2;
    public static final int SALIDA_ERROR_PROPIO = 3;

    /** Constructor privado: no se pueden crear objetos de esta clase. */
    private Constantes() {
    }
}
