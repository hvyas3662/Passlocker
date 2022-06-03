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
    private var selectedCategoryId = -1L

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
            binding.etTitle.setText(credential!!.title)
            binding.etUsername.setText(credential!!.userName)
            binding.etPassword.setText(credential!!.password)
            binding.etRemark.setText(credential!!.remark)
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
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(KEY_CREDENTIAL_MODE, mode)
                        putExtra(KEY_CREDENTIAL_OBJ, it.credential)
                    })
                    if (mode == MODE_EDIT) {
                        toast(getString(R.string.text_credential_updated), Toast.LENGTH_LONG)
                    } else {
                        toast(getString(R.string.text_credential_added), Toast.LENGTH_LONG)
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
            val categoryId = selectedCategoryId
            val title = binding.etTitle.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val remark = binding.etRemark.text.toString()
            if (!vm.validateTitle(title)) {
                toast(getString(R.string.text_credential_title_error))
            } else if (!vm.validateUsername(username)) {
                toast(getString(R.string.text_credential_username_error))
            } else {

                var credentialId = 0L
                val validTitle = vm.getValidTitle(title)
                val validUsername = vm.getValidUsername(username)
                val validPassword = vm.getValidPassword(password)
                val validRemark = vm.getValidRemark(remark)
                var isFavourite = false
                if (mode == MODE_EDIT) {
                    credentialId = credential!!.id
                    isFavourite = credential!!.isFavourite
                }
                vm.insertUpdateCredential(
                    credentialId,
                    validTitle,
                    validUsername,
                    validPassword,
                    validRemark,
                    isFavourite,
                    categoryId
                )
            }
        }

    }

    private fun setUpCategorySelector(categoryList: List<Category>) {
        val catList = ArrayList<String>()
        categoryList.forEach { cat ->
            catList.add(cat.categoryName)
        }
        var selectedIndex = 0
        if (mode == MODE_EDIT) {
            selectedIndex = categoryList.indexOfFirst { it.id == credential!!.categoryId }
            if (selectedIndex < 0) {
                selectedIndex = 0
            }
        }
        selectedCategoryId = categoryList[selectedIndex].id
        val btnList = ButtonList.with(this@AddCredentialsActivity)
            .setLayoutManager(ButtonList.FLAX_LAYOUT_MANAGER)
            .setOrientation(ButtonList.ORIENTATION_VERTICAL)
            .setRecyclerViewHeight(ButtonList.RECYCLER_VIEW_HEIGHT_WRAP_CONTENT)
            .setGapInItem(8)
            .setDataList(catList)
            .setSelectedIndex(selectedIndex)
            .setSelectListener(object : ButtonList.ItemListener {
                override fun onItemClick(item: String, index: Int) {
                    selectedCategoryId = categoryList[index].id
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
        const val KEY_CREDENTIAL_MODE = "credential_mode"
        const val MODE_ADD = "ADD"
        private const val MODE_EDIT = "EDIT"
    }
}