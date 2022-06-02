package com.elevationsoft.passlocker.utils

import android.view.View

object ViewUtils {


    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }
}