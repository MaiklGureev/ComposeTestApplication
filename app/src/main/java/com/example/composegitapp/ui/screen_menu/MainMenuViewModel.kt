package com.example.composegitapp.ui.screen_menu

import androidx.lifecycle.ViewModel
import com.example.composegitapp.common.preferences.IAppSettings
import javax.inject.Inject

class MainMenuViewModel @Inject constructor(
    private val appSettings: IAppSettings
) : ViewModel() {
    fun isTokenFilled(): Boolean {
        return appSettings.isTokenFilled()
    }

    fun saveToken(token: String) {
        appSettings.saveToken(token)
    }

    fun resetToken() {
        appSettings.removeToken()
    }
}