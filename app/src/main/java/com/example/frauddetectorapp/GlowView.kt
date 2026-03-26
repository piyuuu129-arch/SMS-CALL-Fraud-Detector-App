package com.example.frauddetectorapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class GlowView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val glowPaint = Paint().apply {
        color = Color.parseColor("#00E5FF")
        style = Paint.Style.STROKE   // 🔥 IMPORTANT (border glow)
        strokeWidth = 3f             // thickness of border
        isAntiAlias = true
        maskFilter = BlurMaskFilter(120f, BlurMaskFilter.Blur.OUTER) // 🔥 soft neon
    }

    private val borderPaint = Paint().apply {
        color = Color.parseColor("#00E5FF")
        style = Paint.Style.STROKE
        strokeWidth = 3f
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rect = RectF(
            30f,
            30f,
            width - 30f,
            height - 30f
        )

        // 🔥 Outer glow
        canvas.drawRoundRect(rect, 50f, 50f, glowPaint)

        // 🔥 Sharp border on top
        canvas.drawRoundRect(rect, 50f, 50f, borderPaint)
    }
}