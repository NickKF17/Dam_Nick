package ejercicio4;

// Copia de la clase del ejercicio 3 (sin el main): cada corredor es un hilo
public class Correr extends Thread {

    private String equipo;
    private String nombre;

    public Correr(String equipo, String nombre) {
        this.equipo = equipo;
        this.nombre = nombre;
    }

    // Simula correr 100 metros (10 tramos de 10 metros)
    @Override
    public void run() {
        System.out.println(equipo + ":" + nombre + " comienza su relevo");

        long total = 0;
        for (int i = 0; i < 10; i++) {
            // Cada 10 metros tarda entre 950 y 1050 milisegundos
            int tiempo = 950 + (int) (Math.random() * 101);
            try {
                Thread.sleep(tiempo);
            } catch (InterruptedException e) {
                System.out.println(nombre + " ha sido interrumpido");
            }
            total = total + tiempo;
        }

        // Pasamos a segundos con 2 decimales
        double segundos = Math.round(total / 10.0) / 100.0;
        System.out.println(equipo + ":" + nombre + " ha acabado su relevo - Ha tardado: " + segundos + " segundos");
    }
}
