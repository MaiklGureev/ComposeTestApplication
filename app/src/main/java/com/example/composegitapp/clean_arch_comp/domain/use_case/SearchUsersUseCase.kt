package com.example.composegitapp.clean_arch_comp.domain.use_case

import com.example.composegitapp.clean_arch_comp.data.repository.IUsersRepository
import com.example.composegitapp.clean_arch_comp.domain.mappers.IUserItemMapper
import com.example.composegitapp.clean_arch_comp.domain.models.UserItemDomain
import com.example.composegitapp.common.base.IPagingSourceUseCase
import javax.inject.Inject

class SearchUsersUseCase @Inject constructor(
    private val repo: IUsersRepository,
    private val mapper: IUserItemMapper
) : ISearchUsersUseCase {
    override suspend fun loadData(query: String, page: Int): List<UserItemDomain> {
        return repo.searchUsersDefault(query, page).map { mapper.map(it) }
    }
}

interface ISearchUsersUseCase : IPagingSourceUseCase<UserItemDomain>