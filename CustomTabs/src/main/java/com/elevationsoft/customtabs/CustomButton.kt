package com.elevationsoft.customtabs

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.elevationsoft.customtabs.utils.Extensions.dp


class CustomButton : LinearLayout {
    @DrawableRes
    private var icon: Int = 0
    private var label: String = ""
    private var iconWidth = 24.dp
    private var iconHeight = 24.dp
    private var iconLabelGap = 6.dp
    var tabSelected = false

    private lateinit var ivIcon: ImageView
    private lateinit var tvLabel: TextView

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        attrs?.let {

            val ta = context.obtainStyledAttributes(it, R.styleable.CustomButton)

            if (ta.hasValue(R.styleable.CustomButton_icon)) {
                icon = ta.getResourceId(R.styleable.CustomButton_icon, 0)
            }

            if (ta.hasValue(R.styleable.CustomButton_iconWidth)) {
                iconWidth =
                    ta.getInt(R.styleable.CustomButton_iconWidth, iconWidth)
            }

            if (ta.hasValue(R.styleable.CustomButton_iconHeight)) {
                iconHeight =
                    ta.getInt(R.styleable.CustomButton_iconHeight, iconHeight)
            }

            if (ta.hasValue(R.styleable.CustomButton_iconLabelGap)) {
                iconLabelGap =
                    ta.getInt(R.styleable.CustomButton_iconLabelGap, iconLabelGap)
            }

            if (ta.hasValue(R.styleable.CustomButton_labelText)) {
                label = ta.getString(R.styleable.CustomButton_labelText) ?: ""
            }

            if (ta.hasValue(R.styleable.CustomButton_isSelected)) {
                tabSelected = ta.getBoolean(R.styleable.CustomButton_isSelected, false)
            }

            ta.recycle()
        }

        ivIcon = ImageView(context)
        val llp = LayoutParams(iconWidth, iconHeight)
        llp.marginEnd = iconLabelGap
        ivIcon.layoutParams = llp

        tvLabel = TextView(context)
        val llpLabel = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        tvLabel.layoutParams = llpLabel

        updateIcon(icon)
        updateLabel(label)
    }

    fun setIcon(@DrawableRes icon: Int) {
        this.icon = icon
        updateIcon(this.icon)
    }

    fun setLabel(@StringRes strInt: Int) {
        label = context.getString(strInt)
        updateLabel(label)
    }

    fun setLabel(str: String) {
        this.label = str
        updateLabel(label)
    }


    fun updateIcon(@DrawableRes icon: Int) {
        ivIcon.setImageResource(icon)
    }

    fun updateLabel(str: String) {
        tvLabel.text = str
    }

    fun updateIconTint(@ColorInt color: Int) {
        ivIcon.setColorFilter(color)
    }

    fun updateLabelColor(@ColorInt color: Int) {
        tvLabel.setTextColor(color)
    }

    fun updateLabelSize(size: Float) {
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun updateLabelFont(font: Int) {
        val typeface = ResourcesCompat.getFont(context, font)
        tvLabel.typeface = typeface
    }

}