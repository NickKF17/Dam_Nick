package Hilos2;

import java.util.concurrent.Semaphore;
//Lanchas 500-1000 y botes 1,5k-2k
public class Playa {
	private static int soldados = 400000;
	private Semaphore miSemaforo;

	public Playa(Semaphore miSemaforo) {
		super();
		this.miSemaforo = miSemaforo;

	}

	public void rescatar(int tipo,int cantidad) {
		try {
			this.miSemaforo.acquire();
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(tipo==0)
		System.out.println("Han Embarcado "+cantidad+" en Barco" );
		else
			System.out.println("Han Embarcado "+cantidad+" en Lancha");
		
		soldados=soldados-cantidad;
		System.out.println("Quedan "+soldados+" Soldados en la isla");
		this.miSemaforo.release();
	}

	public static int getSoldados() {
		return soldados;
	}
}