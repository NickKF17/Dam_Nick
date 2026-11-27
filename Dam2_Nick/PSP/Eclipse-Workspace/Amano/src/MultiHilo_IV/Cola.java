package MultiHilo_IV;

import java.util.Arrays;

public class Cola {
	private boolean recoger = false;
	private boolean colocadorTerminado = false;
	private Integer[] cadena = { 0, 0, 0 };
	private Integer[] cadenaContador = { 0, 0, 0 };
	private boolean got = false;
	private int contador = 0;

	public synchronized void producir() throws InterruptedException {
		while (this.devolverPosicionCero() == 3) {

			wait();
		}

		int random = (int) (Math.random() * (3 - 1 + 1) + 1);
		cadena[this.devolverPosicionCero()] = random;
		System.out.println("Produciendo tipo " + random);
		System.out.println("Coloco un producto " + random + " en la posicion " + (this.devolverPosicionCero() + 1));
		System.out.println(Arrays.toString(cadena));
		recoger = true;
		notifyAll();
	}

	public synchronized void recoger(int tipo) throws InterruptedException {

		while (recoger == false) {
			System.out.println("hola");
			wait();
		}
		boolean ola = false;

		for (int i = 0; i < cadena.length; i++) {
			if (cadena[i] == tipo) {
				cadena[i] = 0;
				cadenaContador[i]++;
				ola = true;
				break;
			}
		}
		Thread.sleep(100);
		if (ola == true)
			System.out.println("Recogido producto " + tipo);

		if (this.colocadorTerminado == true && this.todoCero() == true) {
			got = true;
			if (contador == 0) {
				System.out.println("Quedan en la cinta " + Arrays.toString(cadena));
				System.out.println("Puestos en total 20");
				System.out.println("Recogidos por tipo:" + Arrays.toString(cadenaContador));
				contador++;
			}
		}

		notifyAll();
	}

	public int devolverPosicionCero() {
		int posicion = 3;
		for (int i = 0; i < cadena.length; i++) {
			if (cadena[i] == 0)
				posicion = i;

		}

		return posicion;
	}

	public boolean todoCero() {
		int contador = 0;
		for (int i = 0; i < cadena.length; i++) {
			if (cadena[i] == 0)
				contador++;

		}
		return contador == 3;

	}

	public void setColocadorTerminado() {
		this.colocadorTerminado = true;
	}

	public boolean getEstadoColocador() {
		return this.colocadorTerminado;
	}

	public boolean getGot() {
		return this.got;
	}
}
