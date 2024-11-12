package com.example.composegitapp.ui.screen_search_repos


import com.example.composegitapp.clean_arch_comp.domain.models.RepoItemDomain
import com.example.composegitapp.clean_arch_comp.domain.use_case.ISearchReposUseCase
import com.example.composegitapp.common.base.AbstractPagingSource
import javax.inject.Inject


class ReposPagingSource @Inject constructor(
    useCase: ISearchReposUseCase
) : AbstractPagingSource<ISearchReposUseCase, RepoItemDomain>(useCase)