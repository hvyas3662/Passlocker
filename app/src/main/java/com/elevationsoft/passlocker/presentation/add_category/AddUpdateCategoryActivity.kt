package com.elevationsoft.passlocker.presentation.add_category

import android.app.Activity
import android.content.Intent
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
    private var nextPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()

        vm.screenState.observe(this) {
            nextPos = it.nextCategoryPosition

            if (it.isLoading) {
                CustomLoader.getInstance().showLoader(this)
            } else {
                CustomLoader.getInstance().hideLoader(this)
            }

            if (it.hasError.asString(this@AddUpdateCategoryActivity).isNotEmpty()) {
                toast(it.hasError.asString(this@AddUpdateCategoryActivity), Toast.LENGTH_LONG)
            } else if (it.isCategoryAdded) {
                CustomLoader.getInstance().hideLoader(this)
                setResult(Activity.RESULT_OK, Intent())
                toast(getString(R.string.text_category_added), Toast.LENGTH_LONG)
                finish()
            }
        }

        vm.getLastCategoryPosition()

        binding.btnSubmit.setOnClickListener {
            val catName = binding.etCatName.text.toString()
            if (!vm.validateCategoryName(catName)) {
                toast(getString(R.string.text_category_error))
            } else {
                val validCatName = vm.getValidCategoryName(catName)
                vm.insertUpdateCategory(0L, validCatName, nextPos)
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