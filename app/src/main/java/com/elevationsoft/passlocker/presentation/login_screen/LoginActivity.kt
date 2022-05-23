package com.elevationsoft.passlocker.presentation.login_screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
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
        setUpBio()
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
                    toast("Authentication error: $errString $errorCode", Toast.LENGTH_SHORT)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    toast("Goto next screen")
                }

            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Verify Your Identity")
            .setNegativeButtonText("Exit")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    }
}