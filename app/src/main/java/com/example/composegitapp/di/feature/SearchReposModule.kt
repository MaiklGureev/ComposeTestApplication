package com.example.composegitapp.di.feature

import androidx.lifecycle.ViewModel
import com.example.composegitapp.clean_arch_comp.data.data_source.IRepoRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.data_source.RepoRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.repository.IReposRepository
import com.example.composegitapp.clean_arch_comp.data.repository.ReposRepository
import com.example.composegitapp.clean_arch_comp.domain.mappers.IRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.RepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.use_case.ISearchReposUseCase
import com.example.composegitapp.clean_arch_comp.domain.use_case.SearchReposUseCase
import com.example.composegitapp.di.view_model.ViewModelKey
import com.example.composegitapp.ui.screen_search_repos.SearchRepoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchReposModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchRepoViewModel::class)
    fun bindSearchRepoViewModel(viewModel: SearchRepoViewModel): ViewModel

    @Binds
    fun bindRepositoryRemoteDataSource(bind: RepoRemoteDataSource): IRepoRemoteDataSource

    @Binds
    fun bindIRepositoryRepository(bind: ReposRepository): IReposRepository

    @Binds
    fun bindSearchReposUseCase(bind: SearchReposUseCase): ISearchReposUseCase

    @Binds
    fun bindRepoItemMapper(bind: RepoItemMapper): IRepoItemMapper

}