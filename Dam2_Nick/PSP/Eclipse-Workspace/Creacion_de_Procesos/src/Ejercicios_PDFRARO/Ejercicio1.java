package Ejercicios_PDF;
/*
 Recibe un argumento y devuelve con System.exit():
   -1 -> argumento vacío
   -2 -> no es un entero
   -3 -> entero positivo
    0 -> entero negativo (o cero)
 */
public class Ejercicio1 {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.exit(-1);
        }

        int numero;
        try {
            numero = Integer.parseInt(args[0].trim());
        } catch (NumberFormatException e) {
            System.exit(-2);
            return;
        }

        if (numero > 0) {
            System.exit(-3);
        }
        System.exit(0);
    }
}
