package Lector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Lector {

    public static void main(String[] args) {

        System.out.println("Esperando que se libere el bloqueo...");

        // Abrimos datos.lock (el mismo fichero que usa el Escritor) y sacamos su canal
        // CREATE por si el Lector se ejecuta antes que el Escritor, WRITE porque lock() lo pide
        try (FileChannel canal = FileChannel.open(Path.of("datos.lock"),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             // lock() se queda esperando hasta que el Escritor suelte el bloqueo
             FileLock bloqueo = canal.lock()) {

            // Ya tenemos el bloqueo, asi que el Escritor ha terminado.
            // Ahora abrimos datos.txt y lo leemos linea a linea
            try (BufferedReader lector = new BufferedReader(new FileReader("datos.txt"))) {

                String linea;
                while ((linea = lector.readLine()) != null) {
                    System.out.println("Leído mensaje: " + linea);
                }
            }

            System.out.println("Fin de lectura del fichero.");

        } catch (IOException e) {
            System.out.println("Error con los ficheros: " + e.getMessage());
        }
    }
}