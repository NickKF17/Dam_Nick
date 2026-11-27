package MultiHilo_IV_corregido
;

public class Colocador extends Thread {
	private Cola cola;

	public Colocador(Cola cola) {
		this.cola = cola;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < 20; i++) {
				cola.producir();
				Thread.sleep((int) (Math.random() * 251));
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			this.cola.setColocadorTerminado();
		}
	}
}
