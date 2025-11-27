package com.example.jocprojecteguillem

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animate
import com.example.countandcatch.R

class MainActivity : AppCompatActivity() {
    private var nivelSeleccionado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elegir_nivel_juego)

        val btnFacil = findViewById<Button>(R.id.btnJcFacil)
        val btnIntermedio = findViewById<Button>(R.id.btnJcIntermedio)
        val btnDificil = findViewById<Button>(R.id.btnJcDificil)


        val buttons = listOf(btnFacil, btnIntermedio, btnDificil)

        btnFacil.setOnClickListener {
            resetZoom(buttons)
            zoomSelected(it)
            nivelSeleccionado = "Fácil"
        }

        btnIntermedio.setOnClickListener {
            resetZoom(buttons)
            zoomSelected(it)
            nivelSeleccionado = "Intermedio"
        }

        btnDificil.setOnClickListener {
            resetZoom(buttons)
            zoomSelected(it)
            nivelSeleccionado = "Difícil"
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
