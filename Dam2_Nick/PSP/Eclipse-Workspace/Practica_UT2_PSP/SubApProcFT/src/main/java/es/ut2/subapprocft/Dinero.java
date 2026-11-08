package es.ut2.subapprocft;

/**
 * Utilidad para trabajar con dinero. Todos los importes se guardan como numero
 * entero de centimos (long) para evitar los errores de redondeo de double
 * (por ejemplo 214356.27000000002 en la ejecucion de ejemplo del enunciado).
 *
 * Parametros del constructor: ninguno (clase de utilidad, no se instancia).
 * Salidas: cadenas con formato "1234.56" (punto decimal, 2 decimales).
 */
public final class Dinero {

    /** Constructor privado: no se pueden crear objetos de esta clase. */
    private Dinero() {
    }

    /**
     * Convierte un importe en centimos a texto con 2 decimales.
     *
     * @param centimos importe en centimos (no negativo)
     * @return el importe como texto, por ejemplo 275523 -> "2755.23"
     */
    public static String formatear(long centimos) {
        return (centimos / 100) + "." + String.format("%02d", centimos % 100);
    }
}
