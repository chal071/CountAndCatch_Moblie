package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
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
        val imgResultadoCruz = findViewById<ImageView>(R.id.imgResultadoCruz)
        val txtResultadoErrores = findViewById<TextView>(R.id.txtResultadoErrores)
        val imgResultadoCheck = findViewById<ImageView>(R.id.imgResultadoCheck)
        val txtResultadoPuntos = findViewById<TextView>(R.id.txtResultadoPuntos)


        val partida = intent.getSerializableExtra("partida") as? Partida

        if (partida == null) {
            txtResultadoNombreNino.text = "Error al cargar nombre"
            txtResultadoErrores.text = ""
            return
        }
        if (partida.juego == 1) {
            imgResultadoCheck.visibility = android.view.View.GONE
            txtResultadoPuntos.visibility = android.view.View.GONE

            txtResultadoErrores.text = "${partida.errores}"
        } else if (partida.juego == 2){
            imgResultadoCruz.visibility = android.view.View.GONE
            txtResultadoErrores.visibility = android.view.View.GONE

            txtResultadoPuntos.text = "${partida.puntos}"
        }

        txtResultadoNombreNino.text = "${partida.nombre}"

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

}