package Ejercicios_PDF;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LlamarEjercicio2 {

    public static void main(String[] args) throws Exception {

        Scanner teclado = new Scanner(System.in);
        ArrayList<String> numeros = new ArrayList<String>();

        // Pedimos numeros hasta que se escriba * 
        String linea = "";
        while (!linea.equals("*")) {
            System.out.println("Escribe un número:");
            linea = teclado.nextLine();
            numeros.add(linea);
        }

        // Lanzamos el programa Ejercicio2
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", "bin", "Ejercicios_PDF.Ejercicio2");
        Process proceso = pb.start();

        // Le mandamos todos los datos por su entrada estandar
        PrintWriter escritor = new PrintWriter(proceso.getOutputStream());
        for (String numero : numeros) {
            escritor.println(numero);
        }
        escritor.flush();
        escritor.close();


        Scanner lector = new Scanner(proceso.getInputStream());
        ArrayList<String> respuesta = new ArrayList<String>();
        while (lector.hasNextLine()) {
            respuesta.add(lector.nextLine());
        }

        int salida = proceso.waitFor();
        if (salida > 127) {
            salida = salida - 256; // 255 -> -1 en Linux
        }

        System.out.println("Valor de Salida: " + salida);
        for (String numero : numeros) {
            System.out.println("Escrito " + numero);
        }
        for (String texto : respuesta) {
            System.out.println(texto);
        }
    }
}
