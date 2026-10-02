package padre;

import java.io.File;

public class LlamarEjercicio3 {

    public static void main(String[] args) throws Exception {

        File texto = new File("texto.txt");
        if (!texto.exists()) {
            System.out.println("No existe el fichero texto.txt");
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "../ClasesHijo/bin", "hijo.Ejercicio3");

        // Entrada desde texto.txt, salida a palindromo.txt y errores a error.txt
        pb.redirectInput(texto);
        pb.redirectOutput(new File("palindromo.txt"));
        pb.redirectError(new File("error.txt"));

        Process proceso = pb.start();
        int salida = proceso.waitFor();

        if (salida > 127) {
            salida = salida - 256; // 255 -> -1 en Linux
        }

        System.out.println("Valor de Salida: " + salida);
        if (salida == 0) {
            System.out.println("El resultado se ha guardado en palindromo.txt");
        } else {
            System.out.println("Ha habido un error, mira error.txt");
        }
    }
}
