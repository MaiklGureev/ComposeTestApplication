package com.example.composegitapp.clean_arch_comp.data.repository

import com.example.composegitapp.clean_arch_comp.data.data_source.IRepoRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.example.composegitapp.clean_arch_comp.data.dto.RepoListDto
import com.example.composegitapp.network.ResponseParams
import javax.inject.Inject

class ReposRepository @Inject constructor(
    private val dataSource: IRepoRemoteDataSource
) : IReposRepository {

    override suspend fun searchReposDefault(
        query: String,
        page: Int
    ): List<RepoDto.RepoItemDto> {
        return dataSource.searchRepositories(
            query = query,
            page = page,
            sort = ResponseParams.Sort.UPDATED,
            order = ResponseParams.Order.DESC,
            perPage = ResponseParams.PER_PAGE_DEFAULT_VALUE,
        )
    }

    override suspend fun getUserRepos(userName: String): List<RepoListDto.RepoListDtoItem> {
        return dataSource.getUserRepos(userName)
    }

}

interface IReposRepository {
    suspend fun searchReposDefault(
        query: String,
        page: Int,
    ): List<RepoDto.RepoItemDto>

    suspend fun getUserRepos(userName: String): List<RepoListDto.RepoListDtoItem>
}