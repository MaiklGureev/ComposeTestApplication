package com.example.composegitapp

import android.app.Application
import android.content.Context
import com.example.composegitapp.di.AppDeps
import com.example.composegitapp.di.ApplicationComponent
import com.example.composegitapp.di.DaggerApplicationComponent

class App() : Application(), AppDeps {

    lateinit var appComponent: ApplicationComponent

    override val context: Context = this

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .withAppDeps(this)
            .build()
    }

}



