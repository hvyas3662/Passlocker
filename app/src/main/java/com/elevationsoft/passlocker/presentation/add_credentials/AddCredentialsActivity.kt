package com.elevationsoft.passlocker.presentation.add_credentials

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.elevationsoft.easydialog.EasyDialog
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityAddCredentialsBinding
import com.elevationsoft.passlocker.utils.ContextUtils.toast

class AddCredentialsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCredentialsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCredentialsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()

        binding.btnSubmit.setOnClickListener {
            EasyDialog.with(this@AddCredentialsActivity)
                .setCancelable(false)
                .setTitle(true, "Harshal vyas")
                .setMessage(true, "A custom message")
                .setPrimaryButton(true, "Yes 1")
                .setSecondaryButton(true, "no1")
                .setOnDismissListener(object : EasyDialog.OnDismissListener {
                    override fun onDismiss() {
                        toast("dismiss listener")
                    }

                }).setOnButtonClickListener(object : EasyDialog.OnButtonClickListener {
                    override fun onPrimaryButtonClick() {
                        toast("yes")
                    }

                    override fun onSecondaryButtonClick() {
                        toast("no")
                    }

                }).show()
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