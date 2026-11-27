package MultiHilo_IV;

public class Colocador extends Thread {
	private Cola cola;

	public Colocador(Cola cola) {
		this.cola = cola;

	}

	@Override
	public void run() {
		for (int i = 0; i < 20; i++) {
			try {
				cola.producir();
				int random = (int) (Math.random() * (250 + 1));
				Thread.sleep(random);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.cola.setColocadorTerminado();
	}
}
