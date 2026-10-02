package hijo;

import java.io.File;
import java.util.Scanner;

public class Ejercicio4 {

    public static void main(String[] args) {

        // Necesitamos 2 argumentos: asignatura y fichero
        if (args.length < 2) {
            System.out.println("Faltan argumentos: asignatura y fichero");
            System.exit(-1);
        }

        String asignatura = args[0];
        String nombreFichero = args[1];

        double suma = 0;
        int contador = 0;
        int aprobados = 0;

        try {
            Scanner lector = new Scanner(new File(nombreFichero));

            // Leemos el fichero nota a nota (una por linea)
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();

                if (!linea.equals("")) {
                    double nota = Double.parseDouble(linea);
                    suma = suma + nota;
                    contador++;

                    if (nota >= 5) {
                        aprobados++;
                    }
                }
            }
            lector.close();

        } catch (Exception e) {
            // No existe el fichero o hay algo que no es una nota
            System.out.println("No se ha podido leer el fichero " + nombreFichero);
            System.exit(-2);
        }

        if (contador == 0) {
            System.out.println("El fichero " + nombreFichero + " no tiene notas");
            System.exit(-3);
        }

        double media = suma / contador;

        System.out.println("En la asignatura " + asignatura);
        System.out.println("Han aprobado " + aprobados + " alumnos");
        // %.1f -> un decimal; Locale.US para que salga con punto (5.3)
        System.out.println("La media es " + String.format(java.util.Locale.US, "%.1f", media));
        System.exit(0);
    }
}
