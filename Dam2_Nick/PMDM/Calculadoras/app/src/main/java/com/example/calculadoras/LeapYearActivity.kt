package com.example.calculadoras

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LeapYearActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leap_year)

        // Cogemos los elementos de la pantalla
        val editYear = findViewById<EditText>(R.id.editYear)
        val btnComprobar = findViewById<Button>(R.id.btnComprobar)
        val txtResultado = findViewById<TextView>(R.id.txtResultado)

        btnComprobar.setOnClickListener {
            val texto = editYear.text.toString()

            if (texto.isEmpty()) {
                txtResultado.text = "Escribe un año"
            } else {
                val year = texto.toInt()

                // Es bisiesto si se divide entre 4 y no entre 100, o si se divide entre 400
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    txtResultado.text = "$year es bisiesto"
                } else {
                    txtResultado.text = "$year no es bisiesto"
                }
            }
        }
    }
}
