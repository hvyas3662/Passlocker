package com.elevationsoft.customtabs.utils

import android.content.res.Resources.getSystem
import android.graphics.Color
import android.view.View
import java.util.*

object Extensions {

    //return pixel
    val Int.dp: Int get() = (this * getSystem().displayMetrics.density).toInt()

    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    fun View.animateX(x: Int) {

    }
}