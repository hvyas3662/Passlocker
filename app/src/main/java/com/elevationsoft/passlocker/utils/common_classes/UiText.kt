package com.elevationsoft.passlocker.utils.common_classes

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {

    class DynamicText(val text: String) : UiText()

    class StaticText(@StringRes val strId: Int, vararg val values: String) : UiText()

    fun asString(context: Context): String {
        return when (this@UiText) {
            is DynamicText -> text
            is StaticText -> context.getString(strId, *values)
        }
    }

}
