package MultiHilo_IV_corregido;

import java.util.Arrays;

public class Cola {
	private boolean colocadorTerminado = false;
	private Integer[] cadena = { 0, 0, 0 };

	public synchronized void producir() throws InterruptedException {
		// La posicion se recalcula en cada vuelta: si no, tras el wait() se
		// seguiria usando un valor antiguo y el hilo no despertaria nunca.
		int posicion;
		while ((posicion = devolverPosicionCero()) == 3) {
			wait();
		}

		int random = (int) (Math.random() * 3 + 1);
		cadena[posicion] = random;
		System.out.println("Produciendo tipo " + random);
		System.out.println("Coloco un producto " + random + " en la posicion " + (posicion + 1));
		System.out.println(Arrays.toString(cadena));
		notifyAll();
	}

	/**
	 * Espera a que haya un producto de su tipo y lo recoge.
	 * @return true si recogio algo; false si el colocador ya termino y no
	 *         quedan productos de ese tipo (el empaquetador puede acabar).
	 */
	public synchronized boolean recoger(int tipo) throws InterruptedException {
		while (!hayTipo(tipo)) {
			if (colocadorTerminado)
				return false;
			wait();
		}

		for (int i = 0; i < cadena.length; i++) {
			if (cadena[i] == tipo) {
				cadena[i] = 0;
				break;
			}
		}
		System.out.println("Recogido producto " + tipo + "  " + Arrays.toString(cadena));
		notifyAll(); // avisa al colocador de que hay hueco
		return true;
	}

	private boolean hayTipo(int tipo) {
		for (Integer c : cadena)
			if (c == tipo)
				return true;
		return false;
	}

	// Devuelve el primer hueco libre (3 si esta llena)
	private int devolverPosicionCero() {
		for (int i = 0; i < cadena.length; i++) {
			if (cadena[i] == 0)
				return i;
		}
		return 3;
	}

	public synchronized void setColocadorTerminado() {
		this.colocadorTerminado = true;
		notifyAll(); // despierta a los empaquetadores que esperan
	}
}
