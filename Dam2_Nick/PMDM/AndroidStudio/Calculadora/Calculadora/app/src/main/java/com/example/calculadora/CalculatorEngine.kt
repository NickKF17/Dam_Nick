package com.example.calculadora
import android.telephony.PhoneNumberUtils.formatNumber
import java.math.BigDecimal
import java.math.MathContext


class CalculatorEngine {

    // Número que se está escribiendo ahora mismo (lo guardamos como texto)
    private var currentInput: String = "0"

    // Primer número de la operación (el que había antes de pulsar +, -, ×, ÷)
    private var storedValue: Double = 0.0

    // Operador pendiente: "+", "-", "×", "÷" (o null si todavía no hay ninguno)
    private var pendingOperator: String? = null

    // Texto de la pantalla pequeña, por ejemplo "12.5 + 4"
    private var expression: String = ""

    // true justo después de pulsar un operador: el siguiente dígito empieza un número nuevo
    private var waitingForNewNumber: Boolean = false

    // true justo después de pulsar "=": si escribes un dígito, se empieza de cero
    private var justCalculated: Boolean = false

    // true si hubo un error (por ejemplo dividir entre 0)
    private var hasError: Boolean = false


    // Funciones para leer estado (las usa MainActivity para pintar pantallas)


    // Texto para la pantalla grande
    fun getMainDisplay(): String = currentInput

    // Texto para la pantalla pequeña
    fun getExpressionDisplay(): String = expression

    // Acciones del usuario


    // El usuario pulsa un dígito del 0 al 9
    fun appendDigit(digit: String) {
        // Si venimos de un error o de un resultado, empezamos de cero
        if (hasError || justCalculated) {
            clear()
        }

        if (waitingForNewNumber) {
            // Acabamos de pulsar un operador: el dígito empieza un número nuevo
            currentInput = digit
            waitingForNewNumber = false
        } else if (currentInput == "0") {
            // Evita números como "007": el 0 inicial se reemplaza
            currentInput = digit
        } else {
            // Caso normal: añadimos el dígito al final
            currentInput += digit
        }
    }

    // El usuario pulsa el punto decimal
    fun appendDecimal() {
        if (hasError || justCalculated) {
            clear()
        }

        if (waitingForNewNumber) {
            // Empezamos un número nuevo con "0."
            currentInput = "0."
            waitingForNewNumber = false
            return
        }

        // Control de errores: si ya hay un punto, no dejamos poner otro (evita 12.3.4)
        if (currentInput.contains(".")) {
            return
        }

        currentInput += "."
    }

    // El usuario pulsa un operador: "+", "-", "×" o "÷"
    fun setOperator(operator: String) {
        if (hasError) return

        // Si veníamos de un resultado, ese resultado será el primer número
        justCalculated = false

        // Si el usuario pulsa dos operadores seguidos (ej: 5 + ×), solo cambiamos el operador
        if (waitingForNewNumber && pendingOperator != null) {
            pendingOperator = operator
            expression = formatNumber(storedValue) + " " + operator
            return
        }

        if (pendingOperator != null) {
            // Había una operación pendiente (ej: 5 + 3 y ahora pulsa ×): la resolvemos primero
            val result = operate(storedValue, currentInput.toDouble(), pendingOperator!!)
            if (result == null) {
                showError("Error: Div por 0")
                return
            }
            storedValue = result
        } else {
            // Primera operación: guardamos el número actual
            storedValue = currentInput.toDouble()
        }

        pendingOperator = operator
        expression = formatNumber(storedValue) + " " + operator
        currentInput = formatNumber(storedValue)
        waitingForNewNumber = true
    }

    // El usuario pulsa "="
    fun calculate() {
        // Si hay error o no hay operación pendiente, no hacemos nada
        if (hasError || pendingOperator == null) return

        // Si pulsa "=" justo después de un operador (ej: "5 +" y "="), usamos el mismo número
        val secondNumber = currentInput.toDouble()
        val operator = pendingOperator!!

        val result = operate(storedValue, secondNumber, operator)

        if (result == null) {
            // Control de errores: división entre cero
            showError("Error: Div por 0")
            return
        }

        // Mostramos la operación completa en la pantalla pequeña: "12.5 + 4 ="
        expression = formatNumber(storedValue) + " " + operator + " " +
                formatNumber(secondNumber) + " ="
        currentInput = formatNumber(result)

        // Dejamos la calculadora lista para una operación nueva
        pendingOperator = null
        storedValue = result
        waitingForNewNumber = false
        justCalculated = true
    }

    // Botón C: limpia todo y reinicia la calculadora
    fun clear() {
        currentInput = "0"
        storedValue = 0.0
        pendingOperator = null
        expression = ""
        waitingForNewNumber = false
        justCalculated = false
        hasError = false
    }

    // Botón DEL: borra el último carácter escrito
    fun deleteLast() {
        // Si hay error, DEL funciona como limpiar
        if (hasError) {
            clear()
            return
        }
        // No borramos resultados ni números que aún no hemos empezado a escribir
        if (waitingForNewNumber || justCalculated) return

        // Quitamos el último carácter
        currentInput = currentInput.dropLast(1)

        // Si nos quedamos sin nada (o solo con "-"), volvemos a "0"
        if (currentInput.isEmpty() || currentInput == "-") {
            currentInput = "0"
        }
    }

    // Botón ±: cambia el signo del número actual
    fun toggleSign() {
        if (hasError || currentInput == "0") return

        currentInput = if (currentInput.startsWith("-")) {
            currentInput.removePrefix("-")   // era negativo -> lo hacemos positivo
        } else {
            "-$currentInput"                  // era positivo -> le ponemos el "-"
        }

        // Si cambiamos el signo de un resultado, lo tratamos como nuevo primer número
        if (justCalculated) {
            storedValue = currentInput.toDouble()
        }
    }


    // FUNCIONES PRIVADAS (ayudantes internos)

    private fun operate(a: Double, b: Double, operator: String): Double? {
        val result = when (operator) {
            "+" -> a + b
            "-" -> a - b
            "×" -> a * b
            "÷" -> {
                if (b == 0.0) return null   // división entre cero: avisamos con null
                a / b
            }
            else -> return null
        }
        // Si el número se hizo infinito (demasiado grande), también es error
        if (result.isInfinite() || result.isNaN()) return null
        return result
    }

    // Pone la calculadora en modo error y muestra el mensaje en la pantalla grande
    private fun showError(message: String) {
        currentInput = message
        expression = ""
        pendingOperator = null
        waitingForNewNumber = false
        justCalculated = false
        hasError = true
    }

    /**
     * Convierte un Double en texto bonito:
     *  - 5.0  -> "5"        (sin ".0" innecesario)
     *  - 0.1 + 0.2 -> "0.3" (redondea errores raros de los decimales)
     */
    private fun formatNumber(value: Double): String {
        return BigDecimal(value)
            .round(MathContext(12))     // máximo 12 cifras significativas
            .stripTrailingZeros()       // quita ceros sobrantes
            .toPlainString()            // evita notación científica (1E+3)
    }
}
