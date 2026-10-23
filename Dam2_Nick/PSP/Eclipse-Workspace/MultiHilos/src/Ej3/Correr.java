package Ej3;

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

    public static void main(String[] args) {
        String[] nombres = {"Pepe", "Maria", "Juan", "Marta"};

        for (int i = 0; i < nombres.length; i++) {
            Correr corredor = new Correr("España", nombres[i]);
            corredor.start();

            // join hace que el main espere a que acabe este corredor
            // antes de pasar al siguiente (asi hay relevo)
            try {
                corredor.join();
            } catch (InterruptedException e) {
                System.out.println("Interrumpido mientras esperaba");
            }
        }

        System.out.println("FINAL DE PROGRAMA");
    }
}
