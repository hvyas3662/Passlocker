package com.elevationsoft.passlocker.presentation.home_screen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.databinding.ActivityMainBinding
import com.elevationsoft.passlocker.presentation.add_category.AddCategoryActivity
import com.elevationsoft.passlocker.presentation.add_credentials.AddCredentialsActivity
import com.elevationsoft.passlocker.utils.ButtonList
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedTabIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedTabIndex = binding.ctvBottomTabBar.getSelectedTabIndex()
        binding.ctvBottomTabBar.setOnTabChangedCallBack { tabIndex ->
            Timber.d("selected tab: $tabIndex")
            selectedTabIndex = tabIndex
        }

        binding.ivAdd.setOnClickListener {
            if (selectedTabIndex == 0) {
                startActivity(Intent(this@MainActivity, AddCredentialsActivity::class.java))
            } else {
                startActivity(Intent(this@MainActivity, AddCategoryActivity::class.java))
            }
        }
        val dataList = ArrayList<String>()
        dataList.add("Cat 1")
        dataList.add("Category 2")
        dataList.add("Category 3")
        dataList.add("Category 4")
        dataList.add("Category 5")
        dataList.add("Category 5")
        dataList.add("Category 7")
        dataList.add("Category 8")
        val btnList = ButtonList.with(this)
            .setLayoutManager(ButtonList.LINEAR_LAYOUT_MANAGER)
            .setOrientation(ButtonList.ORIENTATION_HORIZONTAL)
            .setRecyclerViewHeight(48)
            .setDataList(dataList)
            .setSelectListener(object : ButtonList.ItemListener {
                override fun onItemClick(item: String, index: Int) {
                    toast("$item ($index)")
                }
            })
        binding.llCatTabs.removeAllViews()
        binding.llCatTabs.addView(btnList.createView())
    }
}