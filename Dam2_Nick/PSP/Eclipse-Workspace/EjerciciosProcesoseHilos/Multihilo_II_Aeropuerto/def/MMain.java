package def;

public class MMain{
	

    public static void main(String[] args) throws InterruptedException {

        // El reloj se crea primero (Thread-0), los aeroplanos son Thread-1 ... Thread-20
        Reloj reloj = new Reloj();
        reloj.setDaemon(true); // para que no impida acabar el programa
        reloj.start();

        Pista pista = new Pista();
        Aeroplano[] aeroplanos = new Aeroplano[20];

        for (int i = 0; i < 20; i++) {
            // Aleatoriamente es avion o avioneta
            boolean esAvion = Math.random() < 0.5;

            aeroplanos[i] = new Aeroplano(esAvion, pista);
            aeroplanos[i].start();

            // Llega un aeroplano a la pista cada 2000 ms
            Thread.sleep(2000);
        }

        // Esperamos a que despeguen todos
        for (int i = 0; i < 20; i++) {
            aeroplanos[i].join();
        }

        System.out.println("Han despegado los 20 aeroplanos.");
    }
}
