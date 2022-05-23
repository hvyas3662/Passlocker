package com.elevationsoft.customtabs

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.elevationsoft.customtabs.utils.Extensions.dp

class GroupLayout : LinearLayout {

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
            val ta = context.obtainStyledAttributes(it, R.styleable.GroupLayout)

            ta.recycle()
        }

    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        child?.let { cView ->
            if (cView is CustomButton) {

            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }


    companion object {
        private const val TAG = "CustomTabGroup"
        private val TAB_MIN_HEIGHT = 48.dp
    }

}