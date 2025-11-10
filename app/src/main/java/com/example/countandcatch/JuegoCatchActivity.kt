package com.example.countandcatch

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class JuegoCatchActivity : AppCompatActivity() {

    private lateinit var countdownTimer: TextView
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_catch)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainJuegoCatch)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        countdownTimer = findViewById(R.id.catchTxtTimer)
        startCountdown()
    }

    private fun startCountdown() {
        timer?.cancel() // Cancel any existing timer

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisLeft: Long) {

                val seconds = (millisLeft / 1000).toInt()
                val minutes = seconds / 60
                val secs = seconds % 60
                countdownTimer.text = String.format("%d:%02d", minutes, secs)
            }

            override fun onFinish() {
                countdownTimer.text = "0:00"
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}