package def;

public class Main {

    public static void main(String[] args) {

        int numeroJugadores = 3;

        // El arbitro lo compartimos entre todos los jugadores
        Arbitro arbitro = new Arbitro(numeroJugadores);

        for (int i = 1; i <= numeroJugadores; i++) {
            Jugador jugador = new Jugador(i, arbitro);
            jugador.start();
        }
    }
}
