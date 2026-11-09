package def;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Escritor {

    public static void main(String[] args) {

        // try-with-resources: todo lo que se abre aqui se cierra solo al acabar
        try (RandomAccessFile ficheroBloqueo = new RandomAccessFile("datos.lock", "rw");
             FileChannel canal = ficheroBloqueo.getChannel();
             FileLock bloqueo = canal.lock()) { // lock() espera hasta conseguir el bloqueo

            System.out.println("Establecido bloqueo para escribir los mensajes...");

            // Escribimos los 10 mensajes en datos.txt mientras tenemos el bloqueo
            try (PrintWriter escritor = new PrintWriter(new FileWriter("datos.txt"))) {

                for (int i = 0; i < 10; i++) {
                    escritor.println("Mensaje de iteración :" + i);
                    escritor.flush(); // para que se guarde en el fichero ya

                    System.out.println("Escritor: Escrito mensaje de iteración " + i);

                    // Esperamos entre 1000 y 1500 milisegundos
                    int espera = 1000 + (int) (Math.random() * 501);
                    Thread.sleep(espera);
                }
            }

            // Al salir del try-with-resources el bloqueo se libera solo
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Liberado bloqueo.");
    }
}
