package Ej5;

public class Bomba implements Runnable {

    private int modo; // 1 = cuenta atras, 2 = artificiero

    // Guardamos el hilo de la bomba aqui para que el artificiero pueda interrumpirlo
    private static Thread hiloBomba;

    public Bomba(int modo) {
        this.modo = modo;
    }

    // Metodo 1: cuenta atras de 9 a 0
    public void cuentaAtras() {
        try {
            for (int i = 9; i >= 0; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
            // si llega aqui nadie la ha desactivado
            System.out.println("PUUUUUUMMMMMMMMMM");
        } catch (InterruptedException e) {
            // nos han interrumpido: el artificiero la ha desactivado
            System.out.println("La bomba ha sido desactivada");
        }
    }

    // Metodo 2: el artificiero intenta desactivar la bomba
    public void artificiero() {
        System.out.println("El artificiero comienza a desactivar la bomba");
        try {
            // tarda entre 9500 y 10500 ms
            int tiempo = 9500 + (int) (Math.random() * 1001);
            Thread.sleep(tiempo);

            System.out.println("El artificiero ha desactivado la bomba");
            // avisamos a la bomba de que ya esta desactivada
            hiloBomba.interrupt();
        } catch (InterruptedException e) {
            System.out.println("El artificiero ha muerto");
        }
    }

    // Segun el parametro arrancamos un metodo u otro
    @Override
    public void run() {
        if (modo == 1) {
            cuentaAtras();
        } else {
            artificiero();
        }
    }

    public static void main(String[] args) {
        hiloBomba = new Thread(new Bomba(1));
        Thread hiloArtificiero = new Thread(new Bomba(2));

        hiloBomba.start();
        hiloArtificiero.start();

        // Esperamos a que acabe el hilo de la bomba y despues
        // interrumpimos al artificiero (si ya habia acabado no pasa nada)
        try {
            hiloBomba.join();
        } catch (InterruptedException e) {
            System.out.println("Interrumpido el main");
        }
        hiloArtificiero.interrupt();
    }
}
