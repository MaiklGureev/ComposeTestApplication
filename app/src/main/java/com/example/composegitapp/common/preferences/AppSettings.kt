package com.example.composegitapp.common.preferences

import android.content.SharedPreferences
import com.gureev.ComposeAppGitHub.BuildConfig
import javax.inject.Inject


class AppSettings @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : IAppSettings {

    override val accessToken: String = TOKEN

    override fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    override fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    override fun removeToken() {
        val editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }

    override fun getCurrentApiUrl(): String {
        return BuildConfig.SERVER_URL
    }

    companion object {
        private const val TOKEN =
            "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"

        private const val TOKEN_KEY = "TOKEN_KEY"
    }
}

interface IAppSettings {
    val accessToken: String
    fun getCurrentApiUrl(): String

    fun saveToken(token: String)
    fun getToken(): String?
    fun removeToken()
}
