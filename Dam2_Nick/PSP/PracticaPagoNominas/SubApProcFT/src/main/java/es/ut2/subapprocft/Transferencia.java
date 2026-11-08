package es.ut2.subapprocft;

import java.math.BigDecimal;

/**
 * Una transferencia leida del fichero: cuenta destino e importe. Es inmutable,
 * asi que se puede compartir entre hilos sin sincronizar.
 *
 * Parametros del constructor:
 *   cuenta           -> numero de cuenta de 9 digitos (empieza por 1 o 2)
 *   importeCentimos  -> importe en centimos
 *
 * Salidas: getters y la linea "cuenta;importe" para volver a escribirla en un fichero.
 */
public class Transferencia {

    private final String cuenta;
    private final long importeCentimos;

    /**
     * Crea una transferencia.
     *
     * @param cuenta          cuenta destino (9 digitos, primer digito 1 o 2)
     * @param importeCentimos importe en centimos
     */
    public Transferencia(String cuenta, long importeCentimos) {
        this.cuenta = cuenta;
        this.importeCentimos = importeCentimos;
    }

    /**
     * Interpreta una linea del fichero con formato "cuenta;importe".
     *
     * @param linea linea del fichero, por ejemplo "262198823;2755.23"
     * @return la transferencia correspondiente
     * @throws IllegalArgumentException si la linea esta mal formada
     */
    public static Transferencia desdeLinea(String linea) {
        String[] partes = linea.split(";");
        if (partes.length != 2) {
            throw new IllegalArgumentException("se esperaban 2 campos separados por ';': " + linea);
        }
        String cuenta = partes[0].trim();
        if (!cuenta.matches("[12][0-9]{8}")) {
            throw new IllegalArgumentException("cuenta no valida: " + cuenta);
        }
        long centimos;
        try {
            // movePointRight(2) pasa euros a centimos; longValueExact falla si hay mas de 2 decimales
            centimos = new BigDecimal(partes[1].trim()).movePointRight(2).longValueExact();
        } catch (NumberFormatException | ArithmeticException e) {
            throw new IllegalArgumentException("importe no valido: " + partes[1]);
        }
        if (centimos <= 0) {
            throw new IllegalArgumentException("el importe debe ser positivo: " + partes[1]);
        }
        return new Transferencia(cuenta, centimos);
    }

    /**
     * Indica si es interna (cuenta que empieza por 1) o externa (empieza por 2).
     *
     * @return true si es interna
     */
    public boolean esInterna() {
        return cuenta.charAt(0) == '1';
    }

    /** @return la cuenta destino */
    public String getCuenta() {
        return cuenta;
    }

    /** @return el importe en centimos */
    public long getImporteCentimos() {
        return importeCentimos;
    }

    /**
     * Devuelve la transferencia con el mismo formato del fichero original.
     *
     * @return la linea "cuenta;importe"
     */
    public String comoLinea() {
        return cuenta + ";" + Dinero.formatear(importeCentimos);
    }
}
