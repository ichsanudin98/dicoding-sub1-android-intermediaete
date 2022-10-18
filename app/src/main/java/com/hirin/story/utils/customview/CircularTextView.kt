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
}