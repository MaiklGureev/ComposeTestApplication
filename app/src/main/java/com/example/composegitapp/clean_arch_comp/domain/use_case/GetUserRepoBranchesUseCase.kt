package com.example.composegitapp.clean_arch_comp.domain.use_case

import com.example.composegitapp.clean_arch_comp.data.repository.IReposRepository
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserRepoBranchItemMapper
import com.example.composegitapp.clean_arch_comp.domain.models.RepoBranchItemDomain
import javax.inject.Inject


class GetUserRepoBranchesUseCase @Inject constructor(
    private val repo: IReposRepository,
    private val mapper: IUserRepoBranchItemMapper
) : IGetUserRepoBranchesUseCase {
    override suspend fun loadData(userName: String, userRepo: String): List<RepoBranchItemDomain> {
        return repo.getUserRepoBranches(userName, userRepo)
            .map { mapper.map(userName, userRepo, it) }
    }
}

interface IGetUserRepoBranchesUseCase {
    suspend fun loadData(userName: String, userRepo: String): List<RepoBranchItemDomain>
}