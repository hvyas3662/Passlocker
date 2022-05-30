package com.elevationsoft.passlocker.presentation.home_screen.passlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.FragmentPassListBinding
import com.elevationsoft.passlocker.presentation.home_screen.HomeScreenStatus
import com.elevationsoft.passlocker.presentation.home_screen.HomeScreenViewModel
import com.elevationsoft.passlocker.utils.ButtonList
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassListFragment : Fragment() {
    private val homeVm by viewModels<HomeScreenViewModel>()
    private lateinit var binding: FragmentPassListBinding
    private var stateObserver: Observer<HomeScreenStatus>? = null
    private var isCategoryLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPassListBinding.inflate(layoutInflater, container, false)

        stateObserver = Observer<HomeScreenStatus> {
            updateUi(it)
        }
        homeVm.screenState.observe(requireActivity(), stateObserver!!)

        homeVm.getCategoryList()

        return binding.root
    }


    private fun updateUi(state: HomeScreenStatus) {
        if (isCategoryLoaded) {
            binding.layoutLoadingView.root.hide()
            binding.llSearch.hide()
            binding.layoutEmptyView.root.show()
            binding.layoutEmptyView.tvError.text = getString(R.string.text_no_data)
            binding.rvPasslist.hide()
        } else {
            binding.rvPasslist.hide()
            if (state.categoryListStatus.loading) {
                binding.layoutLoadingView.root.show()
                binding.layoutEmptyView.root.hide()
            }


            if (state.passListStatus.error.asString(requireContext()).isNotEmpty()) {
                binding.llSearch.hide()
                binding.layoutEmptyView.root.show()
                binding.layoutEmptyView.tvError.text =
                    state.passListStatus.error.asString(requireContext())
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
                isCategoryLoaded = true

                updateUi(state)

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
        fun newInstance() = PassListFragment()
    }
}