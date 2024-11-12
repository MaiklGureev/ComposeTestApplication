package com.example.composegitapp.di

import android.content.Context
import com.example.composegitapp.common.utils.downloads.DownloadService
import com.example.composegitapp.di.feature.RepoDownloadsModule
import com.example.composegitapp.di.feature.SearchReposModule
import com.example.composegitapp.di.feature.SearchUsersModule
import com.example.composegitapp.di.feature.UserRepoBranchesModule
import com.example.composegitapp.di.feature.UserReposModule
import com.example.composegitapp.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        BaseViewModelFactoryModule::class,
        NetworkModule::class,
        AppModule::class,

        SearchReposModule::class,
        SearchUsersModule::class,
        UserReposModule::class,
        UserRepoBranchesModule::class,
        RepoDownloadsModule::class,
    ],
    dependencies = [AppDeps::class]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(service: DownloadService)

    @Component.Builder
    interface Builder {

        fun withAppDeps(deps: AppDeps): Builder

        fun build(): ApplicationComponent

    }

}

interface AppDeps {
    val context: Context
}