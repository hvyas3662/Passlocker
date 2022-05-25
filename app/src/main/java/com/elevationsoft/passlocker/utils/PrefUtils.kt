package com.elevationsoft.passlocker.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

@Suppress("unused")
class PrefUtils constructor(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

    //for registration
    fun registerUser(userName: String) {
        val editor = pref.edit()
        editor.putBoolean(KEY_IS_REGISTERED, true)
        editor.putString(KEY_USER_NAME, userName)
        editor.apply()
    }

    fun isUserRegistered(): Boolean {
        return pref.getBoolean(KEY_IS_REGISTERED, false)
    }

    fun getUserName(): String {
        return pref.getString(KEY_USER_NAME, "")!!
    }


    //For Login
    fun updateLogin(timeStamp: Long) {
        val editor = pref.edit()
        editor.putBoolean(KEY_IS_LOGGEDIN, true)
        editor.putLong(KEY_LAST_LOGGEDIN_TIMESTAMP, timeStamp)
        editor.apply()
    }

    fun logout() {
        val editor = pref.edit()
        editor.putBoolean(KEY_IS_LOGGEDIN, false)
        editor.putLong(KEY_LAST_LOGGEDIN_TIMESTAMP, -1L)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false)
    }

    fun getTimeStamp(): Long {
        return pref.getLong(KEY_LAST_LOGGEDIN_TIMESTAMP, -1L)
    }


    companion object {
        private const val PREF_NAME = "passlockerPref"
        private const val KEY_IS_REGISTERED = "isRegistered"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_IS_LOGGEDIN = "isLoggedIn"
        private const val KEY_LAST_LOGGEDIN_TIMESTAMP = "timestamp"
    }

}