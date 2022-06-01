package com.elevationsoft.passlocker.presentation.add_category

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityAddCategoryBinding
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.CustomLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    private val vm by viewModels<AddCategoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()

        vm.screenState.observe(this) {
            if (it.isLoading) {
                CustomLoader.getInstance().showLoader(this)
            } else {
                CustomLoader.getInstance().hideLoader(this)
            }

            if (it.error.asString(this@AddUpdateCategoryActivity).isNotEmpty()) {
                toast(it.error.asString(this@AddUpdateCategoryActivity), Toast.LENGTH_LONG)
            } else if (it.isCategoryAdded) {
                //set result and go back
                finish()
            }
        }

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
                vm.insertUpdateCategory(0L, validCatName, validCatPos)
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