package com.hirin.story.utils.extension

import android.view.MotionEvent
import android.widget.TextView

private const val DRAWABLE_LEFT = 0
private const val DRAWABLE_TOP = 1
private const val DRAWABLE_RIGHT = 2
private const val DRAWABLE_BOTTOM = 3

fun TextView.handleDrawablesTouch(motionEvent: MotionEvent): Boolean {
    val leftDrawableTouched = handleLeftDrawableTouch(motionEvent)
    val topDrawableTouched = handleTopDrawableTouch(motionEvent)
    val rightDrawableTouched = handleRightDrawableTouch(motionEvent)
    val bottomDrawableTouched = handleBottomDrawableTouch(motionEvent)
    return leftDrawableTouched || topDrawableTouched || rightDrawableTouched || bottomDrawableTouched
}

fun TextView.handleLeftDrawableTouch(motionEvent: MotionEvent): Boolean {
    val leftDrawable = compoundDrawables[DRAWABLE_LEFT]
    return leftDrawable != null
            && motionEvent.rawX <= leftDrawable.bounds.width() - paddingLeft
}

fun TextView.handleTopDrawableTouch(motionEvent: MotionEvent): Boolean {
    val topDrawable = compoundDrawables[DRAWABLE_TOP]
    return topDrawable != null
            && motionEvent.rawY <= topDrawable.bounds.height() + paddingTop
}

fun TextView.handleRightDrawableTouch(motionEvent: MotionEvent): Boolean {
    val rightDrawable = compoundDrawables[DRAWABLE_RIGHT]
    return rightDrawable != null
            && motionEvent.rawX >= (right - rightDrawable.bounds.width() - paddingRight)
}

fun TextView.handleBottomDrawableTouch(motionEvent: MotionEvent): Boolean {
    val bottomDrawable = compoundDrawables[DRAWABLE_BOTTOM]
    return bottomDrawable != null
            && motionEvent.rawY >= (bottom - bottomDrawable.bounds.height() - paddingBottom)
}