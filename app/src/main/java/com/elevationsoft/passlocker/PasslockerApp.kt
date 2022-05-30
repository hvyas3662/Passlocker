package com.elevationsoft.passlocker

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.elevationsoft.passlocker.presentation.login_screen.LoginActivity
import com.elevationsoft.passlocker.utils.PrefUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class PasslockerApp : Application() {
    @Inject
    lateinit var prefUtils: PrefUtils
    private var isNotLoginActivity: Boolean = false
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable)
        }

        activeSessionChecking()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                isNotLoginActivity = activity !is LoginActivity
                if (isNotLoginActivity) {
                    if (prefUtils.isUserRegistered() && prefUtils.isLoggedIn()) {
                        prefUtils.updateLogin(System.currentTimeMillis())
                        Timber.tag(LOGIN_CHECK_TAG).d("Session extended")
                    }
                }
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })

    }

    private fun activeSessionChecking() {
        appScope.launch {
            while (true) {
                delay(SESSION_RECHECK_TIME)
                if (prefUtils.isUserRegistered() && prefUtils.isLoggedIn()) {
                    Timber.tag(LOGIN_CHECK_TAG).d(Thread.currentThread().name)
                    checkUpdateSession(isNotLoginActivity)
                }
            }
        }
    }

    private fun checkUpdateSession(isNotLoginActivity: Boolean, isAutoCheck: Boolean = true) {
        if (isNotLoginActivity) {
            if (isAutoCheck) {
                Timber.tag(LOGIN_CHECK_TAG).d("Login Auto Check Called")
            } else {
                Timber.tag(LOGIN_CHECK_TAG).d("Login Check Called")
            }
            val isSessionExpired =
                (System.currentTimeMillis() - prefUtils.getTimeStamp()) >= SESSION_TTL
            if (prefUtils.getTimeStamp() <= 0) {
                Timber.tag(LOGIN_CHECK_TAG).d("Already Logout")
                gotoLoginScreen()
            } else if (isSessionExpired) {
                Timber.tag(LOGIN_CHECK_TAG).d("Session expired: Logging out")
                prefUtils.logout()
                gotoLoginScreen()
            } else {
                Timber.tag(LOGIN_CHECK_TAG).d("Session Not expired: LoggedIn")
            }
        }
    }

    private fun gotoLoginScreen() {
        val intent = Intent(applicationContext, LoginActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    companion object {
        const val SESSION_TTL = ((1000L * 60) * 3)
        const val SESSION_RECHECK_TIME = (1000L * 10)
        const val LOGIN_CHECK_TAG = "LOGIN_CHECK"
    }
}