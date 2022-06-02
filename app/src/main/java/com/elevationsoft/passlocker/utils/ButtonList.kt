package com.elevationsoft.passlocker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elevationsoft.customtabs.utils.Extensions.dp
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.utils.common_classes.EqualSpacingItemDecoration
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager


@Suppress("unused")
class ButtonList {

    companion object {
        var ORIENTATION_HORIZONTAL = 36662
        var ORIENTATION_VERTICAL = 36663
        var FLAX_LAYOUT_MANAGER = 36664
        var LINEAR_LAYOUT_MANAGER = 36665
        var RECYCLER_VIEW_HEIGHT_MATCH_PARENT = -100
        var RECYCLER_VIEW_HEIGHT_WRAP_CONTENT = -200

        fun with(ctx: Context): ButtonList {
            val instance = ButtonList()
            instance.ctx = ctx
            instance.btnBg = ContextCompat.getColor(ctx, R.color.colorOverlay1)
            instance.btnFg = ContextCompat.getColor(ctx, R.color.textPrimary)
            instance.btnBorder = ContextCompat.getColor(ctx, R.color.textPrimary)

            instance.btnBgSelected = ContextCompat.getColor(ctx, R.color.colorAccent)
            instance.btnFgSelected = ContextCompat.getColor(ctx, R.color.textPrimary)
            instance.btnBorderSelected = ContextCompat.getColor(ctx, R.color.colorAccent)

            instance.borderWidth = 2.dp
            instance.borderRadius = 8.dp
            instance.orientation = ORIENTATION_HORIZONTAL
            instance.layoutManager = LINEAR_LAYOUT_MANAGER
            instance.gap = 6.dp
            instance.recyclerViewHeight = 48.dp
            return instance
        }
    }

    private lateinit var ctx: Context
    private var layoutManager = 0
    private var recyclerViewHeight = 0
    private lateinit var rv: RecyclerView
    private lateinit var llm: LinearLayoutManager
    private var borderWidth = 0
    private var borderRadius: Int = 0
    private var orientation = 0
    private var itemHeight = 0
    private var itemTextSize = 0
    private var gap = 0

    //colors
    private var btnBg = 0
    private var btnBgSelected: Int = 0
    private var btnFg: Int = 0
    private var btnFgSelected: Int = 0
    private var btnBorder: Int = 0
    private var btnBorderSelected: Int = 0

    private var listener: ItemListener? = null
    private var hasAutoScrolling = true

    private var disableClick = false

    //data and selections
    private var dataList: ArrayList<String> = ArrayList()
    private var selectedIndex = -1
    private var selectedItem: String = ""


    //getter setter
    fun setRecyclerViewHeight(dp: Int): ButtonList {
        recyclerViewHeight = dp.dp
        return this
    }

    fun setOrientation(orientation: Int): ButtonList {
        this.orientation = orientation
        return this
    }

    fun setLayoutManager(layout_manager: Int): ButtonList {
        this.layoutManager = layout_manager
        return this
    }

    fun setGapInItem(dp: Int): ButtonList {
        gap = dp.dp
        return this
    }

    fun setItemHeight(itemHeight: Int): ButtonList {
        this.itemHeight = itemHeight.dp
        return this
    }

    fun setItemTextSize(sp: Int): ButtonList {
        itemTextSize = sp
        return this
    }

    fun setBorderWidth(borderWidth: Int): ButtonList {
        this.borderWidth = borderWidth.dp
        return this
    }

    fun setBorderRadius(borderRadius: Int): ButtonList {
        this.borderRadius = borderRadius.dp
        return this
    }

    fun setSelectListener(listener: ItemListener?): ButtonList {
        this.listener = listener
        return this
    }

    fun setHasAutoScrolling(hasAutoScrolling: Boolean): ButtonList {
        this.hasAutoScrolling = hasAutoScrolling
        return this
    }

    fun setColors(
        btnBg: Int,
        btnBgSelected: Int,
        btnFg: Int,
        btnFgSelected: Int,
        btnBorder: Int,
        btnBorderSelected: Int
    ): ButtonList {
        this.btnBg = btnBg
        this.btnBgSelected = btnBgSelected
        this.btnFg = btnFg
        this.btnFgSelected = btnFgSelected
        this.btnBorder = btnBorder
        this.btnBorderSelected = btnBorderSelected
        return this
    }


    fun setDisableClick(disableClick: Boolean): ButtonList {
        this.disableClick = disableClick
        return this
    }

    fun setDataList(list: ArrayList<String>): ButtonList {
        dataList.clear()
        dataList.addAll(list)
        return this
    }

    fun getRvInstance(): RecyclerView {
        return rv
    }

    fun getSelectedIndex(): Int {
        return selectedIndex
    }

    fun setSelectedIndex(selectedIndex: Int): ButtonList {
        this.selectedIndex = selectedIndex
        return this
    }

    fun getSelectedItem(): String {
        return selectedItem
    }

    fun setSelectedItem(selectedItem: String): ButtonList {
        this.selectedItem = selectedItem
        return this
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItemList(list: ArrayList<String>) {
        dataList.clear()
        dataList.addAll(list)
        if (rv.adapter != null) {
            rv.adapter!!.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectedItem(item: String) {
        if (dataList.size > 0 && dataList.contains(item)) {
            selectedItem = item
            selectedIndex = dataList.indexOf(selectedItem)
            if (rv.adapter != null) {
                rv.adapter!!.notifyDataSetChanged()
                if (hasAutoScrolling) {
                    rv.scrollToPosition(selectedIndex)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectedIndex(index: Int) {
        if (dataList.size > index && index >= 0) {
            selectedItem = dataList[index]
            selectedIndex = index
            if (rv.adapter != null) {
                rv.adapter!!.notifyDataSetChanged()
                if (hasAutoScrolling) {
                    rv.scrollToPosition(selectedIndex)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelection() {
        selectedIndex = 0
        selectedItem = if (dataList.size > selectedIndex) {
            dataList[selectedIndex]
        } else {
            ""
        }
        if (rv.adapter != null) {
            rv.adapter!!.notifyDataSetChanged()
        }
    }


    fun createView(): RecyclerView {
        if (dataList.size == 0) {
            selectedIndex = 0
            selectedItem = ""
        } else {
            if (selectedIndex < 0) {
                selectedIndex = dataList.indexOf(selectedItem)
                if (selectedIndex < 0) {
                    selectedIndex = 0
                    selectedItem = dataList[selectedIndex]
                }
            } else if (dataList.size < selectedIndex) {
                selectedIndex = 0
                selectedItem = dataList[selectedIndex]
            }
        }


        rv = RecyclerView(ctx)

        when (recyclerViewHeight) {
            RECYCLER_VIEW_HEIGHT_MATCH_PARENT -> {
                rv.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            RECYCLER_VIEW_HEIGHT_WRAP_CONTENT -> {
                rv.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            else -> {
                rv.layoutParams =
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        recyclerViewHeight
                    )
            }
        }
        rv.setHasFixedSize(true)
        if (layoutManager == FLAX_LAYOUT_MANAGER) {
            val flexboxLayoutManager = FlexboxLayoutManager(ctx)
            flexboxLayoutManager.flexWrap = FlexWrap.WRAP
            flexboxLayoutManager.alignItems = AlignItems.FLEX_START
            flexboxLayoutManager.flexDirection = FlexDirection.ROW
            rv.layoutManager = flexboxLayoutManager
            val pixelIn8Dp: Int = 8.dp
            rv.setPadding(0, pixelIn8Dp, 0, pixelIn8Dp)
            rv.clipToPadding = false
        } else {
            val ori: Int = if (orientation == ORIENTATION_VERTICAL) {
                rv.addItemDecoration(
                    EqualSpacingItemDecoration(
                        gap,
                        EqualSpacingItemDecoration.HORIZONTAL
                    )
                )
                LinearLayoutManager.VERTICAL
            } else {
                rv.addItemDecoration(
                    EqualSpacingItemDecoration(
                        gap,
                        EqualSpacingItemDecoration.VERTICAL
                    )
                )
                LinearLayoutManager.HORIZONTAL
            }
            llm = object : LinearLayoutManager(ctx, ori, false) {
                override fun requestChildRectangleOnScreen(
                    parent: RecyclerView,
                    child: View,
                    rect: Rect,
                    immediate: Boolean,
                    focusedChildVisible: Boolean
                ): Boolean {
                    return false
                }
            }
            rv.layoutManager = llm
        }

        rv.adapter = ToggleButtonGroupAdapter(dataList, listener)
        if (hasAutoScrolling) {
            rv.scrollToPosition(selectedIndex)
        }

        return rv
    }

    //helper methods
    private fun makeBackgroundChecked(): Drawable {
        val gradientDrawableChecked = GradientDrawable()
        gradientDrawableChecked.shape = GradientDrawable.RECTANGLE
        gradientDrawableChecked.cornerRadius = borderRadius.toFloat()
        gradientDrawableChecked.setColor(btnBgSelected)
        gradientDrawableChecked.setStroke(borderWidth, btnBorderSelected)
        return gradientDrawableChecked
    }

    private fun makeBackgroundUnChecked(): Drawable {
        val gradientDrawableUnChecked = GradientDrawable()
        gradientDrawableUnChecked.shape = GradientDrawable.RECTANGLE
        gradientDrawableUnChecked.cornerRadius = borderRadius.toFloat()
        gradientDrawableUnChecked.setColor(btnBg)
        gradientDrawableUnChecked.setStroke(borderWidth, btnBorder)
        return gradientDrawableUnChecked
    }


    // class and interface
    interface ItemListener {
        fun onItemClick(item: String, index: Int)
    }


    inner class ToggleButtonGroupAdapter constructor(
        private var itemList: ArrayList<String>,
        private var listener: ItemListener?
    ) :
        RecyclerView.Adapter<ToggleButtonGroupAdapter.ViewMaker>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewMaker {
            val v = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.button_list_item, viewGroup, false)
            return ViewMaker(v)
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onBindViewHolder(holder: ViewMaker, i: Int) {
            holder.toggleBtn.text = itemList[i]
            if (i == selectedIndex) {
                holder.toggleBtn.background = makeBackgroundChecked()
                holder.toggleBtn.setTextColor(btnFgSelected)
            } else {
                holder.toggleBtn.background = makeBackgroundUnChecked()
                holder.toggleBtn.setTextColor(btnFg)
            }

            holder.toggleBtn.setOnClickListener(View.OnClickListener {
                if (disableClick) {
                    return@OnClickListener
                }

                selectedIndex = holder.bindingAdapterPosition
                selectedItem = itemList[selectedIndex]

                notifyDataSetChanged()
                if (listener != null) {
                    listener!!.onItemClick(selectedItem, selectedIndex)
                }
            })

        }


        override fun getItemCount(): Int = itemList.size

        inner class ViewMaker(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var toggleBtn: TextView

            init {
                toggleBtn = itemView.findViewById(R.id.toggleBtn)
                if (itemTextSize > 0) {
                    toggleBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, itemTextSize.toFloat())
                }
                if (layoutManager == FLAX_LAYOUT_MANAGER) {
                    val llp = toggleBtn.layoutParams as FlexboxLayoutManager.LayoutParams
                    llp.bottomMargin = gap
                    llp.rightMargin = gap
                    if (itemHeight > 0) {
                        llp.height = itemHeight
                    }
                    toggleBtn.layoutParams = llp
                } else {
                    if (itemHeight > 0) {
                        val llp = toggleBtn.layoutParams as LinearLayout.LayoutParams
                        llp.height = itemHeight
                        toggleBtn.layoutParams = llp
                    }
                }
            }
        }

    }


}