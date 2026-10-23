package Ej4;

public class Correr extends Thread {

    private String equipo;
    private String nombre;

    public Correr(String equipo, String nombre) {
        this.equipo = equipo;
        this.nombre = nombre;
    }

    // Simula que el corredor corre 100 metros (10 tramos de 10 metros)
    @Override
    public void run() {
        System.out.println(equipo + ":" + nombre + " comienza su relevo");

        long total = 0; // aqui sumamos los milisegundos que tarda

        for (int i = 0; i < 10; i++) {
            // numero aleatorio entre 950 y 1050
            int ms = 950 + (int) (Math.random() * 101);
            total = total + ms;

            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                System.out.println(nombre + " ha sido interrumpido");
            }
        }

        // pasamos a segundos con 2 decimales
        double segundos = Math.round(total / 10.0) / 100.0;

        System.out.println(equipo + ":" + nombre + " ha acabado su relevo - Ha tardado: " + segundos + " segundos");
    }
}
