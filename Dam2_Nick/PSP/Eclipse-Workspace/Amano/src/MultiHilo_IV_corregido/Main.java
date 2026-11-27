package MultiHilo_IV_corregido;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
	public static void main(String[] args) {
		Cola cola = new Cola();
		Colocador colocador = new Colocador(cola);
		Empaquetador emp1 = new Empaquetador(1, cola);
		Empaquetador emp2 = new Empaquetador(2, cola);
		Empaquetador emp3 = new Empaquetador(3, cola);
		// 4 tareas -> 4 hilos (con 3, el tercer empaquetador no arrancaba nunca)
		ExecutorService es = Executors.newFixedThreadPool(4);
		es.submit(colocador);
		es.submit(emp1);
		es.submit(emp2);
		es.submit(emp3);
		es.shutdown();
	}
}
