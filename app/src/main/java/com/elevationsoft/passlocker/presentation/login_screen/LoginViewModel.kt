package com.elevationsoft.passlocker.presentation.login_screen

import androidx.lifecycle.ViewModel
import com.elevationsoft.passlocker.utils.PrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val prefUtils: PrefUtils) : ViewModel() {

    fun isUserRegistered(): Boolean {
        return prefUtils.isUserRegistered()
    }

    fun getRegisteredUseName(): String {
        return prefUtils.getUserName()
    }

    fun registerUser(userName: String) {
        prefUtils.registerUser(userName)
    }

    fun login() {
        prefUtils.updateLogin(System.currentTimeMillis())
    }

    fun logout() {
        prefUtils.logout()
    }

    fun validateName(nameStr: String?): Boolean {
        if (nameStr == null) {
            return false
        } else if (nameStr.isEmpty()) {
            return false
        } else if (nameStr.trim().length < 3) {
            return false
        }
        return true
    }

    fun getValidName(nameStr: String): String {
        return nameStr.trim()
    }
}