package com.elevationsoft.passlocker.presentation.home_screen.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elevationsoft.passlocker.databinding.FragmentCategoryBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.presentation.home_screen.HomeScreenState
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val categoryVm by viewModels<CategoryFragmentViewModel>()
    private var categoryListAdapter: CategoryListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)

        binding.rvCategory.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        binding.rvCategory.layoutManager = llm


        categoryVm.categoryFragState.removeObservers(viewLifecycleOwner)
        categoryVm.categoryFragState.observe(viewLifecycleOwner) {
            updateUi(it)
        }

        categoryVm.getCategoryList()


        return binding.root
    }

    private fun updateUi(state: CategoryFragmentState) {
        if (state.isLoading) {
            binding.layoutLoadingView.root.show()
            binding.layoutEmptyView.root.hide()
            binding.rvCategory.hide()
        } else {
            binding.layoutLoadingView.root.hide()
        }

        if (state.hasError.asString(requireContext()).isNotEmpty()) {
            binding.layoutEmptyView.root.show()
            binding.layoutEmptyView.tvError.text =
                state.hasError.asString(requireContext())
            binding.rvCategory.hide()
        } else if (state.categoryList.isNotEmpty()) {
            binding.layoutEmptyView.root.hide()
            binding.rvCategory.show()
            if (categoryListAdapter != null) {
                categoryListAdapter!!.updateCategoryList(state.categoryList)
            } else {
                categoryListAdapter = CategoryListAdapter(
                    state.categoryList,
                    object : CategoryListAdapter.CategoryItemClickCallback {
                        override fun onCategoryClicked(category: Category) {
                            context?.toast(category.toString())
                        }

                        override fun onCategoryDeleteClicked(category: Category) {
                            context?.toast("delete clicked ${category.categoryName}")
                        }

                    })
                binding.rvCategory.adapter = categoryListAdapter
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = CategoryListFragment()
    }

}