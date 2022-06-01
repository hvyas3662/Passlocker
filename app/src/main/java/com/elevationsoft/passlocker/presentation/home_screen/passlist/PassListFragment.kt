package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.FragmentPassListBinding
import com.elevationsoft.passlocker.utils.ButtonList
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassListFragment : Fragment() {
    private val passListVm by viewModels<PasslistFragmentViewModel>()
    private lateinit var binding: FragmentPassListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPassListBinding.inflate(layoutInflater, container, false)

        passListVm.passlistFragState.removeObservers(viewLifecycleOwner)
        passListVm.passlistFragState.observe(viewLifecycleOwner) {
            updateUi(it)
        }

        passListVm.getCategoryList()

        return binding.root
    }

    private fun updateUi(state: PassListFragmentState) {
        if (state.isCategoryLoaded) {
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

                val catList = ArrayList<String>()
                state.categoryList.forEach {
                    catList.add(it.categoryName)
                }
                val btnList = ButtonList.with(requireContext())
                    .setLayoutManager(ButtonList.LINEAR_LAYOUT_MANAGER)
                    .setOrientation(ButtonList.ORIENTATION_HORIZONTAL)
                    .setRecyclerViewHeight(48)
                    .setDataList(catList)
                    .setSelectedIndex(0)
                    .setSelectListener(object : ButtonList.ItemListener {
                        override fun onItemClick(item: String, index: Int) {
                            requireContext().toast("$item ($index)")
                        }
                    })
                binding.llCatTabs.removeAllViews()
                binding.llCatTabs.addView(btnList.createView())


                binding.layoutLoadingView.root.hide()
                binding.llSearch.hide()
                binding.layoutEmptyView.root.show()
                binding.layoutEmptyView.tvError.text = getString(R.string.text_no_data)
                binding.rvPasslist.hide()
            }

        }

    }


    companion object {
        @JvmStatic
        fun newInstance() = PassListFragment()
    }
}