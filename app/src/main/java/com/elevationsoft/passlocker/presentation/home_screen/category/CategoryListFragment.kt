package com.elevationsoft.passlocker.presentation.home_screen.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elevationsoft.easydialog.EasyDialog
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.FragmentCategoryBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.presentation.add_category.AddUpdateCategoryActivity
import com.elevationsoft.passlocker.presentation.home_screen.HomeActivity
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.CustomLoader
import com.elevationsoft.passlocker.utils.FragmentUtils.startActivityForResult
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoryListFragment : Fragment(), HomeActivity.OnAddClickedCallBack {
    private lateinit var binding: FragmentCategoryBinding
    private val categoryVm by viewModels<CategoryFragmentViewModel>()
    private var categoryListAdapter: CategoryListAdapter? = null
    private lateinit var openAddUpdateCategoryActivity: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HomeActivity.addClickedCallBack = this
        openAddUpdateCategoryActivity = startActivityForResult {
            it?.let { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    categoryVm.getCategoryList()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCategory.setHasFixedSize(true)
        val llm = LinearLayoutManager(requireContext())
        binding.rvCategory.layoutManager = llm

        categoryVm.categoryFragState.removeObservers(viewLifecycleOwner)
        categoryVm.categoryFragState.observe(viewLifecycleOwner) {
            updateUi(it)
        }

        categoryVm.getCategoryList()
    }

    override fun onAddClicked() {
        val intent = Intent(requireActivity(), AddUpdateCategoryActivity::class.java)
        openAddUpdateCategoryActivity.launch(intent)
    }

    private fun updateUi(state: CategoryFragmentState) {
        if (state.isLoading) {
            if (state.loadingType == CategoryFragmentState.LOADING_TYPE_IN_UI) {
                binding.rvCategory.hide()
                binding.layoutEmptyView.root.hide()
                binding.layoutLoadingView.root.show()
            } else {
                binding.rvCategory.hide()
                binding.layoutEmptyView.root.hide()
                binding.layoutLoadingView.root.hide()
                CustomLoader.getInstance().showLoader(requireActivity())
            }
        } else {
            if (state.loadingType == CategoryFragmentState.LOADING_TYPE_IN_UI) {
                binding.layoutLoadingView.root.hide()
            } else {
                CustomLoader.getInstance().hideLoader(requireActivity())
            }
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
                            EasyDialog.with(requireContext())
                                .setCancelable(false)
                                .setTitle(
                                    true,
                                    getString(
                                        R.string.text_category_delete_title,
                                        category.categoryName
                                    )
                                )
                                .setMessage(
                                    true,
                                    getString(
                                        R.string.text_category_delete_message,
                                        category.categoryName
                                    )
                                )
                                .setPrimaryButton(true, getString(R.string.text_yes))
                                .setSecondaryButton(true, getString(R.string.text_no))
                                .setOnButtonClickListener(object :
                                    EasyDialog.OnButtonClickListener {
                                    override fun onPrimaryButtonClick() {
                                        categoryVm.deleteCategory(category.id)
                                    }

                                    override fun onSecondaryButtonClick() {

                                    }

                                }).show()
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