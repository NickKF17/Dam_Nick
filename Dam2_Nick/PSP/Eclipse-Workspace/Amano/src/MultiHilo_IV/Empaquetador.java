package MultiHilo_IV;

public class Empaquetador extends Thread {
	private int tipo;
	private Cola cola;

	public Empaquetador(int tipo, Cola cola) {
		this.tipo = tipo;
		this.cola = cola;
	}

	@Override
	public void run() {
		do {
			try {
				cola.recoger(this.tipo);

				int random = (int) (Math.random() * (500 - 0 + 1) + 0);
				Thread.sleep(random);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}while(cola.getGot()==false);
	}
}
