package ejercicio2;

// Ejercicio 2: implementando Runnable
public class IronMan implements Runnable {

    private String nombre;
    private double velCorrer; // km/h
    private double velBici;   // km/h
    private double velNadar;  // m/h

    // El primero que acaba (compartido por todos los participantes)
    private static String ganador = null;

    public IronMan(String nombre, double velCorrer, double velBici, double velNadar) {
        this.nombre = nombre;
        this.velCorrer = velCorrer;
        this.velBici = velBici;
        this.velNadar = velNadar;
    }

    // 1 hora = 1000 milisegundos
    private void esperar(double horas) {
        try {
            Thread.sleep((long) (horas * 1000));
        } catch (InterruptedException e) {
            System.out.println(nombre + " ha sido interrumpido");
        }
    }

    public void bici() {
        System.out.println(nombre + " comienza la prueba en bici");
        esperar(180 / velBici); // 180 km
        System.out.println(nombre + " ha acabado la prueba en bici");
    }

    public void marathon() {
        System.out.println(nombre + " comienza la marathon");
        esperar(42 / velCorrer); // 42 km
        System.out.println(nombre + " ha acabado la marathon");
    }

    public void nadar() {
        System.out.println(nombre + " comienza la prueba nadando");
        esperar(4000 / velNadar); // 4000 m
        System.out.println(nombre + " ha acabado la prueba nadando");
    }

    // Solo un hilo a la vez puede mirar/cambiar el ganador
    private static synchronized void terminar(String nombre) {
        if (ganador == null) {
            ganador = nombre;
        }
    }

    // Enlaza las pruebas una detras de otra
    @Override
    public void run() {
        bici();
        marathon();
        nadar();
        System.out.println(nombre + " ha terminado el IronMan");
        terminar(nombre);
    }

    public static void main(String[] args) throws InterruptedException {
        Thread pedro = new Thread(new IronMan("Pedro", 6, 20, 1000));
        Thread juan = new Thread(new IronMan("Juan", 7, 18, 2000));
        Thread vicente = new Thread(new IronMan("Vicente", 6, 30, 1000));

        pedro.start();
        vicente.start();
        juan.start();

        // Esperamos a que acaben los tres
        pedro.join();
        juan.join();
        vicente.join();

        System.out.println("El ganador es: " + ganador);
    }
}
