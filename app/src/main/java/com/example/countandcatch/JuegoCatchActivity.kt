package com.example.countandcatch

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class JuegoCatchActivity : AppCompatActivity() {

    // private lateinit var countdownTimer: TextView
    private var timer: CountDownTimer? = null

    private var dX =
        0f //hacer que cuando se toque la pantalla, la cesta no salte a donde se ha puesto el dedo

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_catch)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainJuegoCatch)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val countdownTimer = findViewById<TextView>(R.id.catchTxtTimer)
        startCountdown(countdownTimer)

        val background = findViewById<ImageView>(R.id.imgCatchFondo)
        val basketSlide = findViewById<ImageView>(R.id.imgCatchCesta)
        val appleFall = findViewById<ImageView>(R.id.imgCatchManzana)

        basketSlide.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                }

                MotionEvent.ACTION_MOVE -> {
                    // Update only the X position (horizontal movement)
                    val newX = event.rawX + dX
                    val minX = background.x
                    val maxX = background.x + background.width - view.width
                    view.x = newX.coerceIn(minX, maxX)
                }
            }
            true
        }

        dropItem(background, basketSlide, appleFall)
    }

    private fun startCountdown(countdownTimer: TextView) {
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

    private fun dropItem(background: ImageView, basket: ImageView, item: ImageView) {
        val randomX = background.x + Random.nextFloat() * (background.width - item.width)
        item.x = randomX
        item.y = background.y

        val animator = ObjectAnimator.ofFloat(item, "y", background.y, background.y + background.height)
        animator.duration = 3000
        animator.interpolator = LinearInterpolator()
        animator.start()

        item.postDelayed(object : Runnable {
            override fun run() {
                if (item.y + item.height >= basket.y &&
                    item.x + item.width >= basket.x &&
                    item.x <= basket.x + basket.width) {
                    // Hit basket
                    item.visibility = android.view.View.GONE
                } else if (item.y >= background.y + background.height) {
                    // Hit bottom
                    item.visibility = android.view.View.GONE
                } else {
                    item.postDelayed(this, 16)
                }
            }
        }, 16)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}