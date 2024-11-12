package com.example.composegitapp.di

import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.composegitapp.common.preferences.AppSettings
import com.example.composegitapp.common.preferences.IAppSettings
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
    fun provideContentResolver(context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun provideNotificationManager(context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        try {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            return EncryptedSharedPreferences.create(
                "secure_prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)
        }
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

