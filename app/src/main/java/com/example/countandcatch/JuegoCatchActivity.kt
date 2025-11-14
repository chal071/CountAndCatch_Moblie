package com.example.countandcatch

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class JuegoCatchActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null

    private var slideDistance =
        0f //hacer que cuando se toque la pantalla, la cesta no salte a donde se ha puesto el dedo

    private var

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
        manageCountdown(countdownTimer)

        val background = findViewById<ImageView>(R.id.imgCatchFondo)
        val imgBasket = findViewById<ImageView>(R.id.imgCatchCesta)
        val imgApple = findViewById<ImageView>(R.id.imgCatchManzana)

        //TODO fix
        imgBasket.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    slideDistance = view.x - event.rawX
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + slideDistance
                    val minX = background.x
                    val maxX = background.x + background.width - view.width
                    view.x = newX.coerceIn(minX, maxX)
                }
            }
            true
        }

        dropItem(background, imgBasket, imgApple)
    }

    private fun manageCountdown(countdownTimer: TextView) {
        timer?.cancel()

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisLeft: Long) {
                var seconds = (millisLeft / 1000)
                var minutes = seconds / 60
                seconds = seconds % 60
                countdownTimer.text = String.format("%d:%02d", minutes, seconds)
            }

            //TODO fix this
            override fun onFinish() {
                onDestroy()
            }
        }.start()
    }

    private fun dropItem(background: ImageView, basket: ImageView, item: ImageView) {

        //val itemPosition = (Random.nextInt(0, background.width)).toFloat()
        val randomX = background.x + Random.nextFloat() * (background.width - item.width)
        item.x = 100.toFloat()
        item.y = background.y

        //TODO might be wrong
        val drop = ObjectAnimator.ofFloat(item, "translationY", item.x, background.y - background.height)

        /* val animator =
            ObjectAnimator.ofFloat(item, "y", background.y, background.y + background.height)
        animator.duration = 3500
        animator.interpolator = LinearInterpolator()
        animator.start()

        item.postDelayed(object : Runnable {
            override fun run() {
                if (item.y + item.height >= basket.y && item.x + item.width >= basket.x && item.x <= basket.x + basket.width) {
                    item.visibility = android.view.View.GONE
                } else if (item.y >= background.y + background.height) {
                    item.visibility = android.view.View.GONE
                } else {
                    item.postDelayed(this, 16)
                }
            }
        }, 16)
    }*/

    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}