import java.io.DataOutputStream;
import java.io.FileOutputStream;

// Programa auxiliar: genera datos/coordenadas.dat de prueba (20 bytes por registro)
public class CrearCoordenadas {
	public static void main(String[] args) throws Exception {
		try (DataOutputStream salida = new DataOutputStream(new FileOutputStream("datos/coordenadas.dat"))) {
			escribir(salida, 101, 40.4167f, -3.7037f, "ACTV");
			escribir(salida, 202, -22.9068f, -43.1729f, "WARN");
			escribir(salida, 303, 35.6762f, 139.6503f, "IDLE");
		}
		System.out.println("Fichero de prueba creado");
	}

	static void escribir(DataOutputStream salida, int id, float lat, float lon, String estado) throws Exception {
		salida.writeInt(id); // 4 bytes
		salida.writeFloat(lat); // 4 bytes
		salida.writeFloat(lon); // 4 bytes
		for (int i = 0; i < 4; i++)
			salida.writeChar(estado.charAt(i)); // 4 x 2 bytes
	}
}
