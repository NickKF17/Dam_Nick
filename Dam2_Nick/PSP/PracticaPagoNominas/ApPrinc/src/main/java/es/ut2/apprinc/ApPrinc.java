package es.ut2.apprinc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * ApPrinc - Programa principal. Pide los datos al usuario, lanza la SubApProcFT
 * (que se queda esperando el fichero), despues lanza la SubApGenFT (que lo crea)
 * y, si todo ha ido bien, muestra el informe final.
 *
 * Los datos se envian a cada sub-aplicacion por su flujo de entrada (stdin) y
 * se controla el codigo de salida de ambas: si SubApGenFT falla, se avisa al
 * usuario, se detiene a SubApProcFT y no se muestra el informe.
 *
 * Parametros del constructor: ninguno (solo tiene main, no se instancia).
 * Argumentos opcionales de main: [0] ruta del jar de SubApProcFT y [1] ruta del
 * jar de SubApGenFT (por defecto, las de Constantes).
 * Salidas (codigos de salida): 0 correcto, 1 fallo SubApProcFT, 2 fallo SubApGenFT,
 * 3 error propio de ApPrinc (ver Constantes).
 */
public class ApPrinc {

    /**
     * Punto de entrada. Ejecuta el programa y termina con el codigo de salida.
     *
     * @param args opcionales: jar de SubApProcFT y jar de SubApGenFT
     */
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.exit(ejecutar(args));
    }

    /**
     * Realiza todo el trabajo del programa principal (ver la linea temporal del enunciado).
     *
     * @param args argumentos de main (rutas opcionales de los jar)
     * @return el codigo de salida de ApPrinc
     */
    private static int ejecutar(String[] args) {
        String jarProcesar = args.length > 0 ? args[0] : Constantes.JAR_PROCESAR;
        String jarGenerar = args.length > 1 ? args[1] : Constantes.JAR_GENERAR;

        // 1) Datos del usuario
        DatosEntrada datos;
        try {
            datos = new EntradaUsuario(System.in).pedirDatos();
        } catch (IllegalStateException e) {
            System.out.println("[ApPrinc] No se pudieron leer los datos: " + e.getMessage());
            return Constantes.SALIDA_ERROR_PROPIO;
        }

        // 2) Preparar la ruta: se borran el fichero de transferencias y restos de ejecuciones anteriores
        try {
            prepararRuta(datos);
        } catch (IOException e) {
            System.out.println("[ApPrinc] No se pudo preparar la ruta '" + datos.ruta() + "': " + e.getMessage());
            return Constantes.SALIDA_ERROR_PROPIO;
        }

        Process procesar = null;
        try {
            // 3) Se lanza PRIMERO SubApProcFT (se queda esperando a que exista el fichero)
            System.out.println();
            System.out.println("[ApPrinc] Lanzando SubApProcFT...");
            procesar = lanzar(jarProcesar, true);
            enviarDatos(procesar, datos.ruta(), datos.nombre(),
                    String.valueOf(datos.numTransferencias()), String.valueOf(datos.numHilos()));

            // Hilo que lee y muestra la salida de SubApProcFT mientras se ejecuta SubApGenFT
            InformeFinal informe = new InformeFinal();
            LectorSalida lectorSalida = new LectorSalida(procesar.getInputStream(), informe);
            Thread hiloLector = new Thread(lectorSalida);
            hiloLector.start();

            // Si SubApProcFT ya ha terminado, ha fallado al arrancar: no se lanza SubApGenFT
            if (procesar.waitFor(Constantes.ESPERA_ARRANQUE_MS, TimeUnit.MILLISECONDS)) {
                hiloLector.join();
                System.out.println("[ApPrinc] SubApProcFT ha fallado al arrancar. Valor de salida: " + procesar.exitValue());
                return Constantes.SALIDA_FALLO_PROCESAR;
            }

            // 4) Se lanza SubApGenFT (hereda la pantalla; solo se le envia la entrada)
            System.out.println("[ApPrinc] Lanzando SubApGenFT...");
            Process generar = lanzar(jarGenerar, false);
            try {
                enviarDatos(generar, datos.ruta(), datos.nombre(), String.valueOf(datos.numTransferencias()));
            } catch (IOException e) {
                // Si SubApGenFT ya habia terminado (p. ej. por el error simulado) la tuberia esta cerrada;
                // no pasa nada: a continuacion se lee su codigo de salida, que es lo que decide
            }
            int salidaGenerar = generar.waitFor();
            System.out.println("[ApPrinc] SubApGenFT ha terminado. Valor de salida: " + salidaGenerar);

            // 5) Si SubApGenFT ha fallado: se avisa, se detiene a SubApProcFT y no se pinta mas salida suya
            if (salidaGenerar != 0) {
                lectorSalida.setMostrar(false);
                procesar.destroyForcibly(); // "matar" a SubApProcFT
                procesar.waitFor();
                hiloLector.join();
                System.out.println("[ApPrinc] ERROR: " + explicarSalidaGenerar(salidaGenerar));
                System.out.println("[ApPrinc] Se ha detenido SubApProcFT. No hay informe que mostrar.");
                return Constantes.SALIDA_FALLO_GENERAR;
            }

            // 6) SubApGenFT ha ido bien: se espera a que SubApProcFT termine de procesar
            int salidaProcesar = procesar.waitFor();
            hiloLector.join(); // se asegura de haber leido TODA su salida antes de seguir
            System.out.println("[ApPrinc] SubApProcFT ha terminado. Valor de salida: " + salidaProcesar);
            if (salidaProcesar != 0) {
                System.out.println("[ApPrinc] ERROR: fallo el procesamiento del fichero de transferencias.");
                return Constantes.SALIDA_FALLO_PROCESAR;
            }

            // 7) Informe final
            informe.imprimir();
            return Constantes.SALIDA_OK;

        } catch (IOException e) {
            System.out.println("[ApPrinc] No se pudo lanzar un programa: " + e.getMessage());
            detener(procesar);
            return Constantes.SALIDA_ERROR_PROPIO;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[ApPrinc] Interrumpido");
            detener(procesar);
            return Constantes.SALIDA_ERROR_PROPIO;
        }
    }

    /**
     * Crea la ruta si no existe y borra el fichero de transferencias (si existe), el
     * de bloqueo y los tres de salida, por si quedaron de una ejecucion anterior.
     * Un bloqueo antiguo haria que SubApProcFT esperase para siempre.
     *
     * @param datos datos introducidos por el usuario
     * @throws IOException si no se puede crear la ruta o borrar algun fichero
     */
    private static void prepararRuta(DatosEntrada datos) throws IOException {
        Path ruta = Paths.get(datos.ruta());
        Files.createDirectories(ruta);
        Files.deleteIfExists(ruta.resolve(datos.nombre()));
        Files.deleteIfExists(ruta.resolve(Constantes.FICHERO_BLOQUEO));
        Files.deleteIfExists(ruta.resolve(Constantes.FICHERO_INTERNAS));
        Files.deleteIfExists(ruta.resolve(Constantes.FICHERO_EXTERNAS));
        Files.deleteIfExists(ruta.resolve(Constantes.FICHERO_SIN_SALDO));
    }

    /**
     * Lanza un programa Java empaquetado en un jar como proceso hijo, usando el mismo
     * java con el que corre ApPrinc. Su entrada estandar queda disponible para enviarle datos.
     *
     * @param jar              ruta del jar a ejecutar
     * @param capturarSalida   true: la salida (y los errores) se leen desde ApPrinc;
     *                         false: la salida va directamente a la pantalla
     * @return el proceso lanzado
     * @throws IOException si no se puede arrancar el proceso
     */
    private static Process lanzar(String jar, boolean capturarSalida) throws IOException {
        String java = Paths.get(System.getProperty("java.home"), "bin", "java").toString();
        ProcessBuilder pb = new ProcessBuilder(java, "-jar", jar);
        if (capturarSalida) {
            pb.redirectErrorStream(true); // errores y salida por el mismo flujo
        } else {
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        }
        return pb.start();
    }

    /**
     * Envia datos a un proceso hijo por su entrada estandar, una linea por dato, en
     * UTF-8 (asi las tildes y dieresis llegan bien), y cierra el flujo.
     *
     * @param proceso proceso hijo
     * @param lineas  datos a enviar, en el orden en que los espera el programa
     * @throws IOException si el proceso ya no puede recibir datos
     */
    private static void enviarDatos(Process proceso, String... lineas) throws IOException {
        try (BufferedWriter escritor = new BufferedWriter(
                new OutputStreamWriter(proceso.getOutputStream(), StandardCharsets.UTF_8))) {
            for (String linea : lineas) {
                escritor.write(linea);
                escritor.newLine();
            }
        }
    }

    /**
     * Detiene un proceso hijo si sigue vivo (se usa cuando ApPrinc acaba por un error).
     *
     * @param proceso proceso a detener (puede ser null)
     */
    private static void detener(Process proceso) {
        if (proceso != null && proceso.isAlive()) {
            proceso.destroyForcibly();
        }
    }

    /**
     * Traduce el codigo de salida de SubApGenFT a un mensaje para el usuario.
     *
     * @param codigo codigo de salida de SubApGenFT
     * @return explicacion del fallo
     */
    private static String explicarSalidaGenerar(int codigo) {
        switch (codigo) {
            case 1:
                return "SubApGenFT genero el fichero de transferencias con errores (codigo 1).";
            case 2:
                return "SubApGenFT tuvo un error de entrada/salida al crear el fichero (codigo 2).";
            case 3:
                return "SubApGenFT recibio datos de entrada invalidos (codigo 3).";
            case 4:
                return "El fichero de transferencias ya existia (codigo 4).";
            case 5:
                return "SubApGenFT fue interrumpida (codigo 5).";
            default:
                return "SubApGenFT termino con un codigo inesperado: " + codigo + ".";
        }
    }
}
