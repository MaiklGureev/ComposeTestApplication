package com.example.composegitapp.di

import android.content.Context
import com.example.composegitapp.di.feature.SearchReposModule
import com.example.composegitapp.di.feature.SearchUserReposModule
import com.example.composegitapp.di.feature.SearchUsersModule
import com.example.composegitapp.ui.MainActivity
import dagger.Component

@Component(
    modules = [
        BaseViewModelFactoryModule::class,
        NetworkModule::class,
        AppModule::class,

        SearchReposModule::class,
        SearchUsersModule::class,
        SearchUserReposModule::class,
    ],
    dependencies = [AppDeps::class]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        fun withAppDeps(deps: AppDeps): Builder

        fun build(): ApplicationComponent

    }

}

interface AppDeps {
    val context: Context
}