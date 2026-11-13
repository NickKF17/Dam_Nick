package Hilos2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/*Cada grupo de barcas tenía capacidad para rescatar entre 300-400 soldados
en cada viaje
 Cada grupo de lanchas tenía capacidad para rescatar entre 100-200
soldados en cada viaje
*/
public class SemaforoBarcos {
	public static void main(String[] args) {
		Semaphore semaforosBarcos = new Semaphore(7);
		Playa micola = new Playa(semaforosBarcos);

		ExecutorService miExecutor = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 5; i++) {
			miExecutor.submit(new Navio(0, micola));
		}
		for (int i = 0; i < 5; i++) {
			miExecutor.submit(new Navio(1, micola));
		}
		miExecutor.shutdown();
	}
}
