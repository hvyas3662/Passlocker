package com.elevationsoft.passlocker.presentation.home_screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.databinding.ActivityHomeBinding
import com.elevationsoft.passlocker.presentation.add_category.AddCategoryActivity
import com.elevationsoft.passlocker.presentation.add_credentials.AddCredentialsActivity
import com.elevationsoft.passlocker.presentation.home_screen.category.CategoryListFragment
import com.elevationsoft.passlocker.presentation.home_screen.passlist.PassListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private val homeVm by viewModels<HomeScreenViewModel>()
    private var selectedScreen = PASSLIST
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeVm.updateSelectedScreen(binding.ctvBottomTabBar.getSelectedTabIndex())

        binding.ctvBottomTabBar.setOnTabChangedCallBack { tabIndex ->
            homeVm.updateSelectedScreen(tabIndex)
        }

        homeVm.screenState.observe(this) {
            updateUi(it)
        }

        binding.ivAdd.setOnClickListener {
            if (selectedScreen == PASSLIST) {
                startActivity(Intent(this@HomeActivity, AddCredentialsActivity::class.java))
            } else {
                startActivity(Intent(this@HomeActivity, AddCategoryActivity::class.java))
            }
        }

    }

    private fun updateUi(status: HomeScreenStatus) {
        selectedScreen = status.selectedScreen
        updateSelectedScreen(selectedScreen)
    }

    private fun updateSelectedScreen(selectedScreen: Int) {
        if (selectedScreen == CATEGORY) {
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
        const val PASSLIST = 0
        const val CATEGORY = 1
    }
}