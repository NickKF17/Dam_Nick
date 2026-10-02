package ClasesHijo;

import java.util.Scanner;

public class Ejercicio1 {

    public static void main(String[] args) {

        // Ahora el numero llega por la entrada estandar (que el padre
        // habra redirigido al fichero dato.txt)
        Scanner entrada = new Scanner(System.in);

        // Si no hay nada en el fichero -> -1
        if (!entrada.hasNextLine()) {
            System.exit(-1);
        }

        String texto = entrada.nextLine();

        // Si la linea esta vacia -> -1
        if (texto.equals("")) {
            System.exit(-1);
        }

        try {
            int numero = Integer.parseInt(texto);

            if (numero > 0) {
                System.exit(-3); // entero positivo
            } else {
                System.exit(0); // entero negativo
            }
        } catch (NumberFormatException e) {
            // No se puede convertir a numero -> no es un entero
            System.exit(-2);
        }
    }
}
