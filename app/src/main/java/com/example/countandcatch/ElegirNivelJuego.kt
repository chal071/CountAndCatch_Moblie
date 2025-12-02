package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countandcatch.data.Partida
import androidx.compose.animation.core.animate
import com.example.countandcatch.R

class ElegirNivelJuego : AppCompatActivity() {
    private var nivelSeleccionado: Int? = null
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
            nivelSeleccionado = 1
            resetZoom(buttons)
            zoomSelected(it)
        }

        btnIntermedio.setOnClickListener {
            nivelSeleccionado = 2
            resetZoom(buttons)
            zoomSelected(it)
        }

        btnDificil.setOnClickListener {
            nivelSeleccionado = 3
            resetZoom(buttons)
            zoomSelected(it)
        }

        btnContinuar.setOnClickListener {

            val base = partida

            if (nivelSeleccionado == null) {
                Toast.makeText(this, "Selecciona un nivel antes de continuar", Toast.LENGTH_SHORT).show()
            } else if (base == null) {
                Toast.makeText(this, "Error al cargar la partida", Toast.LENGTH_SHORT).show()
            } else {
                val updated = base.copy(
                    dificultad = nivelSeleccionado!!
                )

                when (partida?.juego) {
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
        }
    }
    private fun addZoomEffect(button: View) {
        button.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .setDuration(150)
            .withEndAction {
                button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
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
