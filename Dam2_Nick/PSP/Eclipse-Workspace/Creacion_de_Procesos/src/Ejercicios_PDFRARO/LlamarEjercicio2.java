package Ejercicios_PDFRARO;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
  Pide números por consola hasta que se escriba "*",
  se los envía a Ejercicio2 por su entrada estándar y muestra su salida.
 */
public class LlamarEjercicio2 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> datos = new ArrayList<>();
        String linea;

        do {
            System.out.println("Escribe un número:");
            linea = sc.nextLine();
            datos.add(linea);
        } while (!linea.trim().equals("*"));

        try {
        	ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "Ejercicios_PDF.Ejercicio2");
            pb.redirectErrorStream(true);
            Process proceso = pb.start();

           
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(proceso.getOutputStream()));
            for (String dato : datos) {
                bw.write(dato);
                bw.newLine();
            }
            bw.flush();
            bw.close();

 
            List<String> salidaHijo = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(proceso.getInputStream()))) {
                String l;
                while ((l = br.readLine()) != null) {
                    salidaHijo.add(l);
                }
            }

            int salida = (byte) proceso.waitFor();
            System.out.println("Valor de Salida: " + salida);
            for (String dato : datos) {
                System.out.println("Escrito " + dato);
            }
            for (String l : salidaHijo) {
                System.out.println(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
