package es.ut2.subapprocft;

/**
 * Constantes de la SubApProcFT: nombres de ficheros, tiempos, rango del saldo,
 * prefijo de las lineas del informe y codigos de salida.
 *
 * Parametros del constructor: ninguno (clase de utilidad, no se instancia).
 *
 * Salidas que produce el programa (codigos de salida de SubApProcFT):
 *   0 -> termino correctamente
 *   1 -> datos de entrada invalidos
 *   2 -> error de entrada/salida (leer el fichero de transferencias o escribir los de salida)
 *   3 -> el fichero de transferencias es incorrecto (linea mal formada o numero de lineas distinto del esperado)
 *   4 -> el programa fue interrumpido
 */
public final class Constantes {

    // Nombres de los ficheros (constantes de la aplicacion segun el enunciado)
    public static final String FICHERO_INTERNAS = "transferencias_internas";
    public static final String FICHERO_EXTERNAS = "transferencias_externas";
    public static final String FICHERO_SIN_SALDO = "transferencias_sin_saldo";
    public static final String FICHERO_BLOQUEO = "transferencias.bloqueo";

    // Codigos de salida (ver comentario de la clase)
    public static final int SALIDA_OK = 0;
    public static final int SALIDA_DATOS_INVALIDOS = 1;
    public static final int SALIDA_ERROR_ES = 2;
    public static final int SALIDA_FICHERO_INCORRECTO = 3;
    public static final int SALIDA_INTERRUMPIDO = 4;

    /** Cada cuanto se vuelve a comprobar si ya existe el fichero de transferencias. */
    public static final int ESPERA_COMPROBACION_MS = 1000;

    /** Retardo que simula las validaciones del banco en una transferencia interna. */
    public static final int RETARDO_INTERNA_MS = 1000;

    /** El saldo inicial es aleatorio(1500.00, 2700.00) x numero de transferencias (en centimos). */
    public static final int SALDO_MIN_CENTIMOS = 150000;
    public static final int SALDO_MAX_CENTIMOS = 270000;

    /** Prefijo de las lineas que SubApProcFT envia a ApPrinc con los datos del informe. */
    public static final String PREFIJO_INFORME = "@@INFORME@@";

    /** Constructor privado: no se pueden crear objetos de esta clase. */
    private Constantes() {
    }
}
