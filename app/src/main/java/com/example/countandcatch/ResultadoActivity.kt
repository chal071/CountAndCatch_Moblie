package com.example.countandcatch

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.countandcatch.data.Partida

class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainResultadoS)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtResultadoNombreNino = findViewById<TextView>(R.id.txtResultadoNombreNino)
        val txtResultadoErrores = findViewById<TextView>(R.id.txtResultadoErrores)

        val partida = intent.getSerializableExtra("partida") as? Partida

        if (partida == null) {
            txtResultadoNombreNino.text = "Error al cargar la partida"
            txtResultadoErrores.text = ""
            return
        }

        txtResultadoNombreNino.text = "Nombre: ${partida.nombre}"
        txtResultadoErrores.text = "Errores: ${partida.errores}"
    }
}