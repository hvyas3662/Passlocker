package com.elevationsoft.passlocker.presentation.add_category

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityAddCategoryBinding

class AddCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()
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