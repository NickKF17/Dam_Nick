package def;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Scanner;
import java.io.File;

public class Lector {

    public static void main(String[] args) {

        System.out.println("Esperando que se libere el bloqueo...");

        try (RandomAccessFile ficheroBloqueo = new RandomAccessFile("datos.lock", "rw");
             FileChannel canal = ficheroBloqueo.getChannel();
             FileLock bloqueo = canal.lock()) { // se queda esperando si el Escritor lo tiene

            // Ya tenemos el bloqueo: leemos datos.txt
            try (Scanner lector = new Scanner(new File("datos.txt"))) {
                while (lector.hasNextLine()) {
                    String linea = lector.nextLine();
                    System.out.println("Leído mensaje: " + linea);
                }
            }

            System.out.println("Fin de lectura del fichero.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
