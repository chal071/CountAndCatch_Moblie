package com.example.countandcatch

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
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
    private lateinit var leftList: MutableList<Pair<Int, Int>>
    private lateinit var rightList: MutableList<Int>
    private var pairCount =5;

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

        val rvImage = findViewById<RecyclerView>(R.id.rvJuegoCountTop)
        val rvNumber = findViewById<RecyclerView>(R.id.rvJuegoCountBottom)

        rvImage.layoutManager = object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }

        rvNumber.layoutManager = object : LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollHorizontally() = false
        }

        var adapterImg = ImgAdapter()
        var adapterNumber = NumberAdapter()

        rvNumber.adapter = adapterNumber
        rvImage.adapter = adapterImg

        adapterImg.submit(imgList)
        adapterNumber.submit(numList)

        adapterImg.setItemSize(widthPx / pairCount - 4)
        adapterNumber.setItemSize(widthPx / pairCount - 4)
    }

    private fun generateShuffled(pairCount: Int): Pair<List<ImageItem>, List<Int>> {
        val selectedIds = PairData.pairMap.keys.shuffled().take(pairCount) // 同一批数字

        val images = selectedIds.map { id ->
            ImageItem(id, PairData.pairMap[id]!!.random())  // 这个数字随机挑一张图
        }.shuffled()  // 上排顺序打乱

        val numbers = selectedIds.shuffled() // 下排顺序打乱

        return images to numbers
    }

}
