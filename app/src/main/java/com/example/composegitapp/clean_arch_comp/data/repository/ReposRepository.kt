package com.example.composegitapp.clean_arch_comp.data.repository

import com.example.composegitapp.clean_arch_comp.data.data_source.IRepoRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.dto.RepoBranchesDto
import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.example.composegitapp.clean_arch_comp.data.dto.RepoListDto
import com.example.composegitapp.network.NetworkParams
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
            sort = NetworkParams.Sort.UPDATED,
            order = NetworkParams.Order.DESC,
            perPage = NetworkParams.PER_PAGE_DEFAULT_VALUE,
        )
    }

    override suspend fun getUserRepos(userName: String): List<RepoListDto.RepoListDtoItem> {
        return dataSource.getUserRepos(userName)
    }

    override suspend fun getUserRepoBranches(
        userName: String,
        repoName: String
    ): List<RepoBranchesDto.RepoBranchesDtoItem> {
        return dataSource.getUserRepoBranches(userName = userName, repoName = repoName)
    }
}

interface IReposRepository {
    suspend fun searchReposDefault(
        query: String,
        page: Int,
    ): List<RepoDto.RepoItemDto>

    suspend fun getUserRepos(userName: String): List<RepoListDto.RepoListDtoItem>

    suspend fun getUserRepoBranches(
        userName: String,
        repoName: String
    ): List<RepoBranchesDto.RepoBranchesDtoItem>
}