package com.example.colorfondo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Cogemos los elementos de la pantalla
        val layoutPrincipal = findViewById<LinearLayout>(R.id.layoutPrincipal)
        val txtColor = findViewById<TextView>(R.id.txtColor)
        val btnElegir = findViewById<Button>(R.id.btnElegir)

        // Aquí recibimos la respuesta de la segunda pantalla
        val lanzador = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            // Solo hacemos algo si el resultado es OK (si vuelve atrás no pasa nada)
            if (resultado.resultCode == Activity.RESULT_OK) {
                val datos = resultado.data
                val colorHex = datos?.getStringExtra("COLOR_HEX")
                val colorNombre = datos?.getStringExtra("COLOR_NAME")

                if (colorHex != null && colorNombre != null) {
                    layoutPrincipal.setBackgroundColor(Color.parseColor(colorHex))
                    txtColor.text = "Color actual: $colorNombre"
                }
            }
        }

        // Al pulsar el botón abrimos la segunda pantalla
        btnElegir.setOnClickListener {
            val intent = Intent(this, ColorPickerActivity::class.java)
            lanzador.launch(intent)
        }
    }
}
