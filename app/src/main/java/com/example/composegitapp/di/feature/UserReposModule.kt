package com.example.composegitapp.di.feature

import androidx.lifecycle.ViewModel
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.UserRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.use_case.GetUserReposUseCase
import com.example.composegitapp.clean_arch_comp.domain.use_case.IGetUserReposUseCase
import com.example.composegitapp.di.view_model.ViewModelKey
import com.example.composegitapp.ui.screen_user_repos.UserReposViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface UserReposModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserReposViewModel::class)
    fun bindUserReposViewModel(viewModel: UserReposViewModel): ViewModel

    @Binds
    fun bindUserRepoItemMapper(bind: UserRepoItemMapper): IUserRepoItemMapper

    @Binds
    fun bindSearchUserReposUseCase(bind: GetUserReposUseCase): IGetUserReposUseCase

}