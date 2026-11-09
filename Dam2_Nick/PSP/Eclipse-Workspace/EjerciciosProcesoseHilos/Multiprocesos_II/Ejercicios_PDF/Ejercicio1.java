package Ejercicios_PDF;

public class Ejercicio1 {

    public static void main(String[] args) {

        // Si no llega ningun argumento (o llega vacio) -> -1
        if (args.length == 0 || args[0].equals("")) {
            System.exit(-1);
        }

        try {
            // Intentamos convertir el texto a numero entero
            int numero = Integer.parseInt(args[0]);

            if (numero > 0) {
                System.exit(-3); // entero positivo
            } else {
                System.exit(0); // entero negativo
            }
        } catch (NumberFormatException e) {
            // Si no se puede convertir, no es un entero
            System.exit(-2);
        }
    }
}
