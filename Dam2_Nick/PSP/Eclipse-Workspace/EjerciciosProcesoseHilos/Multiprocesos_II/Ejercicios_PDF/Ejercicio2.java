package Ejercicios_PDF;

import java.util.Scanner;

public class Ejercicio2 {

    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);
        double suma = 0;

        // Leemos lineas hasta que llegue un *
        while (entrada.hasNextLine()) {
            String linea = entrada.nextLine();

            if (linea.equals("*")) {
                break; 
            }

            try {
                // Si es un numero, lo sumamos
                suma = suma + Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                // Si no es un numero, es una cadena -> error -1
                System.exit(-1);
            }
        }

        System.out.println("Suma: " + suma);
        System.exit(0);
    }
}
