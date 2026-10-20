package com.example.lightsout

import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // Elementos de la pantalla
    private lateinit var tablero: GridLayout
    private lateinit var txtMovimientos: TextView
    private lateinit var cronometro: Chronometer

    // Los botones del tablero (se crean desde el código según el tamaño)
    private val botones = ArrayList<Button>()

    // Tamaño del tablero: 3, 4 o 5 (por defecto 3x3)
    private var tamano = 3

    // Estado de cada casilla: false = color primario, true = color secundario
    private var estado = BooleanArray(9)

    // Los dos colores del juego
    private val colorPrimario = Color.DKGRAY
    private val colorSecundario = Color.MAGENTA

    // Cuántas veces ha pulsado el jugador
    private var movimientos = 0

    // Será true cuando se gane (así no se puede seguir pulsando)
    private var terminado = false

    // Para los sonidos
    private lateinit var tono: ToneGenerator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cogemos los elementos de la pantalla
        tablero = findViewById(R.id.tablero)
        txtMovimientos = findViewById(R.id.txtMovimientos)
        cronometro = findViewById(R.id.cronometro)
        val spinnerTamano = findViewById<Spinner>(R.id.spinnerTamano)
        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar)
        val btnNuevaPartida = findViewById<Button>(R.id.btnNuevaPartida)

        tono = ToneGenerator(AudioManager.STREAM_MUSIC, 80)

        // Rellenamos el desplegable con los tamaños
        val opciones = arrayOf("3 x 3", "4 x 4", "5 x 5")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinnerTamano.adapter = adaptador

        // Cuando se elige un tamaño, se crea el tablero de ese tamaño.
        // Esto también se ejecuta al abrir la app, así que crea el primer tablero.
        spinnerTamano.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tamano = position + 3   // posición 0 -> 3, 1 -> 4, 2 -> 5
                crearTablero()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        btnReiniciar.setOnClickListener {
            reiniciar()
        }

        btnNuevaPartida.setOnClickListener {
            nuevaPartida()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tono.release()
    }

    // Crea los botones del tablero según el tamaño elegido
    private fun crearTablero() {
        tablero.removeAllViews()
        tablero.rowCount = tamano
        tablero.columnCount = tamano
        botones.clear()
        estado = BooleanArray(tamano * tamano)

        for (i in 0 until tamano * tamano) {
            val fila = i / tamano
            val columna = i % tamano

            val boton = Button(this)

            // Cada botón ocupa una casilla y se reparte el espacio a partes iguales
            val parametros = GridLayout.LayoutParams(GridLayout.spec(fila, 1f), GridLayout.spec(columna, 1f))
            parametros.width = 0
            parametros.height = 0
            parametros.setMargins(4, 4, 4, 4)
            boton.layoutParams = parametros

            boton.setOnClickListener {
                pulsar(i)
            }

            tablero.addView(boton)
            botones.add(boton)
        }

        reiniciar()
    }

    // Se llama cuando el jugador pulsa un botón. posicion va de 0 a (tamano*tamano - 1)
    private fun pulsar(posicion: Int) {
        if (terminado) {
            return
        }

        // Al primer movimiento arranca el tiempo
        if (movimientos == 0) {
            cronometro.base = SystemClock.elapsedRealtime()
            cronometro.start()
        }

        cambiarConVecinos(posicion)

        movimientos++
        txtMovimientos.text = "Movimientos: $movimientos"

        vibrar(40)
        tono.startTone(ToneGenerator.TONE_PROP_BEEP, 80)

        comprobarVictoria()
    }

    // Cambia la casilla indicada y sus vecinos (arriba, abajo, izquierda y derecha)
    private fun cambiarConVecinos(posicion: Int) {
        val fila = posicion / tamano
        val columna = posicion % tamano

        cambiar(posicion)

        // Solo cambiamos un vecino si existe, para no salirnos del tablero
        if (fila > 0) {
            cambiar(posicion - tamano)   // el de arriba
        }
        if (fila < tamano - 1) {
            cambiar(posicion + tamano)   // el de abajo
        }
        if (columna > 0) {
            cambiar(posicion - 1)        // el de la izquierda
        }
        if (columna < tamano - 1) {
            cambiar(posicion + 1)        // el de la derecha
        }
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

    // Devuelve true si las casillas están todas en color secundario
    private fun estanTodasEncendidas(): Boolean {
        var todas = true

        for (i in 0 until estado.size) {
            if (!estado[i]) {
                todas = false
            }
        }

        return todas
    }

    private fun comprobarVictoria() {
        if (estanTodasEncendidas()) {
            terminado = true
            cronometro.stop()

            Toast.makeText(this, "¡Has ganado en $movimientos movimientos!", Toast.LENGTH_LONG).show()
            vibrar(300)
            tono.startTone(ToneGenerator.TONE_CDMA_CONFIRM, 600)
        }
    }

    //Vuelve todo al estado inicial (todos en color primario) y pone a cero el contador y el tiempo
    private fun reiniciar() {
        for (i in 0 until estado.size) {
            estado[i] = false
            pintar(i)
        }

        ponerAcero()
    }

    // Genera un tablero aleatorio que SIEMPRE tiene solución.
    // Truco: partimos del tablero ya resuelto (todo en secundario) y hacemos clics al azar.
    // Como cada clic se deshace repitiéndolo, repitiendo esos mismos clics se vuelve a resolver.
    private fun nuevaPartida() {
        for (i in 0 until estado.size) {
            estado[i] = true
        }

        // N clics aleatorios: tantos como casillas tiene el tablero
        val clics = tamano * tamano
        for (i in 0 until clics) {
            cambiarConVecinos(Random.nextInt(estado.size))
        }

        // Si por casualidad ha quedado resuelto, hacemos más clics hasta que no lo esté
        while (estanTodasEncendidas()) {
            cambiarConVecinos(Random.nextInt(estado.size))
        }

        for (i in 0 until estado.size) {
            pintar(i)
        }

        ponerAcero()
    }

    // Pone a cero los movimientos y el tiempo
    private fun ponerAcero() {
        movimientos = 0
        terminado = false
        txtMovimientos.text = "Movimientos: 0"
        cronometro.stop()
        cronometro.base = SystemClock.elapsedRealtime()
    }

    // Hace vibrar el móvil los milisegundos indicados
    @Suppress("DEPRECATION")
    private fun vibrar(milisegundos: Long) {
        val vibrador = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrador.vibrate(VibrationEffect.createOneShot(milisegundos, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}
