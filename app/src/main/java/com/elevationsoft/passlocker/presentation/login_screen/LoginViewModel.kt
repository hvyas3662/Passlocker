package com.elevationsoft.passlocker.presentation.login_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {


    fun validateName(nameStr: String?): Boolean {
        if (nameStr == null) {
            return false
        } else if (nameStr.isEmpty()) {
            return false
        } else if (nameStr.trim().length < 4) {
            return false
        }
        return true
    }

    fun getValidName(nameStr: String): String {
        return nameStr.trim()
    }
}