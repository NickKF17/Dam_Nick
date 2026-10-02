package hijo;

import java.util.Scanner;

public class Ejercicio3 {

    public static void main(String[] args) {

        // El texto llega por la entrada estandar (fichero texto.txt)
        Scanner entrada = new Scanner(System.in);

        // Si no hay texto -> error -1
        if (!entrada.hasNextLine()) {
            System.err.println("Error: el fichero está vacío");
            System.exit(-1);
        }

        String texto = entrada.nextLine();

        if (texto.equals("")) {
            System.err.println("Error: no hay ningún texto");
            System.exit(-1);
        }

        // Construimos el texto al reves, letra a letra
        String alReves = "";
        for (int i = texto.length() - 1; i >= 0; i--) {
            alReves = alReves + texto.charAt(i);
        }

        // El resultado va a la salida estandar (palindromo.txt)
        if (texto.equalsIgnoreCase(alReves)) {
            System.out.println("Es palíndromo");
        } else {
            System.out.println("NO es palíndromo");
        }

        System.exit(0);
    }
}
