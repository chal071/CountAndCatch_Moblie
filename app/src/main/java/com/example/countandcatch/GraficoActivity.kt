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
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.chaquo.python.PyObject
import com.example.countandcatch.utils.JsonHelper

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
        loadMetricsFromJson()

    }
    private fun loadMetricsFromJson() {
        val textViewPlayers = findViewById<TextView>(R.id.textViewPlayers)
        val textViewAvgSession = findViewById<TextView>(R.id.textViewAvgSession)
        val textViewRetention = findViewById<TextView>(R.id.textViewRetention)
        val textViewChurn = findViewById<TextView>(R.id.textViewChurn)

        val imageViewHist = findViewById<ImageView>(R.id.imageViewHist)
        val imageViewDau = findViewById<ImageView>(R.id.imageViewDau)
        val imageViewCompletion = findViewById<ImageView>(R.id.imageViewCompletion)

        val file = File(filesDir, JsonHelper.FILE_NAME)

        if (!file.exists()) {
            JsonHelper.loadList<Any>(this)
        }

        if (!file.exists()) {
            Log.e("Grafico", "JSON file not found: ${file.absolutePath}")
            return
        }

        val jsonString = file.readText()
        Log.d("Grafico", "JSON length = ${jsonString.length}")

        val py = Python.getInstance()
        val module = py.getModule("metrics")
        val resultPy = module.callAttr("run_analysis", jsonString)

        val resultMap: Map<PyObject, PyObject> = resultPy.asMap()

        val keyMetrics = PyObject.fromJava("metrics")
        val keyHist = PyObject.fromJava("hist_session_length")
        val keyDau = PyObject.fromJava("dau")
        val keyCompletion = PyObject.fromJava("completion_by_game")

        val metricsPy = resultMap[keyMetrics] ?: run {
            Log.e("Grafico", "metrics key missing in Python result")
            return
        }

        val metricsMap: Map<PyObject, PyObject> = metricsPy.asMap()

        val kPlayerCount = PyObject.fromJava("player_count")
        val kAvgSession = PyObject.fromJava("average_session_length")
        val kRetention = PyObject.fromJava("retention_rate")
        val kChurn = PyObject.fromJava("churn_rate")

        val playerCount = metricsMap[kPlayerCount]?.toInt() ?: 0
        val avgSessionLength = metricsMap[kAvgSession]?.toDouble() ?: 0.0
        val retentionRate = metricsMap[kRetention]?.toDouble() ?: 0.0
        val churnRate = metricsMap[kChurn]?.toDouble() ?: 0.0

        textViewPlayers.text = playerCount.toString()
        textViewAvgSession.text = "%.1f s".format(avgSessionLength)
        textViewRetention.text = "%.1f %%".format(retentionRate * 100)
        textViewChurn.text = "%.1f %%".format(churnRate * 100)

        val histBase64 = resultMap[keyHist]?.toString() ?: ""
        val dauBase64 = resultMap[keyDau]?.toString() ?: ""
        val completionBase64 = resultMap[keyCompletion]?.toString() ?: ""

        base64ToBitmap(histBase64)?.let { imageViewHist.setImageBitmap(it) }
        base64ToBitmap(dauBase64)?.let { imageViewDau.setImageBitmap(it) }
        base64ToBitmap(completionBase64)?.let { imageViewCompletion.setImageBitmap(it) }
    }



    private fun base64ToBitmap(base64Str: String): Bitmap? {
        if (base64Str.isEmpty()) return null
        val bytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}

