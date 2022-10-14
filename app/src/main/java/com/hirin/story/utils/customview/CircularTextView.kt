package com.hirin.story.utils.customview

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import kotlin.math.max


open class CircularTextView: MaterialTextView {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val r = max(measuredWidth, measuredHeight)
        setMeasuredDimension(r, r)
    }

    /*private var strokeWidth = 0f
    var strokeColor: Int = 0
    var solidColor2: Int = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val circlePaint = Paint()
        circlePaint.color = solidColor2
        circlePaint.flags = Paint.ANTI_ALIAS_FLAG

        val strokePaint = Paint()
        strokePaint.color = strokeColor
        strokePaint.flags = Paint.ANTI_ALIAS_FLAG

        val h = this.height
        val w = this.width

        val diameter = if (h > w) h else w
        val radius = diameter / 2

        this.height = diameter
        this.width = diameter

        canvas?.drawCircle(
            (diameter / 2).toFloat(), (diameter / 2).toFloat(),
            radius.toFloat(), strokePaint
        )

        canvas?.drawCircle(
            (diameter / 2).toFloat(),
            (diameter / 2).toFloat(), (radius - strokeWidth), circlePaint
        )
    }

    fun setStrokeWidth(dp: Int) {
        val scale = context.resources.displayMetrics.density
        strokeWidth = dp * scale
    }

    fun setStrokeColor(color: String?) {
        strokeColor = Color.parseColor(color)
    }

    fun setSolidColor2(color: String?) {
        solidColor2 = Color.parseColor(color)
    }*/
}