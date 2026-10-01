package Ejercicios_PDF;

import java.util.Scanner;

public class LlamarEjercicio3 {

    public static void main(String[] args) throws Exception {

        Scanner teclado = new Scanner(System.in);
        System.out.println("Escribe un texto:");
        String texto = teclado.nextLine();

        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "Ejercicios_PDF.Ejercicio3", texto);
        Process proceso = pb.start();

        Scanner lector = new Scanner(proceso.getInputStream());
        String respuesta = "";
        if (lector.hasNextLine()) {
            respuesta = lector.nextLine();
        }

        int salida = proceso.waitFor();

        System.out.println("Valor de Salida: " + salida);
        System.out.println(respuesta);
    }
}
