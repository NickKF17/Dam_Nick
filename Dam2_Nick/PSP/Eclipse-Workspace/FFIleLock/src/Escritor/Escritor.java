package Escritor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Escritor {

    public static void main(String[] args) {

        // 1. y 2. Abrimos (o creamos) datos.lock y obtenemos su FileChannel directamente
        // CREATE = lo crea si no existe, WRITE = permiso de escritura (necesario para lock())
        // Va en el try-with-resources para que se cierre solo
        try (FileChannel canal = FileChannel.open(Path.of("datos.lock"),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            // 3. Obtenemos el bloqueo (si otro proceso lo tiene, aqui se espera)
            try (FileLock bloqueo = canal.lock()) {

                // 4. Avisamos de que ya tenemos el bloqueo
                System.out.println("Establecido bloqueo para escribir los mensajes...");

                // 5. Mientras tenemos el bloqueo, escribimos en datos.txt
                try (PrintWriter escritor = new PrintWriter(new FileWriter("datos.txt"))) {

                    for (int i = 0; i < 10; i++) {

                        // Escribimos el mensaje en el fichero
                        escritor.println("Mensaje de iteración :" + i);
                        // flush para que se guarde ya en el fichero
                        escritor.flush();

                        // 6. Mostramos por consola lo que hemos escrito
                        System.out.println("Lector: Escrito mensaje de iteración " + i);

                        // Esperamos entre 1 y 1,5 segundos (1000 a 1500 ms)
                        int espera = 1000 + (int) (Math.random() * 501);
                        try {
                            Thread.sleep(espera);
                        } catch (InterruptedException e) {
                            System.out.println("Me han interrumpido: " + e.getMessage());
                        }
                    }
                }
                // Aqui se cierra datos.txt y al salir del try se libera el bloqueo
            }

            // 7. El bloqueo ya esta liberado
            System.out.println("Liberado bloqueo.");

        } catch (IOException e) {
            System.out.println("Error con los ficheros: " + e.getMessage());
        }
    }
}