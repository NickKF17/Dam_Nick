package es.ut2.subapgenft;

/**
 * Constantes de la SubApGenFT: nombre del fichero de bloqueo, parametros de la
 * simulacion y codigos de salida del programa.
 *
 * Parametros del constructor: ninguno (clase de utilidad, no se instancia).
 *
 * Salidas que produce el programa (codigos de salida de SubApGenFT):
 *   0 -> termino correctamente, fichero de transferencias generado
 *   1 -> el fichero "se genero con errores" (aleatorio entre 1 y 30)
 *   2 -> error de entrada/salida al crear el fichero
 *   3 -> datos de entrada invalidos (faltan lineas, no es numero, valor <= 0...)
 *   4 -> el fichero de transferencias ya existia (solo se genera si no existe)
 *   5 -> el programa fue interrumpido mientras esperaba
 */
public final class Constantes {

    /** Nombre del fichero de bloqueo (debe ser el mismo que usa SubApProcFT). */
    public static final String FICHERO_BLOQUEO = "transferencias.bloqueo";

    // Codigos de salida (ver comentario de la clase)
    public static final int SALIDA_OK = 0;
    public static final int SALIDA_FICHERO_CON_ERRORES = 1;
    public static final int SALIDA_ERROR_ES = 2;
    public static final int SALIDA_DATOS_INVALIDOS = 3;
    public static final int SALIDA_FICHERO_YA_EXISTE = 4;
    public static final int SALIDA_INTERRUMPIDO = 5;

    /** Espera inicial aleatoria entre 1 y 5 segundos (en milisegundos). */
    public static final int ESPERA_MIN_MS = 1000;
    public static final int ESPERA_MAX_MS = 5000;

    /** Si el aleatorio (1-100) es menor o igual a este valor, se simula el error. */
    public static final int UMBRAL_ERROR = 30;

    /** Pausa entre la generacion de una transferencia y la siguiente. */
    public static final int PAUSA_ENTRE_TRANSFERENCIAS_MS = 10;

    /** Importe de la nomina entre 1500.00 y 3000.00 euros, guardado en centimos. */
    public static final int IMPORTE_MIN_CENTIMOS = 150000;
    public static final int IMPORTE_MAX_CENTIMOS = 300000;

    /** Constructor privado: no se pueden crear objetos de esta clase. */
    private Constantes() {
    }
}
