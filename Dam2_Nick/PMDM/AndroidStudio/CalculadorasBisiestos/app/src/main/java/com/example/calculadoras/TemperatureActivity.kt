package com.example.calculadoras

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TemperatureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        // Cogemos los elementos de la pantalla
        val editTemp = findViewById<EditText>(R.id.editTemp)
        val spinnerOrigen = findViewById<Spinner>(R.id.spinnerOrigen)
        val spinnerDestino = findViewById<Spinner>(R.id.spinnerDestino)
        val btnConvertir = findViewById<Button>(R.id.btnConvertir)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)

        // Rellenamos los dos desplegables con las unidades
        val unidades = arrayOf("Celsius", "Fahrenheit", "Kelvin")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, unidades)
        spinnerOrigen.adapter = adaptador
        spinnerDestino.adapter = adaptador

        btnConvertir.setOnClickListener {
            val texto = editTemp.text.toString()
            val temp = texto.toDoubleOrNull()

            if (temp == null) {
                txtResultado.text = "Escribe una temperatura"
            } else {
                val origen = spinnerOrigen.selectedItem.toString()
                val destino = spinnerDestino.selectedItem.toString()

                // Paso 1: pasamos la temperatura a Celsius
                var celsius = temp
                if (origen == "Fahrenheit") {
                    celsius = (temp - 32) * 5 / 9
                }
                if (origen == "Kelvin") {
                    celsius = temp - 273.15
                }

                // Paso 2: pasamos de Celsius a la unidad que queremos
                var resultado = celsius
                if (destino == "Fahrenheit") {
                    resultado = celsius * 9 / 5 + 32
                }
                if (destino == "Kelvin") {
                    resultado = celsius + 273.15
                }

                // Dejamos solo 2 decimales
                val resultadoRedondeado = "%.2f".format(resultado)
                txtResultado.text = "$temp $origen = $resultadoRedondeado $destino"
            }
        }
    }
}
