package com.example.countandcatch

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.Image
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
import androidx.core.view.postDelayed
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class JuegoCatchActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null
    private var isGameRunning: Boolean = true;
    private var isAppleFalling: Boolean = true;
    private var score: Int = 0;
    private var negativeSocre: Int = 0;
    private var dX =
        0f //hacer que cuando se toque la pantalla, la cesta no salte a donde se ha puesto el dedo

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
        val background = findViewById<ImageView>(R.id.imgCatchFondo)
        val basketSlide = findViewById<ImageView>(R.id.imgCatchCesta)
        val appleFall = findViewById<ImageView>(R.id.imgCatchManzana)
        val btnStart = findViewById<ImageView>(R.id.catchImgBtnStart)

        btnStart.setOnClickListener {
            Executors.newSingleThreadScheduledExecutor().schedule({
                startGame(countdownTimer, background, basketSlide, appleFall)
                }, 2, TimeUnit.SECONDS)
        }
    }

    private fun startGame(countdownTimer: TextView, background: ImageView, basketSlide: ImageView, appleFall: ImageView){
        manageCountdown(countdownTimer)
        dropItem(background, basketSlide, appleFall)
    }

    private fun slideBasket(basketSlide: ImageView, background: ImageView) {
        val minX = background.x
        val maxX = background.x + background.width - basketSlide.width

        @SuppressLint("ClickableViewAccessibility")
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
    }

    private fun manageCountdown(countdownTimer: TextView) {
        timer?.cancel()

        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisLeft: Long) {

                val seconds = (millisLeft / 1000).toInt()
                val minutes = seconds / 60
                val secs = seconds % 60
                countdownTimer.text = String.format("%d:%02d", minutes, secs)
            }

            override fun onFinish() {
                countdownTimer.text = "0:00"
                isGameRunning = false;
            }
        }.start()
    }

    private fun dropItem(background: ImageView, basket: ImageView, item: ImageView) {
        if (isGameRunning || !isAppleFalling) {

            isAppleFalling = true
            item.visibility = android.view.View.VISIBLE

            val randomX = background.x + Random.nextFloat() * (background.width - item.width)
            item.x = randomX
            item.y = background.y

            val animator =
                ObjectAnimator.ofFloat(item, "y", background.y, background.y + background.height)
            animator.duration = 3000
            animator.interpolator = LinearInterpolator()

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    isAppleFalling = false
                    if (isGameRunning) {
                        item.postDelayed({
                                             dropItem(background, basket, item)
                                         }, 500)
                    }
                }
            })

            animator.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}