package com.elevationsoft.passlocker.presentation.login_screen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.core.content.ContextCompat
import com.elevationsoft.passlocker.R
import com.elevationsoft.passlocker.databinding.ActivityLoginBinding
import com.elevationsoft.passlocker.presentation.home_screen.MainActivity
import com.elevationsoft.passlocker.utils.ContextUtils.toast
import com.elevationsoft.passlocker.utils.ViewUtils.hide
import com.elevationsoft.passlocker.utils.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginVm: LoginViewModel by viewModels()
    private var validatedUserName = ""
    private var isUserRegistered = false

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginVm.logout()
        setUpBio()

        //if user is register then don't ask for name
        isUserRegistered = loginVm.isUserRegistered()
        if (isUserRegistered) {
            val userName = loginVm.getRegisteredUseName()
            binding.etEnterName.clearFocus()
            binding.etEnterName.hide()
            val displayText =
                "Welcome $userName"
            binding.tvWelcome.text = displayText
            binding.llLoginInfo.show()
            binding.ivBio.show()
            binding.tvRegister.hide()
            binding.ivBio.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
        } else {
            binding.etEnterName.requestFocus()
            binding.etEnterName.show()
            binding.llLoginInfo.hide()
            binding.ivBio.show()
            binding.tvRegister.show()
            binding.ivBio.setOnClickListener {
                binding.etEnterName.clearFocus()
                if (loginVm.validateName(binding.etEnterName.text.toString())) {
                    val userName = loginVm.getValidName(binding.etEnterName.text.toString())
                    validatedUserName = userName
                    biometricPrompt.authenticate(promptInfo)
                } else {
                    toast(getString(R.string.text_name_error))
                    binding.etEnterName.requestFocus()
                }
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
                    when (errorCode) {
                        ERROR_NEGATIVE_BUTTON -> {
                            finish()
                        }
                        ERROR_NO_BIOMETRICS -> {
                            toast(getString(R.string.error_no_finger_print))
                            finish()
                        }
                        ERROR_USER_CANCELED -> {
                            if (isUserRegistered) {
                                finish()
                            }
                        }
                        else -> {
                            toast(errString.toString())
                        }
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    if (!isUserRegistered) {
                        loginVm.registerUser(validatedUserName)
                    }
                    loginVm.login()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)

                }

            })

        promptInfo = PromptInfo.Builder()
            .setTitle(getString(R.string.text_verify_identity))
            .setNegativeButtonText(getString(R.string.text_exit_app))
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
    }


    override fun onResume() {
        super.onResume()
        if (isUserRegistered) {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}