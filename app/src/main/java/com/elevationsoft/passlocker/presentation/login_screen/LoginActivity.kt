package com.elevationsoft.passlocker.presentation.login_screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON
import androidx.core.content.ContextCompat
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityLoginBinding
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginVm: LoginViewModel by viewModels()

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBio()

        //todo check saved user if yes the show user add ui


        binding.ivBio.setOnClickListener {
            binding.etEnterName.clearFocus()
            if (loginVm.validateName(binding.etEnterName.text.toString())) {
                binding.etEnterName.hide()
                val displayText =
                    "Welcome ${loginVm.getValidName(binding.etEnterName.text.toString())}"
                binding.tvWelcome.text = displayText
                binding.llLoginInfo.show()
                binding.ivBio.hide()
                binding.tvRegister.hide()

                biometricPrompt.authenticate(promptInfo)

            } else {
                toast(getString(R.string.text_name_error))
                binding.etEnterName.requestFocus()
            }
        }

    }


    private fun setUpBio() {
        val executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == ERROR_NEGATIVE_BUTTON) {
                        finish()
                    } else {
                        toast("Authentication error: $errString ($errorCode)", Toast.LENGTH_SHORT)
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    toast("Goto next screen")
                }

            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.text_verify_identity))
            .setNegativeButtonText(getString(R.string.text_exit_app))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    }
}