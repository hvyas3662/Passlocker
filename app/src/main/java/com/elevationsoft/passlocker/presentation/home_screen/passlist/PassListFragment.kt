package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.FragmentPassListBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.presentation.add_credentials.AddCredentialsActivity
import com.elevationsoft.passlocker.presentation.home_screen.HomeActivity
import com.elevationsoft.passlocker.utils.ButtonList
import com.elevationsoft.passlocker.utils.FragmentUtils.startActivityForResult
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassListFragment : Fragment(), HomeActivity.OnAddClickedCallBack {
    private val passListVm by viewModels<PasslistFragmentViewModel>()
    private lateinit var binding: FragmentPassListBinding
    private lateinit var openAddCredentialActivity: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HomeActivity.addClickedCallBack = this
        openAddCredentialActivity = startActivityForResult {
            it?.let { result ->
                if (result.resultCode == RESULT_OK) {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPassListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passListVm.passlistFragState.removeObservers(viewLifecycleOwner)
        passListVm.passlistFragState.observe(viewLifecycleOwner) {
            updateUi(it)
        }

        passListVm.getCategoryList()
    }

    override fun onAddClicked() {
        val intent = Intent(requireActivity(), AddCredentialsActivity::class.java)
        openAddCredentialActivity.launch(intent)
    }

    private fun updateUi(state: PassListFragmentState) {
        if (state.isCategoryLoaded) {
            //replace loader with
            binding.layoutLoadingView.root.hide()
            binding.llSearch.hide()
            binding.layoutEmptyView.root.show()
            binding.layoutEmptyView.tvError.text = getString(R.string.text_no_data)
            binding.rvPasslist.hide()
        } else {
            binding.rvPasslist.hide()
            if (state.isLoading) {
                binding.layoutLoadingView.root.show()
                binding.layoutEmptyView.root.hide()
            }

            if (state.hasError.asString(requireContext()).isNotEmpty()) {
                binding.llSearch.hide()
                binding.layoutEmptyView.root.show()
                binding.layoutEmptyView.tvError.text =
                    state.hasError.asString(requireContext())
            } else if (state.categoryList.isNotEmpty()) {
                binding.layoutEmptyView.root.hide()
                setUpCategoryTabs(state.categoryList)
                passListVm.setCategoryLoaded()
            }

        }

    }

    private fun setUpCategoryTabs(categoryList: List<Category>) {
        var selectedCategoryIndex =
            categoryList.indexOfFirst { it.id == passListVm.getSavedSelectedCategoryId() }
        if (selectedCategoryIndex < 0) {
            selectedCategoryIndex = 0
            passListVm.saveSelectedCategoryId(categoryList[selectedCategoryIndex].id)
        }
        val catList = ArrayList<String>()
        categoryList.forEach {
            catList.add(it.categoryName)
        }
        val btnList = ButtonList.with(requireContext())
            .setLayoutManager(ButtonList.LINEAR_LAYOUT_MANAGER)
            .setOrientation(ButtonList.ORIENTATION_HORIZONTAL)
            .setRecyclerViewHeight(48)
            .setDataList(catList)
            .setSelectedIndex(selectedCategoryIndex)
            .setSelectListener(object : ButtonList.ItemListener {
                override fun onItemClick(item: String, index: Int) {
                    passListVm.saveSelectedCategoryId(categoryList[index].id)
                }
            })
        binding.llCatTabs.removeAllViews()
        binding.llCatTabs.addView(btnList.createView())

    }

    companion object {
        @JvmStatic
        fun newInstance() = PassListFragment()
    }


}