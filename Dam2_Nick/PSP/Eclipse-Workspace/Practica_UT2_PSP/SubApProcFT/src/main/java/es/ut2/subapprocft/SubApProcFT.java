package es.ut2.subapprocft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SubApProcFT - Sub-aplicacion de Procesamiento del Fichero de Transferencias.
 * Simula el programa del banco que espera el fichero de transferencias y lo
 * procesa en paralelo con un pool de hilos (ExecutorService).
 *
 * Flujo: lee los parametros -> espera (sin limite) a que exista el fichero de
 * transferencias y NO exista el de bloqueo -> lo carga en memoria -> calcula el
 * saldo inicial aleatorio -> lanza los hilos -> espera a que acaben -> cierra
 * los ficheros -> imprime el resumen y las lineas de informe para ApPrinc.
 *
 * Parametros del constructor: ninguno (solo tiene main, no se instancia).
 * Entrada estandar (4 lineas): ruta, nombre del fichero, numero de transferencias
 * esperadas y numero de hilos.
 * Salidas (codigos de salida): 0 correcto, 1 datos invalidos, 2 error de E/S,
 * 3 fichero de transferencias incorrecto, 4 interrumpido (ver Constantes).
 * Ficheros generados: transferencias_internas, transferencias_externas y
 * transferencias_sin_saldo, en la ruta indicada.
 */
public class SubApProcFT {

    /**
     * Punto de entrada. Ejecuta el programa y termina con el codigo de salida.
     *
     * @param args no se usan; los datos llegan por la entrada estandar
     */
    public static void main(String[] args) {
        // Salida en UTF-8 para que ApPrinc la interprete bien (tildes, euro...)
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.exit(ejecutar());
    }

    /**
     * Realiza todo el trabajo de la sub-aplicacion.
     *
     * @return el codigo de salida del programa
     */
    private static int ejecutar() {
        // 1) Parametros desde la entrada estandar (UTF-8)
        String ruta;
        String nombre;
        int numTransferencias;
        int numHilos;
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            ruta = leerLinea(entrada);
            nombre = leerLinea(entrada);
            numTransferencias = Integer.parseInt(leerLinea(entrada).trim());
            numHilos = Integer.parseInt(leerLinea(entrada).trim());
            if (numTransferencias <= 0 || numHilos <= 0) {
                throw new IllegalArgumentException("transferencias e hilos deben ser mayores que 0");
            }
        } catch (IOException | IllegalArgumentException e) { // NumberFormatException es un IllegalArgumentException
            System.out.println("[SubApProcFT] Datos de entrada invalidos: " + e.getMessage());
            return Constantes.SALIDA_DATOS_INVALIDOS;
        }

        Path directorio = Paths.get(ruta);
        Path fichero = directorio.resolve(nombre);
        Path bloqueo = directorio.resolve(Constantes.FICHERO_BLOQUEO);

        try {
            // 2) Espera indefinida hasta que el fichero este completamente generado
            System.out.println("[SubApProcFT] Esperando el fichero de transferencias '" + nombre + "'...");
            esperarFichero(fichero, bloqueo);
            System.out.println("[SubApProcFT] Fichero disponible. Se empieza a procesar con " + numHilos + " hilos");

            // 3) Carga del fichero en memoria
            List<Transferencia> transferencias = leerTransferencias(fichero);
            if (transferencias.size() != numTransferencias) {
                System.out.println("[SubApProcFT] El fichero tiene " + transferencias.size()
                        + " transferencias y se esperaban " + numTransferencias);
                return Constantes.SALIDA_FICHERO_INCORRECTO;
            }

            // 4) Saldo inicial = aleatorio(1500, 2700) con 2 decimales x numero de transferencias
            long importeTotal = 0;
            for (Transferencia t : transferencias) {
                importeTotal += t.getImporteCentimos();
            }
            Random random = new Random();
            int rango = Constantes.SALDO_MAX_CENTIMOS - Constantes.SALDO_MIN_CENTIMOS + 1;
            long saldoInicial = (Constantes.SALDO_MIN_CENTIMOS + random.nextInt(rango)) * (long) numTransferencias;
            System.out.println("Importe total transferencias = " + Dinero.formatear(importeTotal) + " \u20AC");
            System.out.println("El saldo de la cuenta es: " + Dinero.formatear(saldoInicial) + " \u20AC");

            // 5) Pool de hilos que procesan la coleccion compartida en paralelo
            GestorTransferencias gestor = new GestorTransferencias(transferencias, saldoInicial, directorio);
            List<HiloTransferencias> tareas = new ArrayList<>();
            ExecutorService pool = Executors.newFixedThreadPool(numHilos);
            for (int i = 0; i < numHilos; i++) {
                HiloTransferencias tarea = new HiloTransferencias(i, gestor);
                tareas.add(tarea);
                pool.execute(tarea);
            }
            pool.shutdown(); // no se aceptan mas tareas; las lanzadas siguen hasta acabar
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS); // se espera a que terminen todos los hilos

            // 6) Con los hilos parados se cierran los ficheros
            gestor.cerrar();
            for (HiloTransferencias tarea : tareas) {
                if (tarea.haFallado()) {
                    return Constantes.SALIDA_ERROR_ES;
                }
            }

            // 7) Resumen y datos para el informe de ApPrinc
            imprimirResumen(tareas, saldoInicial, gestor.getSaldoCentimos());
            return Constantes.SALIDA_OK;

        } catch (IOException e) {
            System.out.println("[SubApProcFT] Error de entrada/salida: " + e.getMessage());
            return Constantes.SALIDA_ERROR_ES;
        } catch (IllegalArgumentException e) {
            System.out.println("[SubApProcFT] Fichero de transferencias incorrecto: " + e.getMessage());
            return Constantes.SALIDA_FICHERO_INCORRECTO;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[SubApProcFT] Interrumpida");
            return Constantes.SALIDA_INTERRUMPIDO;
        }
    }

    /**
     * Espera indefinidamente (comprobando cada segundo) hasta que existe el fichero
     * de transferencias y ya no existe el de bloqueo. El orden de las comprobaciones
     * importa: SubApGenFT crea el bloqueo ANTES que el fichero y lo borra DESPUES de
     * terminarlo, asi que si vemos el fichero y ya no hay bloqueo, esta completo.
     *
     * @param fichero ruta del fichero de transferencias
     * @param bloqueo ruta del fichero de bloqueo
     * @throws InterruptedException si se interrumpe la espera
     */
    private static void esperarFichero(Path fichero, Path bloqueo) throws InterruptedException {
        while (!(Files.exists(fichero) && !Files.exists(bloqueo))) {
            Thread.sleep(Constantes.ESPERA_COMPROBACION_MS);
        }
    }

    /**
     * Lee el fichero de transferencias a una lista en memoria.
     *
     * @param fichero ruta del fichero
     * @return lista con las transferencias, en el orden del fichero
     * @throws IOException              si no se puede leer
     * @throws IllegalArgumentException si alguna linea esta mal formada
     */
    private static List<Transferencia> leerTransferencias(Path fichero) throws IOException {
        List<Transferencia> lista = new ArrayList<>();
        for (String linea : Files.readAllLines(fichero, StandardCharsets.UTF_8)) {
            if (!linea.isBlank()) {
                lista.add(Transferencia.desdeLinea(linea));
            }
        }
        return lista;
    }

    /**
     * Imprime el resumen por hilo y, a continuacion, las lineas con prefijo
     * (PREFIJO_INFORME) que ApPrinc usa para construir el informe final. Los
     * importes de estas lineas van en centimos para no perder precision.
     *
     * @param tareas        tareas de los hilos, ya terminadas
     * @param saldoInicial  saldo inicial en centimos
     * @param saldoFinal    saldo final en centimos
     */
    private static void imprimirResumen(List<HiloTransferencias> tareas, long saldoInicial, long saldoFinal) {
        for (HiloTransferencias h : tareas) {
            System.out.println("En este hilo: Hilo " + h.getId() + " ha hecho transferencias por valor de: "
                    + Dinero.formatear(h.getImportePagadoCentimos()) + " \u20AC.");
        }
        System.out.println("Quedan " + Dinero.formatear(saldoFinal) + " \u20AC en la cuenta");
        System.out.println("Fin del proceso.");

        // Datos para el informe de ApPrinc (formato: PREFIJO;TIPO;campos separados por ;)
        for (HiloTransferencias h : tareas) {
            System.out.println(Constantes.PREFIJO_INFORME + ";HILO;" + h.getId() + ";" + h.getProcesadas() + ";"
                    + h.getImporteProcesadoCentimos() + ";" + h.getImportePagadoCentimos() + ";"
                    + h.getInternas() + ";" + h.getExternas() + ";" + h.getSinSaldo());
        }
        System.out.println(Constantes.PREFIJO_INFORME + ";SALDO;" + saldoInicial + ";" + saldoFinal);
    }

    /**
     * Lee una linea de la entrada y comprueba que existe y no esta vacia.
     *
     * @param entrada lector de la entrada estandar
     * @return la linea leida
     * @throws IOException              si falla la lectura
     * @throws IllegalArgumentException si no hay linea o esta vacia
     */
    private static String leerLinea(BufferedReader entrada) throws IOException {
        String linea = entrada.readLine();
        if (linea == null || linea.isBlank()) {
            throw new IllegalArgumentException("falta un dato en la entrada");
        }
        return linea;
    }
}
