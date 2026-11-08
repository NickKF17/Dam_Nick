package es.ut2.apprinc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Tarea (se ejecuta en un hilo aparte) que lee la salida de SubApProcFT linea a
 * linea. Las lineas normales se muestran al usuario segun llegan; las que llevan
 * el prefijo de informe se pasan a InformeFinal sin mostrarlas. Se lee en un hilo
 * distinto porque ApPrinc esta a la vez esperando a SubApGenFT, y si nadie lee, la
 * tuberia se llena y SubApProcFT se quedaria bloqueada.
 *
 * Parametros del constructor:
 *   flujo   -> salida estandar de SubApProcFT (leida por ApPrinc)
 *   informe -> objeto donde se guardan las lineas de informe
 *
 * Salidas: las lineas de SubApProcFT por pantalla mientras mostrar sea true.
 */
public class LectorSalida implements Runnable {

    private final InputStream flujo;
    private final InformeFinal informe;
    private volatile boolean mostrar = true; // volatile: lo cambia el hilo principal y lo lee este hilo

    /**
     * Crea el lector.
     *
     * @param flujo   flujo de salida de SubApProcFT
     * @param informe objeto que acumula los datos del informe
     */
    public LectorSalida(InputStream flujo, InformeFinal informe) {
        this.flujo = flujo;
        this.informe = informe;
    }

    /**
     * Activa o desactiva que se pinte la salida de SubApProcFT. ApPrinc lo desactiva
     * cuando SubApGenFT falla, para no mostrar nada de un proceso que se va a matar.
     *
     * @param mostrar true para mostrar las lineas, false para descartarlas
     */
    public void setMostrar(boolean mostrar) {
        this.mostrar = mostrar;
    }

    /**
     * Cuerpo del hilo: lee hasta que SubApProcFT cierra su salida (al terminar o ser detenida).
     */
    @Override
    public void run() {
        try (BufferedReader lector = new BufferedReader(new InputStreamReader(flujo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.startsWith(Constantes.PREFIJO_INFORME)) {
                    informe.procesarLinea(linea); // datos para el informe: no se pintan tal cual
                } else if (mostrar) {
                    System.out.println(linea);
                }
            }
        } catch (IOException e) {
            // La salida se cierra al detener SubApProcFT: es una situacion normal, no un error
        }
    }
}
