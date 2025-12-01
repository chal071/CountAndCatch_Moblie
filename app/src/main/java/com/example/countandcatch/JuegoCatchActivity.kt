package com.example.countandcatch

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.countandcatch.data.Partida
import kotlin.random.Random

class JuegoCatchActivity : AppCompatActivity() {

    private var isGameRunning: Boolean = true;
    private var isAppleFalling: Boolean = false;
    private var positivePoints: Int = 0
    private var negativePoints: Int = 0
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private var elapsedSeconds = 0
    private var timerView: TextView? = null
    private var partida: Partida? = null
    private var maxErrors: Int = 0
    private var startTime = 0L

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

        val layout = findViewById<ConstraintLayout>(R.id.catchLayout)
        val btnHome = findViewById<ImageButton>(R.id.catchBtnHome)
        val timer = findViewById<TextView>(R.id.catchTxtTimer)
        val background = findViewById<ImageView>(R.id.imgCatchFondo)
        val basketSlide = findViewById<ImageView>(R.id.imgCatchCesta)
        val appleFall = findViewById<ImageView>(R.id.imgCatchManzana)
        val btnStart = findViewById<ImageView>(R.id.catchImgBtnStart)
        val vida1 = findViewById<ImageView>(R.id.imgCatchManzanaVida1)
        val vida2 = findViewById<ImageView>(R.id.imgCatchManzanaVida2)
        val vida3 = findViewById<ImageView>(R.id.imgCatchManzanaVida3)

        partida = intent.getSerializableExtra("partida") as? Partida

        maxErrors = when (partida?.dificultad){
            1 -> 3
            2 -> 2
            3 -> 1
            else -> 3
        }

        initializeLives(vida1, vida2, vida3)

        btnHome.setOnClickListener {
            val intent = Intent(this, InicioSesionActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnStart.setOnClickListener {
            btnStart.visibility = android.view.View.GONE

            background.post {
                startGame(layout, basketSlide, appleFall, timer, vida1, vida2, vida3)
            }
        }
    }

    private fun initializeLives(vida1: ImageView, vida2: ImageView, vida3: ImageView) {
        when (maxErrors) {
            1 -> {
                vida1.visibility = View.VISIBLE
                vida2.visibility = View.GONE
                vida3.visibility = View.GONE
            }
            2 -> {
                vida1.visibility = View.VISIBLE
                vida2.visibility = View.VISIBLE
                vida3.visibility = View.GONE
            }
            3 -> {
                vida1.visibility = View.VISIBLE
                vida2.visibility = View.VISIBLE
                vida3.visibility = View.VISIBLE
            }
        }
    }
    private fun startGame(layout: ConstraintLayout, basketSlide: ImageView,
                          appleFall: ImageView, timer: TextView,
                          vida1: ImageView, vida2: ImageView, vida3: ImageView) {

        startTimer(timer)
        slideBasket(basketSlide, layout)
        dropItem(layout, basketSlide, appleFall, vida1, vida2, vida3)
    }

    private val timeRunnable = object : Runnable {
        override fun run() {
            elapsedSeconds++
            timerView?.text = "${elapsedSeconds}s"
            handler.postDelayed(this, 1000)
        }
    }

    private fun startTimer(timer: TextView) {
        timerView = timer
        elapsedSeconds = 0
        timerView?.text = "0s"
        handler.postDelayed(timeRunnable, 1000)
    }

    private fun getDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd")
        return sdf.format(java.util.Date())
    }

    private fun slideBasket(basketSlide: ImageView, layout: ConstraintLayout) {
        val minX = layout.x
        val maxX = layout.x + layout.width - basketSlide.width

        @SuppressLint("ClickableViewAccessibility") basketSlide.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - event.rawX
                }

                MotionEvent.ACTION_MOVE -> {
                    val newX = event.rawX + dX
                    view.x = newX.coerceIn(minX, maxX)
                }
            }
            true
        }
    }

    private fun dropItem(layout: ConstraintLayout, basket: ImageView, item: ImageView,
                         vida1: ImageView, vida2: ImageView, vida3: ImageView) {
        if (isGameRunning && !isAppleFalling) {

            isAppleFalling = true
            item.visibility = android.view.View.VISIBLE

            val randomX = layout.x + Random.nextFloat() * (layout.width - item.width)
            item.x = randomX
            item.y = layout.y

            val animator =
                ObjectAnimator.ofFloat(item, "y", layout.y, layout.y + layout.height)
            animator.duration = 3000
            animator.interpolator = LinearInterpolator()

            var hasScored = false

            animator.addUpdateListener {
                if (!hasScored) {

                    if (item.y + item.height >= basket.y &&
                        item.x + item.width >= basket.x &&
                        item.x <= basket.x + basket.width &&
                        item.y <= basket.y + basket.height) {

                        hasScored = true
                        positivePoints++
                        animator.cancel()
                        item.visibility = android.view.View.GONE
                        isAppleFalling = false

                    } else if (item.y + item.height >= layout.y + layout.height) {
                        hasScored = true
                        negativePoints++
                        animator.cancel()
                        item.visibility = android.view.View.GONE
                        isAppleFalling = false

                        updateLives(negativePoints, vida1, vida2, vida3)

                        if (negativePoints >= maxErrors){
                            endGame(positivePoints)
                        }
                    }
                }
            }

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    isAppleFalling = false
                    if (isGameRunning) {

                        if (partida?.dificultad == 3) {
                            item.postDelayed({
                                                 dropItem(layout, basket, item,
                                                     vida1, vida2, vida3)
                                             }, 200)
                        } else {
                            item.postDelayed({
                                                 dropItem(layout, basket, item,
                                                     vida1, vida2, vida3)
                                             }, 300)
                        }
                    }
                }
            })
            animator.start()
        }
    }

    private fun updateLives(negativePoints: Int, vida1: ImageView, vida2: ImageView, vida3: ImageView) {
        val livesLeft = maxErrors - negativePoints

        when (livesLeft) {
            2 -> vida3.visibility = View.GONE
            1 -> vida2.visibility = View.GONE
            0 -> vida1.visibility = View.GONE
        }
    }

    private fun endGame(positivePoints: Int){
        handler.removeCallbacks(timeRunnable)
        isGameRunning = false

        val base = partida
        if (base != null) {
            val updated = base.copy(
                tiempo_partida = elapsedSeconds,
                fecha = getDate(),
                puntos = positivePoints
            )
            val intent = Intent(this@JuegoCatchActivity, ResultadoActivity::class.java)
            intent.putExtra("partida", updated)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}