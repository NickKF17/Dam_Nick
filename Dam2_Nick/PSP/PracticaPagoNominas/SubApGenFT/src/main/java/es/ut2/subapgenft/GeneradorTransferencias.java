package es.ut2.subapgenft;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

/**
 * Genera el fichero de transferencias (una transferencia por linea con el
 * formato "cuenta;importe") usando un fichero de bloqueo para que la
 * SubApProcFT no lo lea antes de que este completo.
 *
 * Parametros del constructor:
 *   ruta               -> directorio donde se crea el fichero (y el de bloqueo)
 *   nombre             -> nombre del fichero de transferencias
 *   numTransferencias  -> cuantas lineas (transferencias) se generan
 *
 * Salidas: el fichero de transferencias y, mientras se escribe, el fichero de
 * bloqueo. Los errores de E/S se propagan como IOException a quien llama.
 */
public class GeneradorTransferencias {

    private final Path ruta;
    private final Path fichero;
    private final Path bloqueo;
    private final int numTransferencias;
    private final Random random = new Random();

    /**
     * Crea el generador.
     *
     * @param ruta              directorio de trabajo
     * @param nombre            nombre del fichero de transferencias
     * @param numTransferencias numero de transferencias a generar
     */
    public GeneradorTransferencias(Path ruta, String nombre, int numTransferencias) {
        this.ruta = ruta;
        this.fichero = ruta.resolve(nombre);
        this.bloqueo = ruta.resolve(Constantes.FICHERO_BLOQUEO);
        this.numTransferencias = numTransferencias;
    }

    /**
     * Comprueba si el fichero de transferencias ya existe (solo se genera si no existe).
     *
     * @return true si el fichero ya existe
     */
    public boolean ficheroYaExiste() {
        return Files.exists(fichero);
    }

    /**
     * Genera el fichero de transferencias. Orden: crea el bloqueo, escribe el
     * fichero y, por ultimo (siempre, aunque haya error), elimina el bloqueo.
     * Si falla a mitad, borra el fichero incompleto para no dejar datos a medias.
     *
     * @throws IOException          si hay un error de entrada/salida
     * @throws InterruptedException si se interrumpe durante la pausa entre transferencias
     */
    public void generar() throws IOException, InterruptedException {
        Files.createDirectories(ruta);
        // 1) Bloqueo ANTES de crear el fichero. Si ya existe un bloqueo ajeno,
        //    createFile lanza excepcion y no llegamos al try (no borramos un bloqueo que no es nuestro)
        Files.createFile(bloqueo);
        try {
            // 2) CREATE_NEW: falla si el fichero existe, asi nunca se pisa uno anterior
            try (BufferedWriter escritor = Files.newBufferedWriter(fichero, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
                for (int i = 0; i < numTransferencias; i++) {
                    escritor.write(generarLinea());
                    escritor.newLine();
                    Thread.sleep(Constantes.PAUSA_ENTRE_TRANSFERENCIAS_MS); // 10 ms entre transferencias
                }
            }
        } catch (IOException | InterruptedException e) {
            Files.deleteIfExists(fichero); // el escritor ya esta cerrado: no dejamos un fichero a medias
            throw e;
        } finally {
            // 3) El bloqueo se elimina siempre al terminar
            Files.deleteIfExists(bloqueo);
        }
    }

    /**
     * Genera una linea "cuenta;importe". La cuenta tiene 9 digitos: el primero es
     * 1 (interna) o 2 (externa) y los 8 restantes van de 10000000 a 99999999.
     * El importe va de 1500.00 a 3000.00 con 2 decimales (punto decimal).
     *
     * @return la linea de transferencia, por ejemplo "262198823;2755.23"
     */
    private String generarLinea() {
        int primerDigito = 1 + random.nextInt(2);            // 1 o 2
        int resto = 10_000_000 + random.nextInt(90_000_000); // 10000000..99999999
        long cuenta = primerDigito * 100_000_000L + resto;

        // Se trabaja en centimos (enteros) para no arrastrar errores de decimales
        int rango = Constantes.IMPORTE_MAX_CENTIMOS - Constantes.IMPORTE_MIN_CENTIMOS + 1;
        int centimos = Constantes.IMPORTE_MIN_CENTIMOS + random.nextInt(rango);
        String importe = (centimos / 100) + "." + String.format("%02d", centimos % 100);

        return cuenta + ";" + importe;
    }
}
