package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.countandcatch.data.Partida
import com.example.countandcatch.utils.JsonHelper

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
        val txtResultadoTitulo = findViewById<TextView>(R.id.txtResultadoTitulo)

        val layoutJuego1 = findViewById<View>(R.id.layoutJuego1)
        val txtResultadoErrores = findViewById<TextView>(R.id.txtResultadoErrores)
        val imgResultadoCruz = findViewById<ImageView>(R.id.imgResultadoCruz)


        val layoutJuego2 = findViewById<View>(R.id.layoutJuego2)
        val txtResultadoPuntos = findViewById<TextView>(R.id.txtResultadoPuntos)
        val imgResultadoCheck = findViewById<ImageView>(R.id.imgResultadoCheck)
        val txtTiempoPartida = findViewById<TextView>(R.id.txtTiempoPartida)


        val partida = intent.getSerializableExtra("partida") as? Partida

        if (partida == null) {
            txtResultadoNombreNino.text = "Error al cargar"
            txtResultadoTitulo.text = ""
            return
        }

        txtResultadoNombreNino.text = partida.nombre

        val dificultadTexto = when(partida.dificultad) {
            1 -> "Fácil"
            2 -> "Medio"
            3 -> "Difícil"
            else -> "Desconocido"
        }

        txtResultadoTitulo.text = "Dificultad: $dificultadTexto"

        // mostrar los resultados de juegos correspondiente
        if (partida.juego == 1) {
            layoutJuego1.visibility = View.VISIBLE
            layoutJuego2.visibility = View.GONE
            txtResultadoErrores.text = partida.errores.toString()
        } else if (partida.juego == 2) {
            layoutJuego1.visibility = View.GONE
            layoutJuego2.visibility = View.VISIBLE
            txtResultadoPuntos.text = partida.puntos.toString()
            txtTiempoPartida.text = partida.tiempo_partida.toString()


            txtTiempoPartida.text = formatMinutesToTime(partida.tiempo_partida)

        }

        guardarPartida(partida)

        val btnAceptar = findViewById<Button>(R.id.resultadobtnAceptar)
        btnAceptar.setOnClickListener {
            val intent = Intent(this, RankingActivity::class.java)
            intent.putExtra("partida", partida)
            startActivity(intent)
        }
    }


    private fun guardarPartida(partida: Partida) {
        val lista = JsonHelper.loadList<Partida>(this).toMutableList()
        lista.add(partida)
        JsonHelper.saveList(this, lista)
    }

    private fun formatMinutesToTime(seconds: Int): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}
