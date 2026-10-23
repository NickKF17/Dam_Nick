package Ej2;

public class IronMan extends Thread {

    private String nombre;
    private double velCorrer;  // km/h
    private double velBici;    // km/h
    private double velNadar;   // m/h

    // Aqui guardamos el nombre del ganador (es static para que lo compartan todos los hilos)
    private static String ganador = null;

    public IronMan(String nombre, double velCorrer, double velBici, double velNadar) {
        this.nombre = nombre;
        this.velCorrer = velCorrer;
        this.velBici = velBici;
        this.velNadar = velNadar;
    }

    // Metodo auxiliar para esperar un tiempo en milisegundos
    private void esperar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            System.out.println(nombre + " ha sido interrumpido");
        }
    }

    // Prueba de bici: 180 km
    public void bici() {
        System.out.println(nombre + " comienza la prueba en bici");
        // tiempo en horas = km / velocidad, y 1 hora = 1000 ms
        esperar((long) (180 / velBici * 1000));
        System.out.println(nombre + " ha acabado la prueba en bici");
    }

    // Prueba de marathon: 42 km
    public void marathon() {
        System.out.println(nombre + " comienza la marathon");
        esperar((long) (42 / velCorrer * 1000));
        System.out.println(nombre + " ha acabado la marathon");
    }

    // Prueba de natacion: 4000 metros
    public void nadar() {
        System.out.println(nombre + " comienza la prueba de natación");
        esperar((long) (4000 / velNadar * 1000));
        System.out.println(nombre + " ha acabado la prueba de natación");
    }

    // synchronized para que dos hilos no entren a la vez
    private static synchronized void llegarAMeta(String nombre) {
        if (ganador == null) {
            ganador = nombre;
            System.out.println(">>> " + nombre + " llega el primero a meta: ¡GANADOR!");
        } else {
            System.out.println(nombre + " llega a meta");
        }
    }

    // Aqui vamos enlazando las pruebas una detras de otra
    @Override
    public void run() {
        bici();
        marathon();
        nadar();
        llegarAMeta(nombre);
    }

    public static void main(String[] args) {
        IronMan pedro = new IronMan("Pedro", 6, 20, 1000);
        IronMan juan = new IronMan("Juan", 7, 18, 2000);
        IronMan vicente = new IronMan("Vicente", 6, 30, 1000);

        // Todos empiezan a la vez
        pedro.start();
        vicente.start();
        juan.start();
    }
}
