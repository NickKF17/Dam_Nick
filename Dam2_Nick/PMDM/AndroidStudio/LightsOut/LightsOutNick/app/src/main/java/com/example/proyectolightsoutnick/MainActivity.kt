package com.example.proyectolightsoutnick

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

// Pantalla única del juego Lights Out.
// Objetivo: dejar las 9 casillas en morado. Al pulsar una casilla, cambia ella
// y sus vecinas (arriba, abajo, izquierda y derecha): gris -> morado y morado -> gris.
class MainActivity : AppCompatActivity() {

    // Lista con los 9 botones del tablero. El número es su posición en la lista:
    //   0 1 2
    //   3 4 5
    //   6 7 8
    // "lateinit" significa que la rellenamos más tarde (en onCreate), no al declararla.
    private lateinit var botones: List<Button>

    // Guarda el estado lógico de cada casilla (el color es solo su reflejo visual):
    // false = gris (apagada), true = morado (activada)
    private val estado = BooleanArray(9)

    // Aquí guardamos los dos colores de colors.xml(como números enteros)
    private var colorPrimario = 0     // gris
    private var colorSecundario = 0   // morado

    // onCreate se ejecuta una sola vez, cuando se abre la pantalla: aquí se prepara todo.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // carga el diseño de activity_main.xml

        // Leemos los colores definidos en res/values/colors.xml
        colorPrimario = ContextCompat.getColor(this, R.color.primario)
        colorSecundario = ContextCompat.getColor(this, R.color.secundario)

        // Buscamos en el layout los 9 botones por su id (b0..b8) y los metemos
        // en la lista en orden, para poder acceder a ellos por posición.
        botones = listOf(
            findViewById(R.id.b0), findViewById(R.id.b1), findViewById(R.id.b2),
            findViewById(R.id.b3), findViewById(R.id.b4), findViewById(R.id.b5),
            findViewById(R.id.b6), findViewById(R.id.b7), findViewById(R.id.b8)
        )

        // Recorremos los 9 botones y a cada uno le asignamos qué hacer al pulsarlo:
        // llamar a pulsar() indicando su posición (i).
        for (i in 0..8) {
            botones[i].setOnClickListener {
                pulsar(i)
            }
        }

        // Botón Reiniciar: al pulsarlo vuelve todo al estado inicial
        findViewById<Button>(R.id.btnReiniciar).setOnClickListener {
            reiniciar()
        }

        reiniciar() // al abrir la app dejamos el tablero en su estado inicial (todo gris)
    }

    // Lógica de una jugada: se ejecuta al pulsar la casilla número "i"
    private fun pulsar(i: Int) {
        // Pasamos de posición (0..8) a fila y columna con división y resto:
        // por ejemplo, la 5 -> fila 5/3 = 1, columna 5%3 = 2
        val fila = i / 3      // 0, 1 o 2
        val columna = i % 3   // 0, 1 o 2

        cambiar(i)                              // la casilla pulsada
        // Las vecinas solo se cambian si existen (para no salirnos del tablero):
        if (fila > 0) cambiar(i - 3)            // la de arriba (una fila menos = 3 posiciones atrás)
        if (fila < 2) cambiar(i + 3)            // la de abajo
        if (columna > 0) cambiar(i - 1)         // la de la izquierda
        if (columna < 2) cambiar(i + 1)         // la de la derecha

        pintar() // actualizamos los colores en pantalla con el nuevo estado

        // Comprobamos si con esta jugada se ha ganado
        if (hayVictoria()) {
            Toast.makeText(this, "Has Ganado", Toast.LENGTH_LONG).show()
        }
    }

    // Invierte el estado de una casilla: de gris a morado y de morado a gris
    // (el operador ! convierte true en false y false en true)
    private fun cambiar(i: Int) {
        estado[i] = !estado[i]
    }

    // Recorre las 9 casillas y pone a cada botón el color que le corresponde
    // según su estado: morado si está activada, gris si no.
    private fun pintar() {
        for (i in 0..8) {
            if (estado[i]) {
                botones[i].setBackgroundColor(colorSecundario)
            } else {
                botones[i].setBackgroundColor(colorPrimario)
            }
        }
    }

    // Devuelve true si se ha ganado, es decir, si TODAS las casillas están en morado.
    // Si encuentra aunque sea una en gris, devuelve false enseguida.
    private fun hayVictoria(): Boolean {
        for (i in 0..8) {
            if (!estado[i]) return false
        }
        return true
    }

    // Pone todas las casillas en gris (estado false) y repinta el tablero
    private fun reiniciar() {
        for (i in 0..8) {
            estado[i] = false
        }
        pintar()
    }
}
