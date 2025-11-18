package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.countandcatch.data.Partida

class ElegirJuegosActivity : AppCompatActivity() {
    private lateinit var btnEmpezar: Button
    private lateinit var frameJuegoCount: FrameLayout
    private lateinit var frameJuegoCatch: FrameLayout
    private var selectedJuego: String? = null  // A = Count, B = Catch

    private var partida: Partida? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_elegir_juegos)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainElegirJuegos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnHome = findViewById<ImageButton>(R.id.btnHomeEJ)

        btnHome.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        frameJuegoCatch = findViewById(R.id.frameJuegoCatchEJ)
        frameJuegoCount = findViewById(R.id.frameJuegoCountEJ)
        btnEmpezar = findViewById(R.id.btnEmpezarEJ)

        partida = intent.getSerializableExtra("partida") as? Partida

        frameJuegoCount.setOnClickListener {
            selectedJuego = "A"
            highLightSelection(frameJuegoCount, frameJuegoCatch)
        }

        frameJuegoCatch.setOnClickListener {
            selectedJuego = "B"
            highLightSelection(frameJuegoCatch, frameJuegoCount)
        }

        btnEmpezar.setOnClickListener {
            val basePartida = partida
            if (basePartida == null) {
                Toast.makeText(this, "Error al cargar la partida.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (selectedJuego) {
                "A" -> {
                    val updated = basePartida.copy(juego = 1)
                    val intent = Intent(this, ElegirNivelJuego::class.java)
                    intent.putExtra("partida", updated)
                    startActivity(intent)
                }
                "B" -> {
                    val updated = basePartida.copy(juego = 2)
                    val intent = Intent(this, ElegirNivelJuego::class.java)
                    intent.putExtra("partida", updated)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Elegir un juego, por favor.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun highLightSelection(selected: FrameLayout, other: FrameLayout) {
        other.animate()
            .scaleX(1f)
            .scaleY(1f)
            .translationZ(0f)
            .setDuration(150)
            .start()

        selected.animate()
            .scaleX(1.08f)
            .scaleY(1.08f)
            .translationZ(10f)
            .setDuration(200)
            .start()
    }
}
