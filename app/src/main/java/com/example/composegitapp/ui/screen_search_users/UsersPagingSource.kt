package com.example.composegitapp.ui.screen_search_users


import com.example.composegitapp.clean_arch_comp.domain.models.UserItemDomain
import com.example.composegitapp.clean_arch_comp.domain.use_case.ISearchUsersUseCase
import com.example.composegitapp.common.base.AbstractPagingSource
import javax.inject.Inject


class UsersPagingSource @Inject constructor(
    useCase: ISearchUsersUseCase
) : AbstractPagingSource<ISearchUsersUseCase, UserItemDomain>(useCase)