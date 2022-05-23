package com.elevationsoft.customtabs

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
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

@Suppress("unused")
class CustomButton : LinearLayout {
    @DrawableRes
    private var icon: Int = 0
    private var label: String = "Tab 1"
    private var iconWidth = 24.dp.toFloat()
    private var iconHeight = 24.dp.toFloat()
    private var iconLabelGap = 6.dp.toFloat()
    private var paddingStartEnd = 8.dp.toFloat()
    private var tabSelected = false

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
                    ta.getDimension(R.styleable.CustomButton_iconWidth, iconWidth)
            }

            if (ta.hasValue(R.styleable.CustomButton_iconHeight)) {
                iconHeight =
                    ta.getDimension(R.styleable.CustomButton_iconHeight, iconHeight)
            }

            if (ta.hasValue(R.styleable.CustomButton_iconLabelGap)) {
                iconLabelGap =
                    ta.getDimension(R.styleable.CustomButton_iconLabelGap, iconLabelGap)
            }

            if (ta.hasValue(R.styleable.CustomButton_paddingStartEnd)) {
                paddingStartEnd =
                    ta.getDimension(R.styleable.CustomButton_paddingStartEnd, paddingStartEnd)
            }

            if (ta.hasValue(R.styleable.CustomButton_labelText)) {
                label = ta.getString(R.styleable.CustomButton_labelText) ?: ""
            }

            ta.recycle()
        }

        setPadding(paddingStartEnd.toInt(), 0, paddingStartEnd.toInt(), 0)

        setBackgroundColor(Color.TRANSPARENT)
        ivIcon = ImageView(context)
        val llp = LayoutParams(iconWidth.toInt(), iconHeight.toInt())
        llp.marginEnd = iconLabelGap.toInt()
        ivIcon.layoutParams = llp
        addView(ivIcon)

        tvLabel = TextView(context)
        val llpLabel = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        tvLabel.layoutParams = llpLabel
        tvLabel.maxLines = 1
        tvLabel.ellipsize = TextUtils.TruncateAt.END
        tvLabel.setBackgroundColor(Color.TRANSPARENT)
        updateLabelSize(16)
        addView(tvLabel)

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

    fun setIsTabSelected(selected: Boolean) {
        tabSelected = selected
    }


    fun updateIcon(@DrawableRes icon: Int) {
        ivIcon.setImageResource(icon)
        if (icon == 0) {
            ivIcon.visibility = GONE
        } else {
            ivIcon.visibility = VISIBLE
        }
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

    fun updateLabelSize(size: Int) {
        tvLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
    }

    fun updateLabelFont(font: Int) {
        if (font != 0) {
            val typeface = ResourcesCompat.getFont(context, font)
            tvLabel.typeface = typeface
        }
    }

}