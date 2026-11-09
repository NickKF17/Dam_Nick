package ClasesPadre;

import java.io.File;

public class LlamarEjercicio1 {

    public static void main(String[] args) throws Exception {

        // El fichero dato.txt tiene que estar en la carpeta del proyecto
        File fichero = new File("dato.txt");
        if (!fichero.exists()) {
            System.out.println("No existe el fichero dato.txt");
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "ClasesHijo.Ejercicio1");

        // La entrada del hijo ya no es el teclado, es el fichero dato.txt
        pb.redirectInput(fichero);

        Process proceso = pb.start();
        int salida = proceso.waitFor();

        // En Linux los valores negativos llegan como 255, 254, 253...
        if (salida > 127) {
            salida = salida - 256;
        }

        if (salida == -1) {
            System.out.println("No has escrito nada");
        } else if (salida == -2) {
            System.out.println("No has escrito un entero");
        } else if (salida == -3) {
            System.out.println("Has escrito un entero positivo");
        } else if (salida == 0) {
            System.out.println("El entero debe ser positivo");
        }
    }
}
