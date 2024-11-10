package com.example.composegitapp.di

import androidx.lifecycle.ViewModelProvider
import com.example.composegitapp.di.view_model.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface BaseViewModelFactoryModule {
    @Binds
    fun bindViewModelFactory(viewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory
}