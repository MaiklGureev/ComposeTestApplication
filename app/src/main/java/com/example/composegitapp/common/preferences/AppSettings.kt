package com.example.composegitapp.common.preferences

import android.content.Context
import android.content.SharedPreferences
import com.gureev.ComposeAppGitHub.BuildConfig
import com.gureev.ComposeAppGitHub.R
import javax.inject.Inject


class AppSettings @Inject constructor(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : IAppSettings {

    override val accessToken: String = sharedPreferences.getString(TOKEN_KEY, null).orEmpty()

    override fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
    }

    override fun isTokenFilled(): Boolean {
        return sharedPreferences.getString(TOKEN_KEY, null).orEmpty().isNotEmpty()
    }

    override fun removeToken() {
        val editor = sharedPreferences.edit()
        editor.remove(TOKEN_KEY)
        editor.apply()
    }

    override fun getCurrentApiUrl(): String {
        return BuildConfig.SERVER_URL
    }

    override fun getAppName(): String {
        return context.getString(R.string.app_name)
    }

    companion object {
        private const val TOKEN_KEY = "TOKEN_KEY"
    }
}

interface IAppSettings {
    val accessToken: String
    fun getCurrentApiUrl(): String
    fun getAppName(): String

    fun saveToken(token: String)
    fun isTokenFilled(): Boolean
    fun removeToken()
}
