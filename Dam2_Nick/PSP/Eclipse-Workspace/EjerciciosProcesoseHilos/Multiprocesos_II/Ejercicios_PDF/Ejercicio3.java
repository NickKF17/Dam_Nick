package Ejercicios_PDF;

public class Ejercicio3 {

    public static void main(String[] args) {

        // Si no llega ninguna cadena -> -1
        if (args.length == 0) {
            System.exit(-1);
        }

        String texto = args[0];

        // Construimos el texto al reves, letra a letra
        String alReves = "";
        for (int i = texto.length() - 1; i >= 0; i--) {
            alReves = alReves + texto.charAt(i);
        }

        // Si el texto y el texto al reves son iguales, es palindromo
        if (texto.equalsIgnoreCase(alReves)) {
            System.out.println("Es palíndromo");
        } else {
            System.out.println("NO es palíndromo");
        }

        System.exit(0);
    }
}
