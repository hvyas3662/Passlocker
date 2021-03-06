package com.elevationsoft.passlocker.presentation.home_screen.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elevationsoft.passlocker.databinding.CategoryItemBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import java.util.*


class CategoryListAdapter(
    private var categoryList: List<Category>,
    private val callback: CategoryItemClickCallback,
    private val onItemStartDragListener: OnStartDragListener
) : RecyclerView.Adapter<CategoryListAdapter.ViewMaker>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewMaker {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewMaker(binding)
    }

    override fun onBindViewHolder(holder: ViewMaker, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = categoryList.size

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewMaker(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cvMain.setOnClickListener {
                if (categoryList[bindingAdapterPosition].id != -1L) {
                    callback.onCategoryClicked(categoryList[bindingAdapterPosition])
                }
            }

            binding.ivDelete.setOnClickListener {
                callback.onCategoryDeleteClicked(categoryList[bindingAdapterPosition])
            }

            binding.ivMove.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    onItemStartDragListener.onStartDrag(this)
                }
                false
            }
        }

        fun bind() {
            val cat = categoryList[bindingAdapterPosition]
            binding.tvCategoryName.text = cat.categoryName

            if (cat.id == -1L) {
                binding.divider.hide()
                binding.ivMove.hide()
                binding.ivDelete.hide()
            } else {
                binding.divider.show()
                binding.ivMove.show()
                binding.ivDelete.show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCategoryList(catList: List<Category>) {
        categoryList = catList
        notifyDataSetChanged()
    }

    fun onItemMove(fromPosition: Int, toPos: Int): Boolean {
        var toPosition = toPos
        if (toPosition == 0) {
            toPosition = 1
        }
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(categoryList, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(categoryList, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    fun getNewCategoryPositionList(): List<Long> {
        val resultList: MutableList<Long> = mutableListOf()
        categoryList.forEach {
            if (it.id > 0L) {
                resultList.add(it.id)
            }
        }
        return resultList
    }

    interface CategoryItemClickCallback {
        fun onCategoryClicked(category: Category)
        fun onCategoryDeleteClicked(category: Category)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }
}