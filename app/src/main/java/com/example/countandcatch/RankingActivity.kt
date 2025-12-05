package com.example.countandcatch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countandcatch.adapter.RankingAdapter
import com.example.countandcatch.data.Partida
import com.example.countandcatch.utils.JsonHelper

class RankingActivity : AppCompatActivity() {

    private lateinit var tvJuego: TextView
    private lateinit var star1: ImageView
    private lateinit var star2: ImageView
    private lateinit var star3: ImageView
    private lateinit var rvRanking: RecyclerView
    private lateinit var btnHome: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ranking)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRanking)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvJuego = findViewById(R.id.tvJuego)
        star1 = findViewById(R.id.star1)
        star2 = findViewById(R.id.star2)
        star3 = findViewById(R.id.star3)
        rvRanking = findViewById(R.id.rvRanking)
        btnHome = findViewById(R.id.btnHomeRanking)

        val partida = intent.getSerializableExtra("partida") as? Partida ?: return

        val dificultad = partida.dificultad
        val juego = partida.juego

        tvJuego.text = when (juego) {
            1 -> "Juego: Count"
            2 -> "Juego: Catch"
            else -> "Juego: Desconocido"
        }

        rankPartidas(dificultad, juego)
        setStars(dificultad)
        manageHomebtn(partida)
    }

    private fun rankPartidas(dificultad: Int, juego: Int){
        val todasPartidas: List<Partida> = JsonHelper.loadList(this)

        val partidasFiltradas = todasPartidas.filter {
            it.juego == juego &&
                    it.dificultad == dificultad &&
                    it.terminada == 1
        }

        val rankingOrdenado = when (juego) {
            1 -> partidasFiltradas.sortedWith(
                compareBy<Partida> { it.errores }
                    .thenBy { it.tiempo_partida })

            2 -> partidasFiltradas.sortedWith(
                compareByDescending<Partida> { it.puntos }
                    .thenBy { it.tiempo_partida })

            else -> partidasFiltradas
        }

        rvRanking.layoutManager = LinearLayoutManager(this)
        rvRanking.adapter = RankingAdapter(rankingOrdenado, juego)
    }

    private fun manageHomebtn(partida: Partida){
        btnHome.setOnClickListener {
            val newPartida = Partida(
                nombre = partida.nombre,
                edad = partida.edad,
                tiempo_partida = 0,
                errores = 0,
                puntos = 0,
                fecha_hora = "",
                juego = 0,
                dificultad = 0,
                terminada = 0
            )

            val intent = Intent(this, ElegirJuegosActivity::class.java)
            intent.putExtra("partida", newPartida)
            startActivity(intent)
            finish()
        }
    }
    private fun setStars(level: Int) {
        val stars = listOf(star1, star2, star3)
        stars.forEachIndexed { index, imageView ->
            imageView.visibility = if (index < level) View.VISIBLE else View.INVISIBLE
        }
    }
}