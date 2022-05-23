package com.elevationsoft.customtabs.utils

import android.content.res.Resources.getSystem

object Extensions {

    //return pixel
    val Int.dp: Int get() = (this * getSystem().displayMetrics.density).toInt()


}