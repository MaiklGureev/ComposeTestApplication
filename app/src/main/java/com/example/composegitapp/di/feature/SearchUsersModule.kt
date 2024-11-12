package com.example.composegitapp.di.feature

import androidx.lifecycle.ViewModel
import com.example.composegitapp.clean_arch_comp.data.data_source.IUsersRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.data_source.UsersRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.repository.IUsersRepository
import com.example.composegitapp.clean_arch_comp.data.repository.UsersRepository
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.UserItemMapper
import com.example.composegitapp.clean_arch_comp.domain.use_case.ISearchUsersUseCase
import com.example.composegitapp.clean_arch_comp.domain.use_case.SearchUsersUseCase
import com.example.composegitapp.di.view_model.ViewModelKey
import com.example.composegitapp.ui.screen_search_users.SearchUsersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchUsersModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchUsersViewModel::class)
    fun bindSearchUsersViewModel(viewModel: SearchUsersViewModel): ViewModel

    @Binds
    fun bindUserRemoteDataSource(bind: UsersRemoteDataSource): IUsersRemoteDataSource

    @Binds
    fun bindUsersRepository(bind: UsersRepository): IUsersRepository

    @Binds
    fun bindUserItemMapper(bind: UserItemMapper): IUserItemMapper

    @Binds
    fun bindSearchUsersUseCase(bind: SearchUsersUseCase): ISearchUsersUseCase

}