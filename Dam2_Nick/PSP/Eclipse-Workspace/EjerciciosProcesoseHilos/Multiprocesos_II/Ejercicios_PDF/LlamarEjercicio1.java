package Ejercicios_PDF;

import java.util.Scanner;

public class LlamarEjercicio1 {

    public static void main(String[] args) throws Exception {

        Scanner teclado = new Scanner(System.in);
        System.out.println("Escribe un número entero positivo:");
        String dato = teclado.nextLine();

      
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "Ejercicios_PDF.Ejercicio1", dato);
        Process proceso = pb.start();

        // Esperamos a que termine y guardamos su valor de salida
        int salida = proceso.waitFor();

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
