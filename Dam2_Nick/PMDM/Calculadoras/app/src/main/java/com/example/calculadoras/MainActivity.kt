package com.example.calculadoras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBisiestos = findViewById<Button>(R.id.btnBisiestos)
        val btnTemperaturas = findViewById<Button>(R.id.btnTemperaturas)

        btnBisiestos.setOnClickListener {
            startActivity(Intent(this, LeapYearActivity::class.java))
        }

        btnTemperaturas.setOnClickListener {
            startActivity(Intent(this, TemperatureActivity::class.java))
        }
    }
}
