package com.example.samplearchitecture.utils

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visibleIfOrGone(predicate: () -> Boolean) {
    if (predicate()) visible() else gone()
}

fun View.visibleIfOrInvisible(predicate: () -> Boolean) {
    if (predicate()) visible() else invisible()
}

fun View.disable() {
    alpha = 0.7f
    isClickable = false
    isEnabled = false
}

fun View.enable() {
    alpha = 1f
    isClickable = true
    isEnabled = true
}

fun View.enableIf(predicate: () -> Boolean) {
    if (predicate()) enable() else disable()
}

fun RecyclerView.addScrollListenerForDownView(downView: View, currentHeight: Float = 0f) : FloatingScrollListener {
    val listener = FloatingScrollListener(downView, currentHeight)
    addOnScrollListener(listener)
    return listener
}

fun NestedScrollView.addScrollListenerForDownView(downView: View, currentHeight: Float = 0f) : ScrollListener {
    val listener = ScrollListener(downView, currentHeight)
    setOnScrollChangeListener(listener)
    return listener
}

class ScrollListener(private val downView: View,
                     var currentHeight: Float = 0f): NestedScrollView.OnScrollChangeListener {

    override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
        val dy = oldScrollY - scrollY
        val value = if (dy > 0) 0f else downView.height.toFloat()
        // to prevent unneeded animation
        if (downView.translationY == value) return
        ObjectAnimator.ofFloat(downView, "translationY", value)
            .apply {
                duration = 300
                start()
            }
    }

}

class FloatingScrollListener(private val downView: View,
                             var currentHeight: Float = 0f): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {
            if (currentHeight.plus(dy) <= downView.height) currentHeight += dy
            else currentHeight = downView.height.toFloat()
        } else {
            if (currentHeight + dy >= 0) currentHeight += dy
            else currentHeight = 0f
        }

        downView.translationY = currentHeight
    }
}

