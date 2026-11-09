package ejercicio4;

// Ejercicio 4: implementando Runnable
// Cada equipo es un hilo que lanza a sus corredores de uno en uno
public class Equipo implements Runnable {

    private String nombre;
    private String[] corredores;

    public Equipo(String nombre, String[] corredores) {
        this.nombre = nombre;
        this.corredores = corredores;
    }

    @Override
    public void run() {
        for (int i = 0; i < corredores.length; i++) {
            Correr corredor = new Correr(nombre, corredores[i]);
            corredor.start();
            try {
                corredor.join(); // hasta que no acaba, no sale el siguiente
            } catch (InterruptedException e) {
                System.out.println("Relevo interrumpido");
            }
        }
        System.out.println("FINAL DE PROGRAMA - " + nombre);
    }

    public static void main(String[] args) {
        String[] espana = { "Pepe", "Maria", "Juan", "Marta" };
        String[] eeuu = { "John", "Kim", "Mike", "Sarah" };

        Thread equipoEspana = new Thread(new Equipo("España", espana));
        Thread equipoEEUU = new Thread(new Equipo("EEUU", eeuu));

        // Los dos equipos salen a la vez
        equipoEEUU.start();
        equipoEspana.start();
    }
}
