package ClasesPadre;

import java.util.ArrayList;
import java.util.Scanner;

public class LlamarEjercicio4 {

    public static void main(String[] args) throws Exception {
//notas_psp.txt
        Scanner teclado = new Scanner(System.in);

        while (true) {
            System.out.println("Escribe la asignatura:");
            String asignatura = teclado.nextLine();

            // Si escribe * terminamos
            if (asignatura.equals("*")) {
                break;
            }

            System.out.println("Escribe el nombre del fichero:");
            String fichero = teclado.nextLine();

            if (fichero.equals("*")) {
                break;
            }

            // Lanzamos el programa Ejercicio4 con los 2 argumentos
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "ClasesHijo.Ejercicio4", asignatura, fichero);
            Process proceso = pb.start();

            // Leemos lo que escribe el hijo
            Scanner lector = new Scanner(proceso.getInputStream());
            ArrayList<String> respuesta = new ArrayList<String>();
            while (lector.hasNextLine()) {
                respuesta.add(lector.nextLine());
            }

            int salida = proceso.waitFor();
            if (salida > 127) {
                salida = salida - 256;
            }

            System.out.println("Valor de Salida: " + salida);
            for (String linea : respuesta) {
                System.out.println(linea);
            }
            System.out.println();
        }
    }
}
