package ejercicio1;

// Ejercicio 1: heredando de Thread
public class ParImpar extends Thread {

    private boolean pares; // true -> pares, false -> impares

    public ParImpar(boolean pares) {
        this.pares = pares;
    }

    // Saca por consola los pares o los impares entre 1 y 10
    public void sacarNumeros() {
        int inicio;
        if (pares) {
            inicio = 2;
        } else {
            inicio = 1;
        }

        for (int i = inicio; i <= 10; i = i + 2) {
            System.out.println(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Hilo interrumpido");
            }
        }
    }

    @Override
    public void run() {
        sacarNumeros();
    }

    public static void main(String[] args) {
        ParImpar impares = new ParImpar(false);
        ParImpar pares = new ParImpar(true);

        impares.start();
        pares.start();
    }
}
