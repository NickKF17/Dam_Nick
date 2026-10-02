package Ejercicios_PDFRARO;
import java.util.Scanner;

/*
  Pide un entero positivo, llama a Ejercicio1 y, según el valor de salida,
  informa al usuario.
 */
public class LlamarEjercicio1 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un número entero positivo:");
        String dato = sc.nextLine();

        try {
        	ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "Ejercicios_PDF.Ejercicio1", dato);
            pb.inheritIO();
            Process proceso = pb.start();
            int salida = (byte) proceso.waitFor();

            switch (salida) {
                case -1:
                    System.out.println("No has escrito nada");
                    break;
                case -2:
                    System.out.println("No has escrito un entero");
                    break;
                case -3:
                    System.out.println("Has escrito un entero positivo");
                    break;
                case 0:
                    System.out.println("El entero debe ser positivo");
                    break;
                default:
                    System.out.println("Valor de salida desconocido: " + salida);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
