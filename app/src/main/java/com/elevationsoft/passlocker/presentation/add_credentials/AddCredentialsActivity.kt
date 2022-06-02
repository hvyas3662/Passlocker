package com.elevationsoft.passlocker.presentation.add_credentials

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityAddCredentialsBinding
import com.elevationsoft.passlocker.domain.models.Category
import com.elevationsoft.passlocker.domain.models.Credential
import com.elevationsoft.passlocker.utils.ButtonList
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.CustomLoader
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCredentialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCredentialsBinding
    private val vm by viewModels<AddCredentialViewModel>()
    private var mode = MODE_ADD
    private var credential: Credential? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCredentialsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent !== null && intent.hasExtra(KEY_CREDENTIAL_OBJ)) {
            val obj = intent.getParcelableExtra<Credential>(KEY_CREDENTIAL_OBJ)
            if (obj != null) {
                credential = obj
                mode = MODE_EDIT
            }
        }

        initToolBar()

        if (mode == MODE_EDIT) {
            //todo update text field value
        }

        vm.screenState.observe(this) {
            if (it.isCategoryLoaded) {
                if (it.isLoading) {
                    CustomLoader.getInstance().showLoader(this)
                } else {
                    CustomLoader.getInstance().hideLoader(this)
                }

                if (it.hasError.asString(this@AddCredentialsActivity).isNotEmpty()) {
                    toast(it.hasError.asString(this@AddCredentialsActivity), Toast.LENGTH_LONG)
                } else if (it.isCredentialAdded) {
                    CustomLoader.getInstance().hideLoader(this)
                    setResult(Activity.RESULT_OK, Intent())
                    if (mode == MODE_EDIT) {
                        toast(getString(R.string.text_credential_added), Toast.LENGTH_LONG)
                    } else {
                        toast(getString(R.string.text_credential_updated), Toast.LENGTH_LONG)
                    }
                    finish()
                }
            } else {
                if (it.isLoading) {
                    CustomLoader.getInstance().showLoader(this)
                }

                if (it.hasError.asString(this@AddCredentialsActivity).isNotEmpty()) {
                    toast(it.hasError.asString(this@AddCredentialsActivity), Toast.LENGTH_LONG)
                } else if (it.categoryList.isNotEmpty()) {
                    CustomLoader.getInstance().hideLoader(this)
                    setUpCategorySelector(it.categoryList)
                }

            }
        }
        vm.getCategoryList()
        binding.btnSubmit.setOnClickListener {

        }

    }

    private fun setUpCategorySelector(categoryList: List<Category>) {
        val catList = ArrayList<String>()
        categoryList.forEach { cat ->
            catList.add(cat.categoryName)
        }
        val btnList = ButtonList.with(this@AddCredentialsActivity)
            .setLayoutManager(ButtonList.FLAX_LAYOUT_MANAGER)
            .setOrientation(ButtonList.ORIENTATION_VERTICAL)
            .setRecyclerViewHeight(ButtonList.RECYCLER_VIEW_HEIGHT_WRAP_CONTENT)
            .setGapInItem(8)
            .setDataList(catList)
            .setSelectedIndex(0)
            .setSelectListener(object : ButtonList.ItemListener {
                override fun onItemClick(item: String, index: Int) {
                    toast("$item -> $index")
                }
            })
        binding.llCategoryHolder.removeAllViews()
        binding.llCategoryHolder.addView(btnList.createView())
        vm.setCategoryLoaded()
    }

    private fun initToolBar() {
        if (mode == MODE_EDIT) {
            binding.tvTitle.text = getString(R.string.text_update_credentials)
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
        const val KEY_CREDENTIAL_OBJ = "credential_obj"
        private const val MODE_ADD = "ADD"
        private const val MODE_EDIT = "EDIT"
    }
}