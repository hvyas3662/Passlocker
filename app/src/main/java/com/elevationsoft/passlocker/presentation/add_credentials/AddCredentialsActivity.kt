package com.elevationsoft.passlocker.presentation.add_credentials

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityAddCredentialsBinding

class AddCredentialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCredentialsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCredentialsBinding.inflate(layoutInflater)
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