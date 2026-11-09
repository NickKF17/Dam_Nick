package def;

public class Aeroplano extends Thread {

    private boolean esAvion; // true -> AVION, false -> AVIONETA
    private Pista pista;

    public Aeroplano(boolean esAvion, Pista pista) {
        this.esAvion = esAvion;
        this.pista = pista;
    }

    @Override
    public void run() {
        String tipo;
        int turbulencia; // milisegundos

        if (esAvion) {
            tipo = "AVION";
            turbulencia = 3000;
        } else {
            tipo = "AVIONETA";
            turbulencia = 2000;
        }

        System.out.println("Llega a pista el/la " + tipo + " con número " + getName());

        try {
            pista.entrar(esAvion);

            System.out.println("Aeroplano de tipo: " + tipo + " con número " + getName() + " entra en la pista de despegue...");

            // Despega y genera turbulencias: la pista sigue ocupada ese tiempo
            Thread.sleep(turbulencia);

            System.out.println("Aeroplano de tipo: " + tipo + " con número " + getName() + " ha despegado.");

            pista.salir();

        } catch (InterruptedException e) {
            System.out.println(getName() + " ha sido interrumpido");
        }
    }
}
