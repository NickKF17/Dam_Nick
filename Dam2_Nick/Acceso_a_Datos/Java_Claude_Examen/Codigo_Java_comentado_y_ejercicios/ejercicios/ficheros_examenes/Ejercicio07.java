import java.io.RandomAccessFile;

// Ejercicio 7: leer un binario de registros de 20 bytes: int + float + float + 4 char
public class Ejercicio07 {

	// Tamaño del registro: 4 (int) + 4 (float) + 4 (float) + 4 * 2 (chars) = 20 bytes
	static final int TAMANYO_REGISTRO = 4 + 4 + 4 + 4 * 2;

	public static void main(String[] args) {
		mostrarCoordenadas("datos/coordenadas.dat");
	}

	public static void mostrarCoordenadas(String fichero) {
		// "r" = solo lectura. RandomAccessFile nos da length() para saber cuántos registros hay
		try (RandomAccessFile raf = new RandomAccessFile(fichero, "r")) {
			long numRegistros = raf.length() / TAMANYO_REGISTRO;
			// Leemos los registros en el mismo orden y con los mismos tipos con que se escribieron
			for (long i = 0; i < numRegistros; i++) {
				int id = raf.readInt();
				float latitud = raf.readFloat();
				float longitud = raf.readFloat();
				String estado = "";
				for (int j = 0; j < 4; j++)
					estado = estado + raf.readChar();
				System.out.println("Satélite ID: " + id + " | Posición: (" + formatear(latitud) + ", " + formatear(longitud)
						+ ") | Estado: " + estado);
			}
		} catch (Exception e) {
			System.out.println("Error al leer el fichero: " + e.getMessage());
		}
	}

	// 4 decimales y coma decimal. Usamos String.format + replace en lugar de Locale
	// porque así el resultado no depende de la configuración regional del equipo
	public static String formatear(float numero) {
		return String.format("%.4f", numero).replace('.', ',');
	}
}
