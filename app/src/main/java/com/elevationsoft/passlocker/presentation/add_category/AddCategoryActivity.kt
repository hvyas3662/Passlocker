package com.elevationsoft.passlocker.presentation.add_category

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityAddCategoryBinding
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.CustomLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    private val vm by viewModels<AddCategoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()

        binding.btnSubmit.setOnClickListener {

            val catName = binding.etCatName.text.toString()
            val position = binding.etPosition.text.toString()
            if (!vm.validateCategoryName(catName)) {
                toast(getString(R.string.text_category_error))
            } else if (!vm.validateCategoryPosition(position)) {
                toast(getString(R.string.text_position_error))
            } else {
                val validCatName = vm.getValidCategoryName(catName)
                val validCatPos = vm.getValidCategoryPosition(position)
                CustomLoader.getInstance().showLoader(this)
                vm.insertCategory(validCatName, validCatPos)
            }

        }

        vm.isCategoryAdded.observe(this) {
            CustomLoader.getInstance().hideLoader(this)
            if (it) {
                //todo set result
                // finish()
            }
        }

        vm.isCategoryUpdated.observe(this) {
            CustomLoader.getInstance().hideLoader(this)
            if (it) {
                //todo set result
                finish()
            }
        }

    }

    private fun initToolBar() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        setSupportActionBar(binding.toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}