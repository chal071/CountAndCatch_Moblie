package com.example.countandcatch

import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countandcatch.adapter.ImgAdapter
import com.example.countandcatch.adapter.NumberAdapter
import com.example.countandcatch.data.ImageItem
import com.example.countandcatch.data.PairData

class JuegoCountActivity : AppCompatActivity() {

    private var pairCount = 4

    private var selectedImage: ImageItem? = null
    private var selectedNumber: Int? = null

    private lateinit var adapterImg: ImgAdapter
    private lateinit var adapterNumber: NumberAdapter

    private val displayMetrics = Resources.getSystem().displayMetrics
    private val widthPx = displayMetrics.widthPixels

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_count)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainJuegoCount)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val (imgList, numList) = generateShuffled(pairCount)
        inicializarListasDeJuego(numList, imgList)
    }

    private fun generateShuffled(pairCount: Int): Pair<List<ImageItem>, List<Int>> {
        val selectedIds = PairData.pairMap.keys.shuffled().take(pairCount)

        val images = selectedIds.map { id ->
            ImageItem(id, PairData.pairMap[id]!!.random())
        }.shuffled()

        val numbers = selectedIds.shuffled()

        return images to numbers
    }

    private fun inicializarListasDeJuego(numList: List<Int>, imgList: List<ImageItem>) {
        val rvImage = findViewById<RecyclerView>(R.id.rvJuegoCountTop)
        val rvNumber = findViewById<RecyclerView>(R.id.rvJuegoCountBottom)

        adapterImg = ImgAdapter()
        adapterNumber = NumberAdapter()

        rvImage.layoutManager = object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }
        rvNumber.layoutManager = object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
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
            checkMatch()
        }
        adapterNumber.setOnItemClickListener { number ->
            selectedNumber = number
            checkMatch()
        }
    }

    private fun checkMatch() {
        val img = selectedImage
        val num = selectedNumber

        if (img != null && num != null) {
            if (img.pairId == num) {
                Toast.makeText(this, "✅ CORRECT", Toast.LENGTH_SHORT).show()
                adapterImg.removeByPairId(img.pairId)
                adapterNumber.removeByValue(num)

                if (adapterImg.isEmpty() && adapterNumber.isEmpty()) {
                    Toast.makeText(this, "🎉 FINISH", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "❌ INCORRECT", Toast.LENGTH_SHORT).show()
            }

            selectedImage = null
            selectedNumber = null
        }
    }
}
