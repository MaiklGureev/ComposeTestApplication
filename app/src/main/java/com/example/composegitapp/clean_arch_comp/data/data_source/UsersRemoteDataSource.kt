package com.example.composegitapp.clean_arch_comp.data.data_source

import com.example.composegitapp.clean_arch_comp.data.dto.UserDto
import com.example.composegitapp.network.IGitHubApi
import javax.inject.Inject

class UsersRemoteDataSource @Inject constructor(
    private val api: IGitHubApi,
) : IUsersRemoteDataSource {

    override suspend fun searchUsers(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int,
    ): List<UserDto.UserDtoItem> {

        val result = api.searchUsers(
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
}

interface IUsersRemoteDataSource {
    suspend fun searchUsers(
        query: String,
        sort: String,
        order: String,
        perPage: Int,
        page: Int,
    ): List<UserDto.UserDtoItem>
}