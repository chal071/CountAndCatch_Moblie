package com.example.countandcatch

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countandcatch.adapter.ImgAdapter
import com.example.countandcatch.data.ImageItem
import com.example.countandcatch.data.PairData
import com.example.countandcatch.data.Partida

class JuegoCountActivity : AppCompatActivity() {

    private var pairCount = 0;

    private var selectedImage: ImageItem? = null
    private var selectedNumber: ImageItem? = null

    private lateinit var adapterImg: ImgAdapter
    private lateinit var adapterNumber: ImgAdapter

    private val displayMetrics = Resources.getSystem().displayMetrics
    private val widthPx = displayMetrics.widthPixels

    private lateinit var timerText: TextView
    private var startTime = 0L
    private var elapsedSeconds = 0

    private val handler = Handler(Looper.getMainLooper())

    private var partida: Partida? = null

    private var errores = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_count)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainJuegoCount)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        partida = intent.getSerializableExtra("partida") as? Partida

        pairCount = when (partida?.dificultad) {
            1 -> 3
            2 -> 4
            3 -> 5
            else -> 5
        }

        volverPaginaDeInicio()

        val (imgList, numList) = generateShuffled(pairCount)
        inicializarListasDeJuego(numList, imgList)

        startTime = System.currentTimeMillis()
        handler.post(timerRunnable)

    }

    private val timerRunnable = object : Runnable {
        override fun run() {
            timerText = findViewById(R.id.timerTextJC)
            val now = System.currentTimeMillis()
            elapsedSeconds = ((now - startTime) / 1000).toInt()
            timerText.text = "${elapsedSeconds}s"
            handler.postDelayed(this, 1000)
        }
    }

    private fun volverPaginaDeInicio() {
        val buttonHome = findViewById<ImageButton>(R.id.btnHomeJC)

        buttonHome.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }
    }

    private fun obtenerFechaHoy(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(java.util.Date())
    }

    private fun generateShuffled(pairCount: Int): Pair<List<ImageItem>, List<ImageItem>> {
        val selectedIds = PairData.pairMap.keys.shuffled().take(pairCount)

        val imagesTop = selectedIds.map { id ->
            ImageItem(id, PairData.pairMap[id]!!.random())
        }.shuffled()

        val imagesBottom = selectedIds.map { id ->
            ImageItem(id, numeroDrawableFor(id))
        }.shuffled()

        return imagesTop to imagesBottom
    }

    private fun inicializarListasDeJuego(
        numList: List<ImageItem>,
        imgList: List<ImageItem>
    ) {
        val rvImage = findViewById<RecyclerView>(R.id.rvJuegoCountTop)
        val rvNumber = findViewById<RecyclerView>(R.id.rvJuegoCountBottom)

        adapterImg = ImgAdapter()
        adapterNumber = ImgAdapter()

        rvImage.layoutManager =
            object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
                override fun canScrollHorizontally() = false
            }
        rvNumber.layoutManager =
            object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
                override fun canScrollHorizontally() = false
            }

        rvImage.adapter = adapterImg
        rvNumber.adapter = adapterNumber

        adapterImg.submit(imgList)
        adapterNumber.submit(numList)

        val size = widthPx / pairCount - 4
        adapterImg.setItemSize(size)
        adapterNumber.setItemSize(size)

        adapterImg.setOnItemClickListener { image ->
            selectedImage = image
            adapterImg.setSelectedPair(image.pairId)
            checkMatch()
        }
        adapterNumber.setOnItemClickListener { image ->
            selectedNumber = image
            adapterNumber.setSelectedPair(image.pairId)
            checkMatch()
        }
    }


    private fun numeroDrawableFor(id: Int): Int = when (id) {
        1 -> R.drawable.num1
        2 -> R.drawable.num2
        3 -> R.drawable.num3
        4 -> R.drawable.num4
        5 -> R.drawable.num5
        else -> R.drawable.num1
    }


    private fun checkMatch() {
        val img = selectedImage
        val num = selectedNumber

        if (img != null && num != null) {
            if (img.pairId == num.pairId) {
                Toast.makeText(this, "✅ CORRECT", Toast.LENGTH_SHORT).show()
                adapterImg.removeByPairId(img.pairId)
                adapterNumber.removeByPairId(num.pairId)

                if (adapterImg.isEmpty() && adapterNumber.isEmpty()) {
                    handler.removeCallbacks(timerRunnable)
                    val base = partida
                    if (base != null) {
                        val updated = base.copy(
                            tiempo_partida = elapsedSeconds,
                            fecha = obtenerFechaHoy(),
                            errores = errores
                        )
                        val intent = Intent(this, ResultadoActivity::class.java)
                        intent.putExtra("partida", updated)
                        startActivity(intent)
                    }
                    Toast.makeText(this, "🎉 FINISH", Toast.LENGTH_SHORT).show()
                } else {
                    val newCount = adapterImg.getCount()
                    if (newCount > 0) {
                        val newSize = widthPx / newCount - 4
                        adapterImg.setItemSize(newSize)
                        adapterNumber.setItemSize(newSize)
                    }
                }
            } else {
                errores += 1
                Toast.makeText(this, "❌ INCORRECT", Toast.LENGTH_SHORT).show()
            }

            selectedImage = null
            selectedNumber = null
            adapterImg.setSelectedPair(null)
            adapterNumber.setSelectedPair(null)
        }

}

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
    }
}
