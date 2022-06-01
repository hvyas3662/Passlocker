package com.elevationsoft.passlocker.presentation.home_screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.databinding.ActivityHomeBinding
import com.elevationsoft.passlocker.presentation.home_screen.HomeScreenViewModel.Companion.CATEGORY_TAB_INDEX
import com.elevationsoft.passlocker.presentation.home_screen.HomeScreenViewModel.Companion.PASSLIST_TAB_INDEX
import com.elevationsoft.passlocker.presentation.home_screen.category.CategoryListFragment
import com.elevationsoft.passlocker.presentation.home_screen.passlist.PassListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeVm by viewModels<HomeScreenViewModel>()
    private var selectedScreen = PASSLIST_TAB_INDEX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeVm.updateSelectedScreen(binding.ctvBottomTabBar.getSelectedTabIndex())

        binding.ctvBottomTabBar.setOnTabChangedCallBack { tabIndex ->
            homeVm.updateSelectedScreen(tabIndex)
        }

        homeVm.homeScreenState.observe(this) {
            updateUi(it)
        }

        binding.ivAdd.setOnClickListener {
            addClickedCallBack?.onAddClicked()
        }

    }

    private fun updateUi(status: HomeScreenState) {
        selectedScreen = status.selectedScreen
        updateSelectedScreen(selectedScreen)
    }

    private fun updateSelectedScreen(selectedScreen: Int) {
        if (selectedScreen == CATEGORY_TAB_INDEX) {
            setCategoryListFragment()
        } else {
            setPassListFragment()
        }
    }

    private fun setPassListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.clFragContainer.id, PassListFragment.newInstance())
            .commit()
    }

    private fun setCategoryListFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.clFragContainer.id, CategoryListFragment.newInstance())
            .commit()
    }


    companion object {
        var addClickedCallBack: OnAddClickedCallBack? = null
    }


    override fun onDestroy() {
        super.onDestroy()
        addClickedCallBack = null
    }

    interface OnAddClickedCallBack {
        fun onAddClicked()
    }

}