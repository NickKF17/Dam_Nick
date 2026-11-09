// La pista es el recurso compartido: solo puede despegar un aeroplano a la vez
package def;

public class Pista {

    private boolean ocupada = false;
    private int avionesEsperando = 0;

    // El aeroplano espera hasta poder entrar en la pista
    public synchronized void entrar(boolean esAvion) throws InterruptedException {

        // Los aviones se apuntan en la lista de espera
        if (esAvion) {
            avionesEsperando++;
        }

        // Espera si la pista esta ocupada.
        // Las avionetas tambien esperan si hay aviones esperando (prioridad).
        while (ocupada || (!esAvion && avionesEsperando > 0)) {
            wait();
        }

        ocupada = true;
        if (esAvion) {
            avionesEsperando--;
        }
    }

    // Cuando acaba el despegue (y la turbulencia) se libera la pista
    public synchronized void salir() {
        ocupada = false;
        notifyAll(); // avisamos a todos los que esperan
    }
}
