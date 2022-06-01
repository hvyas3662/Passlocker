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
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.CustomLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddUpdateCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    private val vm by viewModels<AddCategoryViewModel>()
    private var nextPos: Int = 0
    private var mode = MODE_ADD
    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent !== null && intent.hasExtra(KEY_CATEGORY_OBJ)) {
            val obj = intent.getParcelableExtra<Category>(KEY_CATEGORY_OBJ)
            if (obj != null) {
                category = obj
                mode = MODE_EDIT
            }
        }

        initToolBar()
        if (mode == MODE_EDIT) {
            binding.etCatName.setText(category!!.categoryName)
        }
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
                if (mode == MODE_EDIT) {
                    toast(getString(R.string.text_category_updated), Toast.LENGTH_LONG)
                } else {
                    toast(getString(R.string.text_category_added), Toast.LENGTH_LONG)
                }
                finish()
            }
        }

        if (mode == MODE_ADD) {
            vm.getLastCategoryPosition()
        }

        binding.btnSubmit.setOnClickListener {
            val catName = binding.etCatName.text.toString()
            if (!vm.validateCategoryName(catName)) {
                toast(getString(R.string.text_category_error))
            } else {
                val validCatName = vm.getValidCategoryName(catName)
                var catId = 0L
                if (mode == MODE_EDIT) {
                    catId = category!!.id
                    nextPos = category!!.position
                }
                vm.insertUpdateCategory(catId, validCatName, nextPos)
            }
        }

    }

    private fun initToolBar() {
        if (mode == MODE_EDIT) {
            binding.tvTitle.text = getString(R.string.text_update_category)
        }
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

    companion object {
        const val KEY_CATEGORY_OBJ = "category_obj"
        private const val MODE_ADD = "ADD"
        private const val MODE_EDIT = "EDIT"
    }
}