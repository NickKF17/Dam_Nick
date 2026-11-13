package Hilos2;

public class Navio extends Thread {
	private Playa cola;


	private int tipo; // 0=Barco,1=Lancha
	private int cantidad;

	public Navio (int tipo, Playa cola) {
		this.cola=cola;
		this.tipo = tipo;
	}

	public void setCantidad() {
		if (this.tipo == 0) {
			this.cantidad = (int) (Math.random() * (400 - 300 + 1) + 300);
		} else {
			this.cantidad = (int) (Math.random() * (200 - 100 + 1) + 100);
		}
	}

	@Override
    public void run() {
    	do {
    		this.setCantidad();
    		cola.rescatar(this.tipo,this.cantidad);	
    	}while(Playa.getSoldados()>=0);
       
    }
}
