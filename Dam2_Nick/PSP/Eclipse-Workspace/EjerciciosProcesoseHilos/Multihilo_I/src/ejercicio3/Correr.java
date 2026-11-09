package ejercicio3;

// Ejercicio 3: heredando de Thread
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

    public static void main(String[] args) throws InterruptedException {
        String[] nombres = { "Pepe", "Maria", "Juan", "Marta" };

        for (int i = 0; i < nombres.length; i++) {
            Correr corredor = new Correr("España", nombres[i]);
            corredor.start();
            // join: esperamos a que acabe antes de lanzar al siguiente (el relevo)
            corredor.join();
        }

        System.out.println("FINAL DE PROGRAMA");
    }
}
