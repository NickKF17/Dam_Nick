package com.example.calculadora

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Las dos pantallas. "lateinit" = las inicializaremos más tarde, en onCreate
    private lateinit var tvResult: TextView       // pantalla grande
    private lateinit var tvExpression: TextView   // pantalla pequeña

    // Creamos el cerebro de la calculadora (se crea una sola vez)
    private val engine = CalculatorEngine()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Le decimos qué diseño XML debe mostrar esta pantalla
        setContentView(R.layout.activity_main)

        // Buscamos las pantallas en el XML usando su id
        tvResult = findViewById(R.id.tvResult)
        tvExpression = findViewById(R.id.tvExpression)

        // Configuramos todos los botones
        setupDigitButtons()
        setupOperatorButtons()
        setupSpecialButtons()

        // Pintamos el estado inicial ("0" en la pantalla)
        updateDisplay()
    }

    // Configura los botones del 0 al 9 y el punto.

    private fun setupDigitButtons() {
        // Lista con los ids de los 10 botones numéricos
        val digitButtonIds = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        // Recorremos la lista; para cada id buscamos su botón y le ponemos un "oyente de clic"
        for (buttonId in digitButtonIds) {
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener {
                // button.text es el texto del botón ("7", "3"...). Se lo pasamos al cerebro.
                engine.appendDigit(button.text.toString())
                updateDisplay()
            }
        }

        // Botón del punto decimal
        findViewById<Button>(R.id.btnDot).setOnClickListener {
            engine.appendDecimal()
            updateDisplay()
        }
    }

    // Configura los botones + - × ÷ y el igual.

    private fun setupOperatorButtons() {
        // Lista con los ids de los 4 operadores
        val operatorButtonIds = listOf(
            R.id.btnAdd, R.id.btnSub, R.id.btnMul, R.id.btnDiv
        )

        for (buttonId in operatorButtonIds) {
            val button = findViewById<Button>(buttonId)
            button.setOnClickListener {
                // El texto del botón ("+", "-", "×", "÷") es el operador
                engine.setOperator(button.text.toString())
                updateDisplay()
            }
        }

        // Botón igual: calcula el resultado
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            engine.calculate()
            updateDisplay()
        }
    }

    // Configura los botones especiales: C, DEL y ±.
    private fun setupSpecialButtons() {
        // C: limpia todo
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            engine.clear()
            updateDisplay()
        }

        // DEL: borra el último carácter
        findViewById<Button>(R.id.btnDel).setOnClickListener {
            engine.deleteLast()
            updateDisplay()
        }

        // ±: cambia el signo
        findViewById<Button>(R.id.btnSign).setOnClickListener {
            engine.toggleSign()
            updateDisplay()
        }
    }

    /**
     * Copia lo que dice el cerebro a las dos pantallas.
     * La llamamos después de CADA clic para que la pantalla siempre esté al día.
     */
    private fun updateDisplay() {
        tvResult.text = engine.getMainDisplay()
        tvExpression.text = engine.getExpressionDisplay()
    }
}
