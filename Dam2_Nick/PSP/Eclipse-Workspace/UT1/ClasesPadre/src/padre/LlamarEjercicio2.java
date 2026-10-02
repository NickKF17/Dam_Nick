package padre;

import java.io.File;

public class LlamarEjercicio2 {

    public static void main(String[] args) throws Exception {

        File datos = new File("datos.txt");
        if (!datos.exists()) {
            System.out.println("No existe el fichero datos.txt");
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "../ClasesHijo/bin", "hijo.Ejercicio2");

        // Entrada desde datos.txt, salida a suma.txt y errores a error.txt
        pb.redirectInput(datos);
        pb.redirectOutput(new File("suma.txt"));
        pb.redirectError(new File("error.txt"));

        Process proceso = pb.start();
        int salida = proceso.waitFor();

        if (salida > 127) {
            salida = salida - 256; // 255 -> -1 en Linux
        }

        System.out.println("Valor de Salida: " + salida);
        if (salida == 0) {
            System.out.println("La suma se ha guardado en suma.txt");
        } else {
            System.out.println("Ha habido un error, mira error.txt");
        }
    }
}
