package com.elevationsoft.customtabs

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.elevationsoft.customtabs.utils.Extensions.dp

@Suppress("unused")
class GroupLayout : FrameLayout {
    private lateinit var clipView: View
    private lateinit var tabLayout: LinearLayout
    private val tabList = ArrayList<CustomButton>()
    private val minHeight = 48.dp

    private var background: Int = R.drawable.tab_bg
    private var clipBg: Int = R.drawable.clip_bg
    private var fullPadding: Float = 8.dp.toFloat()

    @ColorInt
    private var tabIconTint = Color.WHITE

    @ColorInt
    private var selectedTabIconTint = Color.WHITE

    @ColorInt
    private var tabTextColor = Color.WHITE
    private var tabTextSize = 16
    private var tabFontFamily: Int = 0

    @ColorInt
    private var selectedTabTextColor = Color.WHITE
    private var selectedTabTextSize = 16
    private var selectedTabFontFamily: Int = 0

    private var selectedTabIndex = 0

    private var animationDuration = 150

    private var tabChangedCallBack: ((Int) -> Unit)? = null

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

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.GroupLayout)

            if (ta.hasValue(R.styleable.GroupLayout_background)) {
                background = ta.getResourceId(R.styleable.GroupLayout_background, background)
            }

            if (ta.hasValue(R.styleable.GroupLayout_clipBg)) {
                clipBg = ta.getResourceId(R.styleable.GroupLayout_clipBg, clipBg)
            }

            if (ta.hasValue(R.styleable.GroupLayout_fullPadding)) {
                fullPadding = ta.getDimension(R.styleable.GroupLayout_fullPadding, fullPadding)
            }

            if (ta.hasValue(R.styleable.GroupLayout_tabIconTint)) {
                tabIconTint = ta.getColor(R.styleable.GroupLayout_tabIconTint, tabIconTint)
            }

            if (ta.hasValue(R.styleable.GroupLayout_selectedTabIconTint)) {
                selectedTabIconTint =
                    ta.getColor(R.styleable.GroupLayout_selectedTabIconTint, selectedTabIconTint)
            }

            if (ta.hasValue(R.styleable.GroupLayout_tabTextColor)) {
                tabTextColor =
                    ta.getColor(R.styleable.GroupLayout_tabTextColor, tabTextColor)
            }

            if (ta.hasValue(R.styleable.GroupLayout_tabTextSize)) {
                tabTextSize =
                    ta.getDimensionPixelSize(R.styleable.GroupLayout_tabTextSize, tabTextSize)
            }

            if (ta.hasValue(R.styleable.GroupLayout_tabFontFamily)) {
                tabFontFamily =
                    ta.getResourceId(R.styleable.GroupLayout_tabFontFamily, tabFontFamily)
            }


            if (ta.hasValue(R.styleable.GroupLayout_selectedTabTextColor)) {
                selectedTabTextColor =
                    ta.getColor(R.styleable.GroupLayout_selectedTabTextColor, selectedTabTextColor)
            }

            if (ta.hasValue(R.styleable.GroupLayout_selectedTabTextSize)) {
                selectedTabTextSize =
                    ta.getDimensionPixelSize(
                        R.styleable.GroupLayout_selectedTabTextSize,
                        selectedTabTextSize
                    )
            }

            if (ta.hasValue(R.styleable.GroupLayout_selectedTabFontFamily)) {
                selectedTabFontFamily =
                    ta.getResourceId(
                        R.styleable.GroupLayout_selectedTabFontFamily,
                        selectedTabFontFamily
                    )
            }

            if (ta.hasValue(R.styleable.GroupLayout_selectedTabIndex)) {
                selectedTabIndex =
                    ta.getInt(R.styleable.GroupLayout_selectedTabIndex, selectedTabIndex)
            }

            if (ta.hasValue(R.styleable.GroupLayout_tabAnimationDuration)) {
                animationDuration =
                    ta.getInt(R.styleable.GroupLayout_tabAnimationDuration, animationDuration)
            }

            ta.recycle()
        }

        minimumHeight = minHeight
        setBackgroundResource(background)
        setPadding(
            fullPadding.toInt(),
            fullPadding.toInt(),
            fullPadding.toInt(),
            fullPadding.toInt()
        )

        //add clip view
        clipView = View(context)
        val clipViewLlp = LayoutParams(0, LayoutParams.MATCH_PARENT)
        clipView.layoutParams = clipViewLlp
        clipView.setBackgroundResource(clipBg)
        addView(clipView)

        //add linear layout
        tabLayout = LinearLayout(context)
        val tabLayoutLlp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        tabLayout.layoutParams = tabLayoutLlp
        tabLayout.setBackgroundColor(Color.TRANSPARENT)
        addView(tabLayout)

        tabLayout.orientation = LinearLayout.HORIZONTAL
        tabLayout.gravity = Gravity.CENTER


    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val clipParams = clipView.layoutParams
            clipParams.width = tabLayout.measuredWidth / tabList.size
            clipView.layoutParams = clipParams
            moveClip(false)
        }
    }


    fun addView(child: CustomButton) {
        addTab(child)
    }

    override fun addView(child: View?) {
        if (child is CustomButton) {
            addTab(child)
        } else {
            super.addView(child)
        }
    }


    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (child is CustomButton) {
            addTab(child)
        } else {
            super.addView(child, params)
        }
    }


    private fun addTab(child: View?) {
        child?.let { cView ->
            if (cView is CustomButton) {
                tabList.add(cView)
                val llp = LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)

                tabLayout.addView(cView, llp)
                cView.setOnClickListener {
                    val index = tabList.indexOf(it)
                    if (index >= 0 && selectedTabIndex != index) {
                        selectedTabIndex = index
                        moveClip(true)
                        tabChangedCallBack?.let { function ->
                            function(selectedTabIndex)
                        }
                    }
                }
            }
        }
    }


    private fun moveClip(animation: Boolean = true) {
        if (tabList.size > selectedTabIndex) {
            val oneTabWidth = tabLayout.measuredWidth / tabList.size
            val newClipX = (selectedTabIndex * oneTabWidth).toFloat()

            if (animation) {
                clipView.animate()
                    .setDuration(animationDuration.toLong())
                    .translationX(newClipX)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) {

                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            updateSelectedState()
                        }

                        override fun onAnimationCancel(animation: Animator?) {

                        }

                        override fun onAnimationRepeat(animation: Animator?) {

                        }

                    })
                    .start()
            } else {
                clipView.translationX = newClipX
                updateSelectedState()
            }


        }
    }


    private fun updateSelectedState() {
        tabList.forEachIndexed { index, btn ->
            if (index == selectedTabIndex) {
                btn.updateIconTint(selectedTabIconTint)
                btn.updateLabelColor(selectedTabTextColor)
                btn.updateLabelSize(selectedTabTextSize)
                btn.updateLabelFont(selectedTabFontFamily)
                btn.setIsTabSelected(true)
            } else {
                btn.updateIconTint(tabIconTint)
                btn.updateLabelColor(tabTextColor)
                btn.updateLabelSize(tabTextSize)
                btn.updateLabelFont(tabFontFamily)
                btn.setIsTabSelected(false)
            }
        }
    }

    fun getSelectedTabIndex(): Int {
        return selectedTabIndex
    }

    fun setOnTabChangedCallBack(callback: (Int) -> Unit) {
        tabChangedCallBack = callback
    }

    interface OnTabChangeCallBack {
        fun onTabChanged(tabIndex: Int);
    }
}