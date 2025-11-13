package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countandcatch.R

class MainActivity : AppCompatActivity() {
    private var nivelSeleccionado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elegir_nivel_juego)

        val btnFacil = findViewById<Button>(R.id.btnJcFacil)
        val btnIntermedio = findViewById<Button>(R.id.btnJcIntermedio)
        val btnDificil = findViewById<Button>(R.id.btnJcDificil)
        val btnContinuar = findViewById<Button>(R.id.btnJcContinuar)

        btnFacil.setOnClickListener {
            nivelSeleccionado = "Fácil"
        }

        btnIntermedio.setOnClickListener {
            nivelSeleccionado = "Intermedio"
        }

        btnDificil.setOnClickListener {
            nivelSeleccionado = "Difícil"
        }

        btnContinuar.setOnClickListener {
            // Si no eligió nivel, mostramos aviso y salimos
            if (nivelSeleccionado == null) {
                Toast.makeText(this, "Selecciona un nivel antes de continuar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

    }
}
