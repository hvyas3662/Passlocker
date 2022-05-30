package com.elevationsoft.passlocker.presentation.home_screen.category

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elevationsoft.passlocker.databinding.CategoryItemBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show

class CategoryListAdapter(
    private var categoryList: List<Category>,
    private val callback: CategoryItemClickCallback
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

    inner class ViewMaker(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cvMain.setOnClickListener {
                callback.onCategoryClicked(categoryList[adapterPosition])
            }

            binding.ivDelete.setOnClickListener {
                callback.onCategoryDeleteClicked(categoryList[adapterPosition])
            }
        }

        fun bind() {
            val cat = categoryList[adapterPosition]
            binding.tvCategoryName.text = cat.categoryName
            /* if (cat.id == -1L) {
                 binding.ivMove.isClickable = false
                 binding.ivDelete.isClickable = false

                 binding.ivMove.alpha = 0.6f
                 binding.ivDelete.alpha = 0.6f
             } else {
                 binding.ivMove.isClickable = true
                 binding.ivDelete.isClickable = true
             }*/

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

    interface CategoryItemClickCallback {
        fun onCategoryClicked(category: Category)
        fun onCategoryDeleteClicked(category: Category)
    }
}