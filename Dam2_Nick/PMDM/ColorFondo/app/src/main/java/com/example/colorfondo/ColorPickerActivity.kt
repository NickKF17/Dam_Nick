package com.example.colorfondo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ColorPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        // Cogemos los tres botones
        val btnRojo = findViewById<Button>(R.id.btnRojo)
        val btnVerde = findViewById<Button>(R.id.btnVerde)
        val btnAzul = findViewById<Button>(R.id.btnAzul)

        btnRojo.setOnClickListener {
            devolverColor("#F44336", "Rojo")
        }

        btnVerde.setOnClickListener {
            devolverColor("#4CAF50", "Verde")
        }

        btnAzul.setOnClickListener {
            devolverColor("#2196F3", "Azul")
        }
    }

    // Guarda el color en un Intent, lo devuelve a la pantalla principal y cierra esta pantalla
    private fun devolverColor(colorHex: String, colorNombre: String) {
        val intent = Intent()
        intent.putExtra("COLOR_HEX", colorHex)
        intent.putExtra("COLOR_NAME", colorNombre)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
