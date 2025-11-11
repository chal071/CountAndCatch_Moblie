package com.example.countandcatch

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat

import androidx.core.view.WindowInsetsCompat


class ElegirJuegosActivity : AppCompatActivity() {
    private lateinit var btnEmpezar: Button;
    private lateinit var frameJuegoCount: FrameLayout;
    private lateinit var frameJuegoCatch: FrameLayout;
    private var selectedJuego: String? = null //guardar el juego seleccionado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_elegir_juegos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainElegirJuegos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        frameJuegoCatch = findViewById(R.id.frameJuegoCatch)
        frameJuegoCount = findViewById(R.id.frameJuegoCount)
        btnEmpezar = findViewById(R.id.btnEmpezar)

        frameJuegoCount.setOnClickListener {
            selectedJuego = "A"
            highLightSelection(frameJuegoCount, frameJuegoCatch)
        }

        frameJuegoCount.setOnClickListener {
            selectedJuego = "B"
            highLightSelection(frameJuegoCatch, frameJuegoCount)
        }

        btnEmpezar.setOnClickListener {
            when (selectedJuego) {

                else -> Toast.makeText(this, "Elegir un juego, por favor.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun highLightSelection(selected: FrameLayout, other: FrameLayout) {

    }
}

