package com.elevationsoft.passlocker.presentation.home_screen.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.elevationsoft.passlocker.databinding.FragmentCategoryBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.presentation.home_screen.HomeScreenStatus
import com.elevationsoft.passlocker.presentation.home_screen.HomeScreenViewModel
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val homeVm by viewModels<HomeScreenViewModel>()
    private var stateObserver: Observer<HomeScreenStatus>? = null
    private var categoryListAdapter: CategoryListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)

        binding.rvCategory.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        binding.rvCategory.layoutManager = llm

        homeVm.getCategoryList()

        stateObserver = Observer<HomeScreenStatus> {
            updateUi(it)
        }
        homeVm.screenState.observe(requireActivity(), stateObserver!!)

        return binding.root
    }

    private fun updateUi(state: HomeScreenStatus) {
        if (state.categoryListStatus.loading) {
            binding.layoutLoadingView.root.show()
            binding.layoutEmptyView.root.hide()
            binding.rvCategory.hide()
        } else {
            binding.layoutLoadingView.root.hide()
        }

        if (state.categoryListStatus.error.asString(requireContext()).isNotEmpty()) {
            binding.layoutEmptyView.root.show()
            binding.layoutEmptyView.tvError.text =
                state.categoryListStatus.error.asString(requireContext())
            binding.rvCategory.hide()
        } else if (state.categoryListStatus.categoryList.isNotEmpty()) {
            binding.layoutEmptyView.root.hide()
            binding.rvCategory.show()
            if (categoryListAdapter != null) {
                categoryListAdapter!!.updateCategoryList(state.categoryListStatus.categoryList)
            } else {
                categoryListAdapter = CategoryListAdapter(
                    state.categoryListStatus.categoryList,
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

    override fun onDestroyView() {
        super.onDestroyView()
        stateObserver?.let {
            homeVm.screenState.removeObserver(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CategoryListFragment()
    }
}