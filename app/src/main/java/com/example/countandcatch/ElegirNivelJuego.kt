package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countandcatch.data.Partida

class ElegirNivelJuego : AppCompatActivity() {
    private var partida: Partida? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elegir_nivel_juego)

        partida = intent.getSerializableExtra("partida") as? Partida

        val btnFacil = findViewById<Button>(R.id.btnJcFacil)
        val btnIntermedio = findViewById<Button>(R.id.btnJcIntermedio)
        val btnDificil = findViewById<Button>(R.id.btnJcDificil)

        val buttons = listOf(btnFacil, btnIntermedio, btnDificil)

        btnFacil.setOnClickListener {
            resetZoom(buttons)
            zoomSelected(it)
            irAlJuego(1)
        }

        btnIntermedio.setOnClickListener {
            resetZoom(buttons)
            zoomSelected(it)
            irAlJuego(2)
        }

        btnDificil.setOnClickListener {
            resetZoom(buttons)
            zoomSelected(it)
            irAlJuego(3)
        }
    }

    private fun irAlJuego(nivel: Int) {
        val base = partida
        if (base == null) {
            Toast.makeText(this, "Error al cargar la partida", Toast.LENGTH_SHORT).show()
            return
        }

        val updated = base.copy(
            dificultad = nivel
        )

        when (updated.juego) {
            1 -> {
                val intent = Intent(this, JuegoCountActivity::class.java)
                intent.putExtra("partida", updated)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, JuegoCatchActivity::class.java)
                intent.putExtra("partida", updated)
                startActivity(intent)
            }
            else -> {
                Toast.makeText(this, "Error al cargar la partida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun zoomSelected(button: View) {
        button.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(150)
    }

    private fun resetZoom(buttons: List<View>) {
        buttons.forEach {
            it.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(150)
        }
    }
}

