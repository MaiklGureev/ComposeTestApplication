package com.example.composegitapp.clean_arch_comp.domain.use_case

import com.example.composegitapp.clean_arch_comp.data.repository.IReposRepository
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserRepoItemMapper
import com.example.composegitapp.clean_arch_comp.domain.models.UserRepoItemDomain
import javax.inject.Inject

class GetUserReposUseCase @Inject constructor(
    private val repo: IReposRepository,
    private val mapper: IUserRepoItemMapper
) : IGetUserReposUseCase {
    override suspend fun loadData(userName: String): List<UserRepoItemDomain> {
        return repo.getUserRepos(userName).map { mapper.map(it) }
    }
}

interface IGetUserReposUseCase {
    suspend fun loadData(userName: String): List<UserRepoItemDomain>
}