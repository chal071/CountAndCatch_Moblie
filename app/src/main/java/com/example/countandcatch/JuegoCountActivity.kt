package com.example.countandcatch

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
    private var pairCount =4;
    private var selectedImage: ImageItem? = null
    private var selectedNumber: Int? = null
    val displayMetrics = Resources.getSystem().displayMetrics
    val widthPx = displayMetrics.widthPixels

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
        inicializarListasDeJuego(numList,imgList)
    }

    private fun generateShuffled(pairCount: Int): Pair<List<ImageItem>, List<Int>> {
        val selectedIds = PairData.pairMap.keys.shuffled().take(pairCount)

        val images = selectedIds.map { id ->
            ImageItem(id, PairData.pairMap[id]!!.random())
        }.shuffled()

        val numbers = selectedIds.shuffled()

        return images to numbers
    }


    private fun  inicializarListasDeJuego(numList: List<Int>, imgList: List<ImageItem>){
        val rvImage = findViewById<RecyclerView>(R.id.rvJuegoCountTop)
        val rvNumber = findViewById<RecyclerView>(R.id.rvJuegoCountBottom)

        val adapterImg = ImgAdapter()
        val adapterNumber = NumberAdapter()

        rvImage.layoutManager = object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }
        rvNumber.layoutManager = object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }

        rvNumber.adapter = adapterNumber
        rvImage.adapter = adapterImg

        adapterImg.submit(imgList)
        adapterNumber.submit(numList)

        adapterImg.setItemSize(widthPx / pairCount - 4)
        adapterNumber.setItemSize(widthPx / pairCount - 4)

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
                Toast.makeText(this, "✅", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "❌", Toast.LENGTH_SHORT).show()
            }
            selectedImage = null
            selectedNumber = null
        }
    }

}
