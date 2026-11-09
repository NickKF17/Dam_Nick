package def;
public class Jugador extends Thread {

    private int id;
    private Arbitro arbitro;

    public Jugador(int id, Arbitro arbitro) {
        this.id = id;
        this.arbitro = arbitro;
    }

    @Override
    public void run() {
        // Repetimos hasta que se acabe el juego
        while (!arbitro.isJuegoAcabado()) {

            // Solo jugamos si es nuestro turno
            if (arbitro.getTurno() == id) {
                int numero = 1 + (int) (10 * Math.random());
                arbitro.jugada(id, numero);
            }
        }
    }
}
