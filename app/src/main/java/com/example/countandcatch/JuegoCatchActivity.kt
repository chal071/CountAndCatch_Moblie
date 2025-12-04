package com.example.countandcatch

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.example.countandcatch.utils.JsonHelper
import kotlin.random.Random

class JuegoCatchActivity : AppCompatActivity() {

    private var isGameRunning: Boolean = true
    private var positivePoints: Int = 0
    private var negativePoints: Int = 0
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private var elapsedSeconds = 0
    private var timerView: TextView? = null
    private var partida: Partida? = null
    private var maxErrors: Int = 0

    // hacer que cuando se toque la pantalla, la cesta no salte a donde se ha puesto el dedo
    private var dX = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego_catch)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainJuegoCatch)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        partida = intent.getSerializableExtra("partida") as? Partida ?: return

        val layout = findViewById<ConstraintLayout>(R.id.catchLayout)
        val basketSlide = findViewById<ImageView>(R.id.imgCatchCesta)
        val vida1 = findViewById<ImageView>(R.id.imgCatchManzanaVida1)
        val vida2 = findViewById<ImageView>(R.id.imgCatchManzanaVida2)
        val vida3 = findViewById<ImageView>(R.id.imgCatchManzanaVida3)

        maxErrors = when (partida?.dificultad) {
            1 -> 3
            2 -> 2
            3 -> 1
            else -> 3
        }

        layout.post {
            centerBasket(basketSlide, layout)
        }

        initializeLives(vida1, vida2, vida3)
        manageHomebtn()
        manageStartbtn(layout, basketSlide, vida1, vida2, vida3)

    }

    private fun manageHomebtn(){
        val btnHome = findViewById<ImageButton>(R.id.catchBtnHome)

        btnHome.setOnClickListener {
            isGameRunning = false
            saveIncompleteGame()

            val intent = Intent(this, ElegirJuegosActivity::class.java)
            intent.putExtra("partida", partida)
            startActivity(intent)
            finish()
        }
    }

    private fun manageStartbtn(layout: ConstraintLayout, basketSlide: ImageView, vida1: ImageView, vida2: ImageView, vida3: ImageView){
        val btnStart = findViewById<ImageView>(R.id.catchImgBtnStart)
        val appleFall = findViewById<ImageView>(R.id.imgCatchManzana)
        val appleFall2 = findViewById<ImageView>(R.id.imgCatchManzana2)
        val timer = findViewById<TextView>(R.id.catchTxtTimer)
        val background = findViewById<ImageView>(R.id.imgCatchFondo)

        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE

            background.post {
                startGame(layout, basketSlide, appleFall, appleFall2, timer, vida1, vida2, vida3)
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

    private fun startGame(
        layout: ConstraintLayout,
        basketSlide: ImageView,
        appleFall: ImageView,
        appleFall2: ImageView,
        timer: TextView,
        vida1: ImageView,
        vida2: ImageView,
        vida3: ImageView
    ) {
        startTimer(timer)
        slideBasket(basketSlide, layout)

        // 主苹果负责循环；顺便有概率带上第二个
        dropItem(
            layout = layout,
            basket = basketSlide,
            item = appleFall,
            vida1 = vida1,
            vida2 = vida2,
            vida3 = vida3,
            isMainApple = true,
            extraApple = appleFall2
        )
    }

    private val timeRunnable = object : Runnable {
        @SuppressLint("SetTextI18n")
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

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return sdf.format(java.util.Date())
    }

    private fun centerBasket(basketSlide: ImageView, layout: ConstraintLayout) {
        val centerX = layout.x + (layout.width - basketSlide.width) / 2
        basketSlide.x = centerX
    }

    private fun slideBasket(basketSlide: ImageView, layout: ConstraintLayout) {
        val minX = layout.x
        val maxX = layout.x + layout.width - basketSlide.width

        @SuppressLint("ClickableViewAccessibility")
        basketSlide.setOnTouchListener { view, event ->
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

    private fun dropItem(
        layout: ConstraintLayout,
        basket: ImageView,
        item: ImageView,
        vida1: ImageView,
        vida2: ImageView,
        vida3: ImageView,
        isMainApple: Boolean,
        extraApple: ImageView? = null
    ) {
        if (!isGameRunning) return

        item.visibility = View.VISIBLE

        val randomX = layout.x + Random.nextFloat() * (layout.width - item.width)
        item.x = randomX
        item.y = layout.y

        val dificultad = partida?.dificultad ?: 1

        val baseDuration = when (dificultad) {
            1 -> 3200L
            2 -> 2200L
            3 -> 1400L
            else -> 3000L
        }
        val fallDuration = if (isMainApple) {
            baseDuration
        } else {
            (baseDuration * 1.5f).toLong()
        }

        val animator = ObjectAnimator.ofFloat(item, "y", layout.y, layout.y + layout.height)
        animator.duration = fallDuration
        animator.interpolator = LinearInterpolator()

        var hasScored = false

        animator.addUpdateListener {
            if (!isGameRunning) {
                animator.cancel()
                item.visibility = View.GONE
                return@addUpdateListener
            }

            if (!hasScored) {
                val hitBasket =
                    item.y + item.height >= basket.y &&
                            item.x + item.width >= basket.x &&
                            item.x <= basket.x + basket.width &&
                            item.y <= basket.y + basket.height

                val outOfScreen =
                    item.y + item.height >= layout.y + layout.height

                if (hitBasket) {
                    hasScored = true
                    positivePoints++
                    animator.cancel()
                    item.visibility = View.GONE
                } else if (outOfScreen) {
                    hasScored = true
                    negativePoints++
                    animator.cancel()
                    item.visibility = View.GONE

                    updateLives(negativePoints, vida1, vida2, vida3)

                    if (negativePoints >= maxErrors) {
                        endGame(positivePoints)
                    }
                }
            }
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                if (isMainApple && extraApple != null) {
                    val dificultad = partida?.dificultad ?: 1
                    val spawnTwo = dificultad == 3 && Random.nextFloat() < 0.4f

                    if (spawnTwo && extraApple.visibility != View.VISIBLE && isGameRunning) {
                        dropItem(
                            layout = layout,
                            basket = basket,
                            item = extraApple,
                            vida1 = vida1,
                            vida2 = vida2,
                            vida3 = vida3,
                            isMainApple = false,
                            extraApple = null
                        )
                    }
                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                if (!isGameRunning) {
                    item.visibility = View.GONE
                    return
                }

                if (isMainApple && isGameRunning) {
                    val dificultad = partida?.dificultad ?: 1
                    val delay = if (dificultad == 3) 200L else 300L

                    item.postDelayed({
                        if (isGameRunning) {
                            dropItem(
                                layout = layout,
                                basket = basket,
                                item = item,
                                vida1 = vida1,
                                vida2 = vida2,
                                vida3 = vida3,
                                isMainApple = true,
                                extraApple = extraApple
                            )
                        }
                    }, delay)
                }
            }
        })

        animator.start()
    }

    private fun updateLives(negativePoints: Int, vida1: ImageView, vida2: ImageView, vida3: ImageView) {
        val livesLeft = maxErrors - negativePoints

        when (livesLeft) {
            2 -> vida3.visibility = View.GONE
            1 -> vida2.visibility = View.GONE
            0 -> vida1.visibility = View.GONE
        }
    }

    private fun saveIncompleteGame() {
        handler.removeCallbacks(timeRunnable)

        val base = partida
        if (base != null) {
            val partidaIncompleta = base.copy(
                tiempo_partida = elapsedSeconds,
                fecha_hora = getDateTime(),
                puntos = positivePoints,
                terminada = 0
            )

            val lista = JsonHelper.loadList<Partida>(this).toMutableList()
            lista.add(partidaIncompleta)
            JsonHelper.saveList(this, lista)
        }
    }

    private fun endGame(positivePoints: Int) {
        handler.removeCallbacks(timeRunnable)
        isGameRunning = false

        val base = partida
        if (base != null) {
            val updated = base.copy(
                tiempo_partida = elapsedSeconds,
                fecha_hora = getDateTime(),
                puntos = positivePoints,
                terminada = 1
            )
            val intent = Intent(this@JuegoCatchActivity, ResultadoActivity::class.java)
            intent.putExtra("partida", updated)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeRunnable)
    }
}
