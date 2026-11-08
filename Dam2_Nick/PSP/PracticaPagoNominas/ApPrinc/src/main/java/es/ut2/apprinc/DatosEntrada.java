package es.ut2.apprinc;

/**
 * Datos que introduce el usuario. Es un record: clase inmutable con sus getters
 * (nombre(), ruta()...) generados automaticamente.
 *
 * Parametros del constructor: nombre del fichero de transferencias, ruta donde se
 * guarda, numero de transferencias a generar y numero de hilos.
 * Salidas: ninguna (solo almacena datos).
 *
 * @param nombre            nombre del fichero de transferencias
 * @param ruta              directorio donde se crea
 * @param numTransferencias numero de transferencias a generar (mayor que 0)
 * @param numHilos          hilos con los que procesar el fichero (mayor que 0)
 */
public record DatosEntrada(String nombre, String ruta, int numTransferencias, int numHilos) {
}
