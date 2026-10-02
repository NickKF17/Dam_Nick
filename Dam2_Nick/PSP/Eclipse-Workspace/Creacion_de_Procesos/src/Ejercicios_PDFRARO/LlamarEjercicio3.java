package Ejercicios_PDFRARO;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/*
  Pide un texto por consola, llama a Ejercicio3 y muestra su respuesta.
 */
public class LlamarEjercicio3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escribe un texto:");
        String texto = sc.nextLine();

        try {
        	ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "Ejercicios_PDF.Ejercicio3", texto);
            pb.redirectErrorStream(true);
            Process proceso = pb.start();

            StringBuilder respuesta = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String l;
                while ((l = br.readLine()) != null) {
                    respuesta.append(l).append(System.lineSeparator());
                }
            }

            int salida = (byte) proceso.waitFor();
            System.out.println("Valor de Salida: " + salida);
            if (salida == -1) {
                System.out.println("No has escrito nada");
            } else {
                System.out.print(respuesta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
