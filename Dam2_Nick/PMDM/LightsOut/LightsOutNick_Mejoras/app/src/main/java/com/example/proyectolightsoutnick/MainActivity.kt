package com.example.proyectolightsoutnick

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

// Pantalla única del juego Lights Out.
// Objetivo: dejar todas las casillas en morado. Al pulsar una casilla, cambia ella
// y sus vecinas (arriba, abajo, izquierda y derecha): gris -> morado y morado -> gris.
// El tablero puede ser de 3x3, 4x4 o 5x5 (se elige en el Spinner).
class MainActivity : AppCompatActivity() {

    // Tamaño actual del tablero: 3, 4 o 5 (casillas por lado)
    private var tamano = 3

    // Lista con los botones del tablero. El número es su posición en la lista.
    // Ejemplo con tamaño 3:
    //   0 1 2
    //   3 4 5
    //   6 7 8
    // Empieza vacía: se rellena en prepararTablero() con los botones del tablero elegido.
    private var botones = ArrayList<Button>()

    // Estado lógico de cada casilla: false = gris (apagada), true = morado (activada).
    // Se crea de nuevo cuando cambia el tamaño, por eso es "var" y empieza vacío.
    private var estado = BooleanArray(0)

    // Copia del tablero con el que empezó la partida, para poder usar "Reiniciar"
    private var estadoInicial = BooleanArray(0)

    // Los dos colores leídos de colors.xml (como números enteros)
    private var colorPrimario = 0     // gris
    private var colorSecundario = 0   // morado

    // Contador de pulsaciones de la partida actual
    private var movimientos = 0

    // Segundos que han pasado desde que empezó la partida
    private var segundos = 0

    // Pasa a true cuando se gana, para que ya no se pueda seguir jugando
    private var juegoTerminado = false

    // Elementos de la pantalla que usaremos varias veces
    // Los tres tableros del layout (3x3, 4x4 y 5x5). Solo se ve uno a la vez.
    private lateinit var tablero3: GridLayout
    private lateinit var tablero4: GridLayout
    private lateinit var tablero5: GridLayout
    private lateinit var txtMovimientos: TextView
    private lateinit var txtTiempo: TextView

    // Handler: sirve para ejecutar una tarea pasado un tiempo. Lo usamos como
    // cronómetro: cada segundo se ejecuta "tareaCronometro", que suma 1 y se vuelve a programar.
    private val handler = Handler(Looper.getMainLooper())
    private val tareaCronometro = object : Runnable {
        override fun run() {
            segundos++
            mostrarTiempo()
            handler.postDelayed(this, 1000) // volver a ejecutarme dentro de 1 segundo
        }
    }

    // Para hacer sonidos cortos (pitidos)
    private var sonidos: ToneGenerator? = null

    // Para hacer vibrar el móvil
    private var vibrador: Vibrator? = null

    // onCreate se ejecuta una sola vez, cuando se abre la pantalla: aquí se prepara todo.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // carga el diseño de activity_main.xml

        // Leemos los colores definidos en res/values/colors.xml
        colorPrimario = ContextCompat.getColor(this, R.color.primario)
        colorSecundario = ContextCompat.getColor(this, R.color.secundario)

        // Buscamos los elementos fijos del layout por su id
        tablero3 = findViewById(R.id.tablero3)
        tablero4 = findViewById(R.id.tablero4)
        tablero5 = findViewById(R.id.tablero5)
        txtMovimientos = findViewById(R.id.txtMovimientos)
        txtTiempo = findViewById(R.id.txtTiempo)

        // Preparamos el sonido (volumen 80 de 100) y el vibrador del móvil
        sonidos = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        vibrador = getSystemService(VIBRATOR_SERVICE) as Vibrator

        // Botón Nueva partida: genera un tablero aleatorio nuevo
        findViewById<Button>(R.id.btnNuevaPartida).setOnClickListener {
            nuevaPartida()
        }

        // Botón Reiniciar: vuelve al tablero con el que empezó la partida
        findViewById<Button>(R.id.btnReiniciar).setOnClickListener {
            reiniciar()
        }

        // Spinner de tamaños: le ponemos las tres opciones
        val spinner = findViewById<Spinner>(R.id.spinnerTamano)
        val opciones = listOf("3 x 3", "4 x 4", "5 x 5")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)

        // Cuando el usuario elige una opción: la posición 0 es 3x3, la 1 es 4x4 y la 2 es 5x5,
        // así que el tamaño es posición + 3. Después empezamos una partida de ese tamaño.
        // (Este listener también se ejecuta al abrir la app, así que ahí empieza la primera partida.)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tamano = position + 3
                prepararTablero()
                nuevaPartida()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacemos nada, pero hay que escribir esta función obligatoriamente
            }
        }
    }

    // Muestra el tablero del tamaño elegido, oculta los otros dos y recoge sus botones.
    // Los botones están ya creados en activity_main.xml con ids t3_0.., t4_0.. y t5_0..
    private fun prepararTablero() {
        // Solo se ve el tablero del tamaño elegido; los demás se esconden con GONE
        tablero3.visibility = if (tamano == 3) View.VISIBLE else View.GONE
        tablero4.visibility = if (tamano == 4) View.VISIBLE else View.GONE
        tablero5.visibility = if (tamano == 5) View.VISIBLE else View.GONE

        botones.clear() // vaciamos la lista de botones del tablero anterior
        for (i in 0 until tamano * tamano) {
            // Construimos el nombre del id, por ejemplo "t4_7" (tablero 4x4, botón 7),
            // y buscamos su número de id en los recursos para poder hacer findViewById.
            val id = resources.getIdentifier("t${tamano}_$i", "id", packageName)
            val boton = findViewById<Button>(id)

            // Al pulsar este botón llamamos a pulsar() con su posición (i)
            boton.setOnClickListener {
                pulsar(i)
            }

            botones.add(boton) // lo añadimos a nuestra lista, en orden
        }
    }

    // Empieza una partida nueva con un tablero aleatorio que siempre tiene solución.
    private fun nuevaPartida() {
        estado = BooleanArray(tamano * tamano)

        // Para garantizar que tenga solución partimos del tablero ya ganado
        // todo morado y hacemos clics aleatorios. Como cada clic se deshace
        // repitiéndolo, basta con repetir esos mismos clics para volver a ganar.
        // Repetimos por si, por casualidad, los clics se anulan entre sí y el
        // tablero queda ya ganado (no tendría gracia).
        do {
            for (i in estado.indices) {
                estado[i] = true
            }
            val clics = tamano * tamano  // N = número de clics aleatorios
            for (c in 1..clics) {
                val casilla = Random.nextInt(tamano * tamano) // número al azar entre 0 y casillas-1
                cambiarVecinas(casilla)
            }
        } while (hayVictoria())

        // Guardamos una copia para poder reiniciar luego (copyOf hace una copia del array)
        estadoInicial = estado.copyOf()

        empezarPartida()
    }

    // Deja los contadores a cero, repinta y arranca el cronómetro
    private fun empezarPartida() {
        movimientos = 0
        segundos = 0
        juegoTerminado = false
        mostrarMovimientos()
        mostrarTiempo()
        pintar()

        // Paramos el cronómetro anterior (si lo había) y arrancamos uno nuevo
        handler.removeCallbacks(tareaCronometro)
        handler.postDelayed(tareaCronometro, 1000)
    }

    // Lógica de una jugada: se ejecuta al pulsar la casilla número "i"
    private fun pulsar(i: Int) {
        if (juegoTerminado) return  // si ya se ganó, los botones no hacen nada

        cambiarVecinas(i)   // cambia la casilla pulsada y las de alrededor
        movimientos++       // sumamos una pulsación
        mostrarMovimientos()
        pintar()            // actualizamos los colores en pantalla

        // Comprobamos si con esta jugada se ha ganado
        if (hayVictoria()) {
            juegoTerminado = true
            handler.removeCallbacks(tareaCronometro) // paramos el cronómetro
            sonidos?.startTone(ToneGenerator.TONE_PROP_ACK, 600)  // sonido de victoria (más largo)
            vibrador?.vibrate(300)                                 // vibración de victoria (más larga)
            val minutos = segundos / 60
            val seg = segundos % 60
            Toast.makeText(this, "Has Ganado. " + getString(R.string.win_msg, movimientos, minutos, seg), Toast.LENGTH_LONG).show()
        } else {
            sonidos?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)  // pitido corto al pulsar
            vibrador?.vibrate(30)                                  // vibración corta al pulsar
        }
    }

    // Cambia la casilla "i" y sus vecinas, sin repintar ni contar nada.
    // Se usa tanto al jugar como al generar el tablero aleatorio.
    private fun cambiarVecinas(i: Int) {
        // Pasamos de posición a fila y columna con división y resto.
        // Ejemplo con tamaño 4: la 6 -> fila 6/4 = 1, columna 6%4 = 2
        val fila = i / tamano
        val columna = i % tamano

        cambiar(i)                                      // la casilla pulsada
        // Las vecinas solo se cambian si existen (para no salirnos del tablero):
        if (fila > 0) cambiar(i - tamano)               // la de arriba (una fila menos)
        if (fila < tamano - 1) cambiar(i + tamano)      // la de abajo
        if (columna > 0) cambiar(i - 1)                 // la de la izquierda
        if (columna < tamano - 1) cambiar(i + 1)        // la de la derecha
    }

    // Invierte el estado de una casilla: de gris a morado y de morado a gris
    // (el operador ! convierte true en false y false en true)
    private fun cambiar(i: Int) {
        estado[i] = !estado[i]
    }

    // Recorre todas las casillas y pone a cada botón el color que le corresponde
    // según su estado: morado si está activada, gris si no.
    private fun pintar() {
        for (i in estado.indices) {
            if (estado[i]) {
                botones[i].setBackgroundColor(colorSecundario)
            } else {
                botones[i].setBackgroundColor(colorPrimario)
            }
        }
    }

    // Devuelve true si se ha ganado, es decir, si todas las casillas están en morado.
    // Si encuentra aunque sea una en gris, devuelve false enseguida.
    private fun hayVictoria(): Boolean {
        for (i in estado.indices) {
            if (!estado[i]) return false
        }
        return true
    }

    // Vuelve al tablero con el que empezó la partida y pone los contadores a cero
    private fun reiniciar() {
        estado = estadoInicial.copyOf()
        empezarPartida()
    }

    // Escribe el número de movimientos en pantalla (usa el texto de strings.xml)
    private fun mostrarMovimientos() {
        txtMovimientos.text = getString(R.string.moves_fmt, movimientos)
    }

    // Escribe el tiempo en pantalla con formato mm:ss
    private fun mostrarTiempo() {
        txtTiempo.text = getString(R.string.time_fmt, segundos / 60, segundos % 60)
    }

    // Se ejecuta al cerrar la pantalla: paramos el cronómetro y liberamos el sonido
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(tareaCronometro)
        sonidos?.release()
    }
}
