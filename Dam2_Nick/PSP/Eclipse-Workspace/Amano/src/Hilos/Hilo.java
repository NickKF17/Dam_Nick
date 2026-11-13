package Hilos;

public class Hilo extends Thread {
    ColaSemaforo cola;
    
    public Hilo(ColaSemaforo cola) {
    	super();
        this.cola = cola;
    }

    @Override
    public void run() {
       cola.pasar(getName());
    }
}