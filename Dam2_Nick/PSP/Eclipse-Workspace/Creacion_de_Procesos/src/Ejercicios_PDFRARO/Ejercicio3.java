package Ejercicios_PDFRARO;
import java.text.Normalizer;


public class Ejercicio3 {

    public static void main(String[] args) {
        if (args.length == 0 || args[0].trim().isEmpty()) {
            System.exit(-1);
        }

        String texto = String.join(" ", args);
        String limpio = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "");

        boolean palindromo = new StringBuilder(limpio).reverse().toString().equals(limpio);

        System.out.println(palindromo ? "Es palíndromo" : "NO es palíndromo");
        System.exit(0);
    }
}
