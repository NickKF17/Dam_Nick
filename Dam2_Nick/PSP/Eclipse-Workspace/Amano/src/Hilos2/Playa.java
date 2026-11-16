package Hilos2;

import java.util.concurrent.Semaphore;

//Lanchas 500-1000 y botes 1,5k-2k
public class Playa {
	private static int contador;
	private static int soldados = 400000;
	private Semaphore miSemaforo;
	private static int tiempo2 = 0;

	public Playa(Semaphore miSemaforo) {
		super();
		this.miSemaforo = miSemaforo;

	}

	public void rescatar(int tipo, int cantidad) {
		try {
			this.miSemaforo.acquire();
			restaSoldados(tipo, cantidad);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		this.miSemaforo.release();
	}

	private static synchronized void restaSoldados(int tipo, int cantidad) throws InterruptedException {
		if(Navio.getTiempo()<6*24000) {
		soldados -= cantidad;
		
		if (tipo == 0)
			System.out.println("Han Embarcado " + cantidad + " en Barco");
		else
			System.out.println("Han Embarcado " + cantidad + " en Lancha");

		System.out.println("Quedan " + soldados + " Soldados en la isla");
	
		contador++;
		if (contador == 7) {
			contador = 0;
			Navio.anyadirtiempo(900);
			tiempo2 += 900;
			//Thread.sleep(900);

			if (Navio.getTiempo() >= 6 * 24000) {
				System.out.println("Operacion Dinamo Finalizada");
				System.out.println("Se han salvado " + (400000 - soldados) + " soldados");
				System.out.println("Quedan por salvar " + soldados);
			}
		}

		if (tiempo2 > 24000) {
			System.out.println("Han pasado " + (Navio.getTiempo() / 24000) + " dias");
			tiempo2 = 0;
		}
		}
	}

	public static int getSoldados() {
		return soldados;
	}
}