package Hilos;

import java.util.concurrent.Semaphore;

public class ColaSemaforo {
Semaphore miSemaforo;
public ColaSemaforo(Semaphore miSemaforo) {
	super();
	this.miSemaforo=miSemaforo;
	
}
public void pasar(String nombre) {
	try {
		System.out.println("Esperando semaforo hilo: "+nombre);
		this.miSemaforo.acquire();
		System.out.println("Pasando semaforo hilo: "+nombre);
		System.out.println("Paso 1");
		Thread.sleep(1000);
		System.out.println("Paso 2");
		Thread.sleep(1000);
		System.out.println("Paso 3");
		Thread.sleep(1000);
		
	}catch(InterruptedException e) {
		e.printStackTrace();
	}
	System.out.println("Soltando semafoto hilo: "+nombre);
	this.miSemaforo.release();
}

}