package ejercicio5;

// Ejercicio 5: implementando Runnable
// Segun el parametro, el hilo hace de bomba (cuenta atras) o de artificiero
public class Bomba implements Runnable {

    private boolean esBomba; // true -> cuenta atras, false -> artificiero

    // Lo cambia el artificiero cuando consigue desactivar la bomba
    private static volatile boolean desactivada = false;

    public Bomba(boolean esBomba) {
        this.esBomba = esBomba;
    }

    // Cuenta atras de 9 a 0
    public void cuentaAtras() {
        try {
            for (int i = 9; i >= 0; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("La cuenta atras ha sido interrumpida");
        }

        if (desactivada) {
            System.out.println("La bomba ha sido desactivada");
        } else {
            System.out.println("PUUUUUUMMMMMMMMMM");
        }
    }

    // El artificiero tarda entre 9500 y 10500 milisegundos
    public void artificiero() {
        System.out.println("El artificiero comienza a desactivar la bomba");
        int tiempo = 9500 + (int) (Math.random() * 1001);
        try {
            Thread.sleep(tiempo);
            desactivada = true;
            System.out.println("El artificiero ha desactivado la bomba");
        } catch (InterruptedException e) {
            System.out.println("El artificiero ha muerto");
        }
    }

    @Override
    public void run() {
        if (esBomba) {
            cuentaAtras();
        } else {
            artificiero();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread hiloBomba = new Thread(new Bomba(true));
        Thread hiloArtificiero = new Thread(new Bomba(false));

        hiloBomba.start();
        hiloArtificiero.start();

        // Cuando acaba la bomba, interrumpimos al artificiero
        hiloBomba.join();
        hiloArtificiero.interrupt();
    }
}
