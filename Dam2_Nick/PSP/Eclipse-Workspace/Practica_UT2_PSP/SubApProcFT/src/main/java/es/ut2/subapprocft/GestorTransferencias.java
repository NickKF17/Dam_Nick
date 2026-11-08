package es.ut2.subapprocft;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Clase COMPARTIDA por todos los hilos. Guarda las transferencias pendientes, el
 * saldo de la cuenta de la empresa y los tres ficheros de salida. Todo el acceso
 * a estos datos compartidos se hace con metodos synchronized, de forma que:
 *  - cada transferencia la coge un unico hilo (se procesa una sola vez),
 *  - la comprobacion y la resta del saldo son una operacion indivisible,
 *  - las escrituras en los ficheros no se mezclan.
 *
 * Parametros del constructor:
 *   transferencias -> lista con todas las transferencias leidas del fichero
 *   saldoCentimos  -> saldo inicial de la cuenta de la empresa (en centimos)
 *   ruta           -> directorio donde se crean los 3 ficheros de salida
 *
 * Salidas: los ficheros transferencias_internas, transferencias_externas y
 * transferencias_sin_saldo (se sobrescriben si ya existian).
 */
public class GestorTransferencias {

    private final List<Transferencia> transferencias;
    private int siguiente = 0;     // indice de la proxima transferencia sin procesar
    private long saldoCentimos;    // saldo actual de la cuenta de la empresa

    private final BufferedWriter ficheroInternas;
    private final BufferedWriter ficheroExternas;
    private final BufferedWriter ficheroSinSaldo;

    /**
     * Crea el gestor y abre los tres ficheros de salida.
     *
     * @param transferencias lista de transferencias a procesar
     * @param saldoCentimos  saldo inicial en centimos
     * @param ruta           directorio de los ficheros de salida
     * @throws IOException si no se pueden crear los ficheros
     */
    public GestorTransferencias(List<Transferencia> transferencias, long saldoCentimos, Path ruta) throws IOException {
        this.transferencias = transferencias;
        this.saldoCentimos = saldoCentimos;
        this.ficheroInternas = Files.newBufferedWriter(ruta.resolve(Constantes.FICHERO_INTERNAS), StandardCharsets.UTF_8);
        this.ficheroExternas = Files.newBufferedWriter(ruta.resolve(Constantes.FICHERO_EXTERNAS), StandardCharsets.UTF_8);
        this.ficheroSinSaldo = Files.newBufferedWriter(ruta.resolve(Constantes.FICHERO_SIN_SALDO), StandardCharsets.UTF_8);
    }

    /**
     * Entrega la siguiente transferencia sin procesar. Es synchronized para que dos
     * hilos nunca reciban la misma.
     *
     * @return la siguiente transferencia, o null si ya no quedan
     */
    public synchronized Transferencia siguienteTransferencia() {
        if (siguiente >= transferencias.size()) {
            return null;
        }
        Transferencia t = transferencias.get(siguiente);
        siguiente++;
        return t;
    }

    /**
     * Intenta pagar la transferencia y la graba en el fichero que corresponda.
     * Si hay saldo: lo resta y la graba como interna o externa. Si no: la graba
     * en el fichero de sin saldo. Es synchronized: la comprobacion del saldo y su
     * resta no pueden ser interrumpidas por otro hilo.
     *
     * @param t transferencia a registrar
     * @return en que fichero se ha grabado (INTERNA, EXTERNA o SIN_SALDO)
     * @throws IOException si falla la escritura
     */
    public synchronized Resultado registrar(Transferencia t) throws IOException {
        if (t.getImporteCentimos() > saldoCentimos) {
            escribir(ficheroSinSaldo, t);
            return Resultado.SIN_SALDO;
        }
        saldoCentimos -= t.getImporteCentimos(); // se actualiza el saldo de la empresa
        if (t.esInterna()) {
            escribir(ficheroInternas, t);
            return Resultado.INTERNA;
        }
        escribir(ficheroExternas, t);
        return Resultado.EXTERNA;
    }

    /**
     * Devuelve el saldo actual de la cuenta de la empresa.
     *
     * @return saldo en centimos
     */
    public synchronized long getSaldoCentimos() {
        return saldoCentimos;
    }

    /**
     * Cierra los tres ficheros de salida. Se llama cuando todos los hilos han terminado.
     *
     * @throws IOException si falla el cierre de algun fichero
     */
    public synchronized void cerrar() throws IOException {
        try (ficheroInternas; ficheroExternas; ficheroSinSaldo) {
            // try-with-resources cierra los tres aunque falle alguno
        }
    }

    /**
     * Escribe una transferencia (una linea) en el fichero indicado.
     *
     * @param fichero fichero de destino
     * @param t       transferencia a escribir
     * @throws IOException si falla la escritura
     */
    private void escribir(BufferedWriter fichero, Transferencia t) throws IOException {
        fichero.write(t.comoLinea());
        fichero.newLine();
    }
}
