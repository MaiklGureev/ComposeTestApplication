package com.example.composegitapp.di.feature

import androidx.lifecycle.ViewModel
import com.example.composegitapp.di.view_model.ViewModelKey
import com.example.composegitapp.ui.screen_downloads.RepoDownloadsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface RepoDownloadsModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepoDownloadsViewModel::class)
    fun bindRepoDownloadsViewModel(viewModel: RepoDownloadsViewModel): ViewModel


}