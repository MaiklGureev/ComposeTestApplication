package com.example.composegitapp.di.feature

import androidx.lifecycle.ViewModel
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserRepoBranchItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.UserRepoBranchItemMapper
import com.example.composegitapp.clean_arch_comp.domain.use_case.GetUserRepoBranchesUseCase
import com.example.composegitapp.clean_arch_comp.domain.use_case.IGetUserRepoBranchesUseCase
import com.example.composegitapp.di.view_model.ViewModelKey
import com.example.composegitapp.ui.screen_repo_branches.UserRepoBranchesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface UserRepoBranchesModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserRepoBranchesViewModel::class)
    fun bindUserRepoBranchesViewModel(viewModel: UserRepoBranchesViewModel): ViewModel

    @Binds
    fun bindUserRepoBranchItemMapper(bind: UserRepoBranchItemMapper): IUserRepoBranchItemMapper

    @Binds
    fun bindGetUserRepoBranchesUseCase(bind: GetUserRepoBranchesUseCase): IGetUserRepoBranchesUseCase

}