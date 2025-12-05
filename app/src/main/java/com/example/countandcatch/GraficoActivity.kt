package com.example.countandcatch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chaquo.python.Python
import java.io.File
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class GraficoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grafico)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainGrafico)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        fun loadMetricsFromJson() {
            // 1. 读你设备里的 JSON 文件
            val file = File(filesDir, "sessions.json")   // 路径按你自己的来
            val jsonString = file.readText()

            // 2. 调用 Python
            val py = Python.getInstance()
            val module = py.getModule("metrics")   // metrics.py

            val result = module.callAttr("run_analysis", jsonString)

            // 3. 取 metrics 里的数字
            val metricsPy = result.get("metrics")

            val playerCount = metricsPy.get("player_count").toInt()
            val avgSessionLength = metricsPy.get("average_session_length").toDouble()
            val retentionRate = metricsPy.get("retention_rate").toDouble()
            val churnRate = metricsPy.get("churn_rate").toDouble()

            // 4. 取三张图的 base64
            val histBase64 = result.get("hist_session_length").toString()
            val dauBase64 = result.get("dau").toString()
            val completionBase64 = result.get("completion_by_game").toString()

            // 5. 丢到 UI（自己换成你真正的控件）
            textViewPlayers.text = playerCount.toString()
            textViewAvgSession.text = "%.1f s".format(avgSessionLength)
            textViewRetention.text = "%.1f %%".format(retentionRate * 100)
            textViewChurn.text = "%.1f %%".format(churnRate * 100)

            imageViewHist.setImageBitmap(base64ToBitmap(histBase64))
            imageViewDau.setImageBitmap(base64ToBitmap(dauBase64))
            imageViewCompletion.setImageBitmap(base64ToBitmap(completionBase64))
        }

    }
}

