package com.example.composegitapp.clean_arch_comp.data.data_source

import com.example.composegitapp.clean_arch_comp.data.dto.RepoBranchesDto
import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.example.composegitapp.clean_arch_comp.data.dto.RepoListDto
import com.example.composegitapp.network.IGitHubApi
import javax.inject.Inject

class RepoRemoteDataSource @Inject constructor(
    private val api: IGitHubApi,
) : IRepoRemoteDataSource {

    override suspend fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int,
    ): List<RepoDto.RepoItemDto> {

        val result = api.searchRepository(
            query = query,
            sort = sort,
            order = order,
            perPage = perPage,
            page = page
        )

        return when (result.code()) {
            200 -> {
                result.body()?.items.orEmpty()
            }

            304 -> {
                // Not modified: Возвращаем пустой список или другой подходящий ответ
                emptyList()
            }

            422 -> {
                // Validation failed: Возвращаем сообщение об ошибке
                throw IllegalArgumentException("Validation failed for the query: $query")
            }

            503 -> {
                // Service unavailable: Возвращаем сообщение о недоступности сервиса
                throw IllegalStateException("Service is unavailable. Please try again later.")
            }

            else -> {
                // Обработка любого другого кода ответа (например, логирование или возврат пустого списка)
                emptyList()
            }
        }
    }

    override suspend fun getUserRepos(userName: String): List<RepoListDto.RepoListDtoItem> {
        val result = api.getUserRepos(userName)
        return when (result.code()) {
            200 -> {
                result.body().orEmpty()
            }

            else -> {
                emptyList()
            }
        }
    }

    override suspend fun getUserBranches(
        userName: String,
        repoName: String
    ): List<RepoBranchesDto.RepoBranchesDtoItem> {
        val result = api.getUserBranches(userName, repoName)
        return when (result.code()) {
            200 -> {
                result.body().orEmpty()
            }

            else -> {
                emptyList()
            }
        }
    }


}

interface IRepoRemoteDataSource {
    suspend fun searchRepositories(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int,
    ): List<RepoDto.RepoItemDto>

    suspend fun getUserRepos(userName: String): List<RepoListDto.RepoListDtoItem>

    suspend fun getUserBranches(
        userName: String,
        repoName: String
    ): List<RepoBranchesDto.RepoBranchesDtoItem>
}