package com.example.lightsout

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Los 9 botones del tablero (posiciones 0 a 8)
    private val botones = ArrayList<Button>()

    // Estado de cada casilla: false = color primario, true = color secundario
    private val estado = BooleanArray(9)

    // Los dos colores del juego
    private val colorPrimario = Color.DKGRAY
    private val colorSecundario = Color.MAGENTA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Guardamos los botones en la lista, en orden
        botones.add(findViewById(R.id.btn0))
        botones.add(findViewById(R.id.btn1))
        botones.add(findViewById(R.id.btn2))
        botones.add(findViewById(R.id.btn3))
        botones.add(findViewById(R.id.btn4))
        botones.add(findViewById(R.id.btn5))
        botones.add(findViewById(R.id.btn6))
        botones.add(findViewById(R.id.btn7))
        botones.add(findViewById(R.id.btn8))

        // A cada botón le decimos qué hacer al pulsarlo
        for (i in 0 until 9) {
            botones[i].setOnClickListener {
                pulsar(i)
            }
        }

        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar)
        btnReiniciar.setOnClickListener {
            reiniciar()
        }

        // Al empezar, todos en color primario
        reiniciar()
    }

    // Se llama cuando pulsamos un botón. posicion va de 0 a 8
    private fun pulsar(posicion: Int) {
        val fila = posicion / 3
        val columna = posicion % 3

        // Cambia el botón pulsado
        cambiar(posicion)

        // Cambia los vecinos (solo si existen, para no salirnos del tablero)
        if (fila > 0) {
            cambiar(posicion - 3)   // el de arriba
        }
        if (fila < 2) {
            cambiar(posicion + 3)   // el de abajo
        }
        if (columna > 0) {
            cambiar(posicion - 1)   // el de la izquierda
        }
        if (columna < 2) {
            cambiar(posicion + 1)   // el de la derecha
        }

        comprobarVictoria()
    }

    // Cambia el estado de una casilla y la vuelve a pintar
    private fun cambiar(posicion: Int) {
        estado[posicion] = !estado[posicion]
        pintar(posicion)
    }

    // Pone el color del botón según su estado
    private fun pintar(posicion: Int) {
        if (estado[posicion]) {
            botones[posicion].setBackgroundColor(colorSecundario)
        } else {
            botones[posicion].setBackgroundColor(colorPrimario)
        }
    }

    // Si los 9 están en color secundario, has ganado
    private fun comprobarVictoria() {
        var ganado = true

        for (i in 0 until 9) {
            if (!estado[i]) {
                ganado = false
            }
        }

        if (ganado) {
            Toast.makeText(this, "¡Has ganado!", Toast.LENGTH_LONG).show()
        }
    }

    // Vuelve todo al estado inicial (todos en color primario)
    private fun reiniciar() {
        for (i in 0 until 9) {
            estado[i] = false
            pintar(i)
        }
    }
}
