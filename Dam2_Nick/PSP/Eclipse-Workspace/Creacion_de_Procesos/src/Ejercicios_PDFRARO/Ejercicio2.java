package Ejercicios_PDF;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Ejercicio2 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        double suma = 0;
        String linea;

        while ((linea = br.readLine()) != null) {
            linea = linea.trim();

            if (linea.equals("*")) {
                break;
            }

            try {
                double valor = Double.parseDouble(linea);
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.exit(-1);
                }
                suma += valor;
            } catch (NumberFormatException e) {
                System.exit(-1);
            }
        }

        System.out.println("Suma: " + suma);
        System.exit(0);
    }
}
