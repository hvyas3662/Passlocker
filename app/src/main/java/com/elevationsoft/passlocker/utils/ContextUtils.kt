package com.elevationsoft.passlocker.utils

import android.content.Context
import android.widget.Toast

object ContextUtils {
    fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, msg, length).show()
    }
}