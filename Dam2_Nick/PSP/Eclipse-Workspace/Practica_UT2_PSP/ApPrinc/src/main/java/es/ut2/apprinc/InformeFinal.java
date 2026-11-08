package es.ut2.apprinc;

import java.util.ArrayList;
import java.util.List;

/**
 * Recoge las lineas de informe que envia SubApProcFT (con el prefijo
 * PREFIJO_INFORME) y, con ellas, calcula e imprime el informe final: totales
 * y datos de cada hilo.
 *
 * Formato de las lineas que interpreta:
 *   PREFIJO;HILO;id;procesadas;importeProcesado;importePagado;internas;externas;sinSaldo
 *   PREFIJO;SALDO;saldoInicial;saldoFinal      (importes en centimos)
 *
 * Parametros del constructor: ninguno.
 * Salidas: el informe por la salida estandar al llamar a imprimir().
 */
public class InformeFinal {

    /** Datos de un hilo (record interno: clase inmutable). */
    private record DatosHilo(int id, int procesadas, long importeProcesado, long importePagado,
                             int internas, int externas, int sinSaldo) {
    }

    private final List<DatosHilo> hilos = new ArrayList<>();
    private long saldoInicial = 0;
    private long saldoFinal = 0;

    /**
     * Interpreta una linea de informe y guarda sus datos. Las lineas mal formadas se ignoran.
     *
     * @param linea linea recibida de SubApProcFT (empieza por el prefijo)
     */
    public synchronized void procesarLinea(String linea) {
        String[] c = linea.split(";");
        try {
            if (c[1].equals("HILO")) {
                hilos.add(new DatosHilo(Integer.parseInt(c[2]), Integer.parseInt(c[3]), Long.parseLong(c[4]),
                        Long.parseLong(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7]), Integer.parseInt(c[8])));
            } else if (c[1].equals("SALDO")) {
                saldoInicial = Long.parseLong(c[2]);
                saldoFinal = Long.parseLong(c[3]);
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("[ApPrinc] Linea de informe ignorada (formato incorrecto): " + linea);
        }
    }

    /**
     * Calcula los totales y muestra el informe final: totales generales y despues
     * los datos de cada hilo.
     */
    public synchronized void imprimir() {
        int procesadas = 0;
        long importeProcesado = 0;
        long importePagado = 0;
        int internas = 0;
        int externas = 0;
        int sinSaldo = 0;
        for (DatosHilo h : hilos) {
            procesadas += h.procesadas();
            importeProcesado += h.importeProcesado();
            importePagado += h.importePagado();
            internas += h.internas();
            externas += h.externas();
            sinSaldo += h.sinSaldo();
        }

        System.out.println();
        System.out.println("================ INFORME FINAL ================");
        System.out.println("Saldo inicial de la cuenta de la empresa: " + Dinero.formatear(saldoInicial) + " \u20AC");
        System.out.println("Saldo final de la cuenta de la empresa:   " + Dinero.formatear(saldoFinal) + " \u20AC");
        System.out.println("Total de transferencias procesadas:       " + procesadas);
        System.out.println("Cantidad total de dinero procesado:       " + Dinero.formatear(importeProcesado) + " \u20AC");
        System.out.println("Cantidad total de dinero pagado:          " + Dinero.formatear(importePagado) + " \u20AC");
        System.out.println("Transferencias internas procesadas:       " + internas);
        System.out.println("Transferencias externas procesadas:       " + externas);
        System.out.println("Transferencias sin saldo procesadas:      " + sinSaldo);
        System.out.println();
        System.out.println("--- Detalle por hilo ---");
        for (DatosHilo h : hilos) {
            System.out.println("Hilo " + h.id() + ":");
            System.out.println("   Transferencias procesadas:  " + h.procesadas());
            System.out.println("   Dinero procesado:           " + Dinero.formatear(h.importeProcesado()) + " \u20AC");
            System.out.println("   Dinero pagado:              " + Dinero.formatear(h.importePagado()) + " \u20AC");
            System.out.println("   Internas procesadas:        " + h.internas());
            System.out.println("   Externas procesadas:        " + h.externas());
            System.out.println("   Sin saldo procesadas:       " + h.sinSaldo());
        }
        System.out.println("===============================================");
    }
}
