package Ej4;

public class Equipo implements Runnable {

    private String nombreEquipo;
    private String[] corredores;

    public Equipo(String nombreEquipo, String[] corredores) {
        this.nombreEquipo = nombreEquipo;
        this.corredores = corredores;
    }

    @Override
    public void run() {
        for (int i = 0; i < corredores.length; i++) {
            Correr corredor = new Correr(nombreEquipo, corredores[i]);
            corredor.start();

            // Esperamos a que acabe antes de que salga el siguiente (relevo)
            try {
                corredor.join();
            } catch (InterruptedException e) {
                System.out.println("Interrumpido mientras esperaba");
            }
        }

        System.out.println("FINAL DE PROGRAMA - " + nombreEquipo);
    }

    public static void main(String[] args) {
        Equipo espana = new Equipo("España", new String[]{"Pepe", "Maria", "Juan", "Marta"});
        Equipo eeuu = new Equipo("EEUU", new String[]{"John", "Kim", "Mike", "Sarah"});

        // Los dos equipos corren a la vez
        Thread hiloEspana = new Thread(espana);
        Thread hiloEeuu = new Thread(eeuu);

        hiloEeuu.start();
        hiloEspana.start();
    }
}
