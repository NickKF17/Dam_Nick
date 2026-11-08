package es.ut2.subapprocft;

/**
 * Resultado de procesar una transferencia. Cada transferencia acaba en
 * exactamente uno de los tres ficheros de salida.
 *
 * Parametros del constructor: ninguno (enumerado).
 * Valores: INTERNA, EXTERNA (pagadas) y SIN_SALDO (no habia dinero suficiente).
 */
public enum Resultado {
    INTERNA,
    EXTERNA,
    SIN_SALDO
}
