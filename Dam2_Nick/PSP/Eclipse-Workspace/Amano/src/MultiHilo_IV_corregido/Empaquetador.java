package MultiHilo_IV_corregido;

public class Empaquetador extends Thread {
	private int tipo;
	private Cola cola;

	public Empaquetador(int tipo, Cola cola) {
		this.tipo = tipo;
		this.cola = cola;
	}

	@Override
	public void run() {
		try {
			while (cola.recoger(this.tipo)) {
				Thread.sleep((int) (Math.random() * 501)); // empaquetar, fuera del lock
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
