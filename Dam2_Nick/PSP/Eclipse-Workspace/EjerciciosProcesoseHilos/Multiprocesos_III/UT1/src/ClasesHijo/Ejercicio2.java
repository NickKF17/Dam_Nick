package ClasesHijo;

import java.util.Scanner;

public class Ejercicio2 {

    public static void main(String[] args) {

        // La entrada es el fichero datos.txt (lo redirige el padre)
        Scanner entrada = new Scanner(System.in);
        double suma = 0;

        // Leemos lineas hasta que llegue un * o se acabe el fichero
        while (entrada.hasNextLine()) {
            String linea = entrada.nextLine();

            if (linea.equals("*")) {
                break;
            }

            try {
                suma = suma + Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                // El error se escribe en la salida de error (error.txt)
                System.err.println("Error: '" + linea + "' no es un número");
                System.exit(-1);
            }
        }

        // La suma se escribe en la salida estandar (suma.txt)
        System.out.println("Suma: " + suma);
        System.exit(0);
    }
}
