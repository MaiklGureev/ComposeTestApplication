package com.example.composegitapp.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.composegitapp.common.preferences.AppSettings
import com.example.composegitapp.common.preferences.IAppSettings
import com.example.composegitapp.common.utils.BrowserUtil
import com.example.composegitapp.common.utils.downloads.DownloadManager
import com.example.composegitapp.common.utils.downloads.IDownloadManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [AppModuleBind::class])
class AppModule {
    @Provides
    fun provideBrowserUtil(context: Context): BrowserUtil {
        return BrowserUtil(context)
    }

    @Provides
    fun provideContentResolver(context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            /* fileName = */ "secure_prefs",
            /* masterKeyAlias = */
            masterKeyAlias,
            /* context = */
            context,
            /* prefKeyEncryptionScheme = */
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            /* prefValueEncryptionScheme = */
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @DownloadDir
    fun provideSaveDir(): File {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    }

    @Provides
    @CacheDir
    fun provideCacheDir(context: Context): File {
        return context.cacheDir
    }
}

@Module
interface AppModuleBind {
    @Binds
    fun bindAppSettings(bindImpl: AppSettings): IAppSettings

    @Singleton
    @Binds
    fun bindDownloadManager(bindImpl: DownloadManager): IDownloadManager
}

@Qualifier
annotation class DownloadDir

@Qualifier
annotation class CacheDir

