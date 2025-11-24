package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.countandcatch.data.Partida

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
        val btnContinuar = findViewById<Button>(R.id.btnJcContinuar)

        btnFacil.setOnClickListener {
            nivelSeleccionado = 1
        }

        btnIntermedio.setOnClickListener {
            nivelSeleccionado = 2
        }

        btnDificil.setOnClickListener {
            nivelSeleccionado = 3
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
}
