package es.ut2.subapprocft;

import java.io.IOException;

/**
 * Tarea que ejecuta cada hilo del pool: coge transferencias del gestor compartido
 * hasta que no quedan, las procesa y lleva sus propias cuentas (importe procesado,
 * importe pagado y numero de transferencias internas, externas y sin saldo).
 *
 * Parametros del constructor:
 *   id      -> numero del hilo (0, 1, 2...) usado en los mensajes y el informe
 *   gestor  -> gestor compartido con las transferencias y el saldo
 *
 * Salidas: una linea por transferencia procesada en la salida estandar y los
 * contadores del hilo, que se consultan con los getters al terminar.
 * Los contadores se actualizan y leen con metodos synchronized para que el hilo
 * principal vea siempre los valores definitivos.
 */
public class HiloTransferencias implements Runnable {

    private final int id;
    private final GestorTransferencias gestor;

    private int procesadas = 0;
    private long importeProcesadoCentimos = 0;   // incluye las que no tenian saldo
    private long importePagadoCentimos = 0;      // solo las que se pudieron pagar
    private int internas = 0;
    private int externas = 0;
    private int sinSaldo = 0;
    private boolean fallo = false;

    /**
     * Crea la tarea de un hilo.
     *
     * @param id     identificador del hilo
     * @param gestor gestor compartido entre todos los hilos
     */
    public HiloTransferencias(int id, GestorTransferencias gestor) {
        this.id = id;
        this.gestor = gestor;
    }

    /**
     * Cuerpo del hilo: procesa transferencias hasta que el gestor ya no tiene mas.
     * El retardo de 1 s de las internas se hace FUERA de los metodos synchronized
     * del gestor; si no, se bloquearia a los demas hilos y no habria paralelismo.
     */
    @Override
    public void run() {
        try {
            Transferencia t;
            while ((t = gestor.siguienteTransferencia()) != null) {
                if (t.esInterna()) {
                    Thread.sleep(Constantes.RETARDO_INTERNA_MS); // simula las validaciones del banco
                }
                Resultado resultado = gestor.registrar(t);
                acumular(t, resultado);
                System.out.println("Hilo " + id + " | " + descripcion(resultado) + " | Cuenta " + t.getCuenta()
                        + " | " + Dinero.formatear(t.getImporteCentimos()) + " \u20AC");
            }
        } catch (IOException e) {
            System.out.println("Hilo " + id + " | Error de escritura: " + e.getMessage());
            marcarFallo();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // se respeta la interrupcion y se termina el hilo
            marcarFallo();
        }
    }

    /**
     * Texto que describe el resultado para el mensaje por pantalla.
     *
     * @param r resultado de la transferencia
     * @return descripcion corta
     */
    private String descripcion(Resultado r) {
        switch (r) {
            case INTERNA:
                return "Grabada transferencia interna";
            case EXTERNA:
                return "Grabada transferencia externa";
            default:
                return "SIN SALDO";
        }
    }

    /**
     * Suma una transferencia procesada a los contadores del hilo.
     *
     * @param t         transferencia procesada
     * @param resultado en que fichero se grabo
     */
    private synchronized void acumular(Transferencia t, Resultado resultado) {
        procesadas++;
        importeProcesadoCentimos += t.getImporteCentimos(); // se cuenta aunque no haya saldo
        switch (resultado) {
            case INTERNA:
                internas++;
                importePagadoCentimos += t.getImporteCentimos();
                break;
            case EXTERNA:
                externas++;
                importePagadoCentimos += t.getImporteCentimos();
                break;
            default:
                sinSaldo++;
        }
    }

    /** Marca que este hilo termino por un error. */
    private synchronized void marcarFallo() {
        fallo = true;
    }

    /** @return identificador del hilo */
    public int getId() {
        return id;
    }

    /** @return numero de transferencias procesadas */
    public synchronized int getProcesadas() {
        return procesadas;
    }

    /** @return importe procesado en centimos (incluye las que no tenian saldo) */
    public synchronized long getImporteProcesadoCentimos() {
        return importeProcesadoCentimos;
    }

    /** @return importe realmente pagado en centimos */
    public synchronized long getImportePagadoCentimos() {
        return importePagadoCentimos;
    }

    /** @return numero de transferencias internas procesadas */
    public synchronized int getInternas() {
        return internas;
    }

    /** @return numero de transferencias externas procesadas */
    public synchronized int getExternas() {
        return externas;
    }

    /** @return numero de transferencias sin saldo procesadas */
    public synchronized int getSinSaldo() {
        return sinSaldo;
    }

    /** @return true si el hilo termino por un error */
    public synchronized boolean haFallado() {
        return fallo;
    }
}
