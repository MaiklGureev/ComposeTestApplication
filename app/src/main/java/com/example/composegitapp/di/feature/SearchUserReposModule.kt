package com.example.composegitapp.di.feature

import androidx.lifecycle.ViewModel
import com.example.composegitapp.clean_arch_comp.data.data_source.IUsersRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.data_source.UsersRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.repository.IReposRepository
import com.example.composegitapp.clean_arch_comp.data.repository.IUsersRepository
import com.example.composegitapp.clean_arch_comp.data.repository.UsersRepository
import com.example.composegitapp.di.view_model.ViewModelKey
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.UserItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.UserRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.use_case.ISearchUserReposUseCase
import com.example.composegitapp.clean_arch_comp.domain.use_case.ISearchUsersUseCase
import com.example.composegitapp.clean_arch_comp.domain.use_case.SearchUserReposUseCase
import com.example.composegitapp.clean_arch_comp.domain.use_case.SearchUsersUseCase
import com.example.composegitapp.ui.screen_search_users.SearchUsersViewModel
import com.example.composegitapp.ui.screen_user_repos.SearchUserReposViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchUserReposModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchUserReposViewModel::class)
    fun bindSearchUserReposViewModel(viewModel: SearchUserReposViewModel): ViewModel

    @Binds
    fun bindUserRepoItemMapper(bind: UserRepoItemMapper): IUserRepoItemMapper

    @Binds
    fun bindSearchUserReposUseCase(bind: SearchUserReposUseCase): ISearchUserReposUseCase
    
}