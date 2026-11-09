package def;
public class Arbitro {

    private int totalJugadores;
    private int turno;
    private int numeroAdivinar;
    private boolean juegoAcabado;

    public Arbitro(int totalJugadores) {
        this.totalJugadores = totalJugadores;
        this.turno = 1; // empieza el jugador 1
        this.numeroAdivinar = 1 + (int) (10 * Math.random());
        this.juegoAcabado = false;

        System.out.println("NÚMERO A ADIVINAR: " + numeroAdivinar);
    }

    // Devuelve a quien le toca
    public synchronized int getTurno() {
        return turno;
    }

    // Indica si el juego se ha acabado
    public synchronized boolean isJuegoAcabado() {
        return juegoAcabado;
    }

    // Comprueba la jugada. Es synchronized: mientras un jugador esta
    // jugando, ningun otro puede entrar en este metodo.
    public synchronized void jugada(int idJugador, int numero) {

        // Por si el juego ya habia acabado o no era su turno
        if (juegoAcabado || idJugador != turno) {
            return;
        }

        System.out.println("Jugador" + idJugador + " dice: " + numero);

        if (numero == numeroAdivinar) {
            juegoAcabado = true;
            System.out.println("   Jugador " + idJugador + " gana, adivinó el número!!!");
        } else {
            // Siguiente turno (despues del ultimo vuelve al 1)
            turno = turno + 1;
            if (turno > totalJugadores) {
                turno = 1;
            }
            System.out.println("\tLe toca a Jug" + turno);
        }
    }
}
