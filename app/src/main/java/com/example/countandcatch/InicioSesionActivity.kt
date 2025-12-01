package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageButton
import android.widget.Toast
import com.example.countandcatch.data.Partida

class InicioSesionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sesion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainIS)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnHome = findViewById<ImageButton>(R.id.btnHomeIS)
        val btnContinue = findViewById<Button>(R.id.btnContinueIS)

        btnHome.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnContinue.setOnClickListener{
            guardarNombreDeUsuario()
        }


    }

    private fun guardarNombreDeUsuario() {
        val etName = findViewById<EditText>(R.id.etNameIS)
        val nombre = etName.text.toString().trim()

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Por favor, introduzca su nombre.", Toast.LENGTH_SHORT).show()
        }else{
        val partida = Partida(
            nombre = nombre,
            tiempo_partida = 0,
            errores = 0,
            fecha_hora = "",
            juego = 0,
            dificultad = 0
        )
        val intent = Intent(this, ElegirJuegosActivity::class.java)
        intent.putExtra("partida", partida)
        startActivity(intent)
        }
    }
}
