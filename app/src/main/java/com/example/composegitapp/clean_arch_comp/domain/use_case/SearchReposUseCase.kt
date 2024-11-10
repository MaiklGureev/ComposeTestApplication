package com.example.composegitapp.clean_arch_comp.domain.use_case

import com.example.composegitapp.clean_arch_comp.data.repository.IReposRepository
import com.example.composegitapp.clean_arch_comp.data.repository.IUsersRepository
import com.example.composegitapp.clean_arch_comp.domain.mappers.IRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserItemMapper
import com.example.composegitapp.clean_arch_comp.domain.models.RepoItemDomain
import com.example.composegitapp.clean_arch_comp.domain.models.UserItemDomain
import com.example.composegitapp.common.base.IPagingSourceUseCase
import javax.inject.Inject


class SearchReposUseCase @Inject constructor(
    private val repo: IReposRepository,
    private val mapper: IRepoItemMapper
) : ISearchReposUseCase {
    override suspend fun loadData(query: String, page: Int): List<RepoItemDomain> {
        return repo.searchReposDefault(query, page).map { mapper.map(it) }
    }
}

interface ISearchReposUseCase : IPagingSourceUseCase<RepoItemDomain>