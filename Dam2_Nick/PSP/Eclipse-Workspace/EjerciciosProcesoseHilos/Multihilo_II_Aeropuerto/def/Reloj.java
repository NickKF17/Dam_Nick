// Hilo que va contando los segundos (para ver cuando pasa cada cosa)
package def;

public class Reloj extends Thread {

    @Override
    public void run() {
        int segundos = 0;
        while (true) {
            System.out.println(segundos);
            segundos++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
