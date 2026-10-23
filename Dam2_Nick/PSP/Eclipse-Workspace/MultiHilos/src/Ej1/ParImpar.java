package Ej1;

public class ParImpar implements Runnable {

    // true = saca los pares, false = saca los impares
    private boolean pares;

    public ParImpar(boolean pares) {
        this.pares = pares;
    }

    // Metodo que saca por consola los pares o los impares entre 1 y 10
    // dependiendo del dato "pares"
    public void mostrarNumeros() {
        for (int i = 1; i <= 10; i++) {

            // esPar sera true si el numero es par
            boolean esPar = (i % 2 == 0);

            // Solo lo mostramos si coincide con lo que nos han pedido
            if (esPar == pares) {
                System.out.println(i);

                // Esperamos un tiempo aleatorio (50 a 150 ms) para que los hilos se mezclen
                try {
                    Thread.sleep(50 + (int) (Math.random() * 101));
                } catch (InterruptedException e) {
                    System.out.println("Hilo interrumpido");
                }
            }
        }
    }

    // El metodo run es lo que hace el hilo cuando arranca
    @Override
    public void run() {
        mostrarNumeros();
    }

    public static void main(String[] args) {
        // Un hilo para los pares y otro para los impares
        Thread hiloPares = new Thread(new ParImpar(true));
        Thread hiloImpares = new Thread(new ParImpar(false));

        hiloPares.start();
        hiloImpares.start();
    }
}
