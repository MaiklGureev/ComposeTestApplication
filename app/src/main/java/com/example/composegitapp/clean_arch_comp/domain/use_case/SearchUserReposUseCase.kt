package com.example.composegitapp.clean_arch_comp.domain.use_case

import com.example.composegitapp.clean_arch_comp.data.repository.IReposRepository
import com.example.composegitapp.clean_arch_comp.data.repository.IUsersRepository
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserItemMapper
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.models.UserItemDomain
import com.example.composegitapp.clean_arch_comp.domain.models.UserRepoItemDomain
import com.example.composegitapp.common.base.IPagingSourceUseCase
import javax.inject.Inject

class SearchUserReposUseCase @Inject constructor(
    private val repo: IReposRepository,
    private val mapper: IUserRepoItemMapper
) : ISearchUserReposUseCase {
    override suspend fun loadData(userName: String): List<UserRepoItemDomain> {
        return repo.getUserRepos(userName).map { mapper.map(it) }
    }
}

interface ISearchUserReposUseCase {
    suspend fun loadData(userName: String): List<UserRepoItemDomain>
}