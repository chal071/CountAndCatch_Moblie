package com.example.countandcatch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class LineView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.parseColor("#419DFF")
        strokeWidth = 8f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
    }

    private val lines = mutableListOf<Pair<PointF, PointF>>()

    fun addLine(start: PointF, end: PointF) {
        lines.add(start to end)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
    }
}
