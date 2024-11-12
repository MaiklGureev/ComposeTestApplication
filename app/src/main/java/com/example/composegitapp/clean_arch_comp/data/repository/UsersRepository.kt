package com.example.composegitapp.clean_arch_comp.data.repository

import com.example.composegitapp.clean_arch_comp.data.data_source.IUsersRemoteDataSource
import com.example.composegitapp.clean_arch_comp.data.dto.UserDto
import com.example.composegitapp.network.NetworkParams
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val dataSource: IUsersRemoteDataSource
) : IUsersRepository {
    override suspend fun searchUsersDefault(
        query: String,
        page: Int
    ): List<UserDto.UserDtoItem> {
        return dataSource.searchUsers(
            query = query,
            page = page,
            sort = NetworkParams.Sort.UPDATED,
            order = NetworkParams.Order.DESC,
            perPage = NetworkParams.PER_PAGE_DEFAULT_VALUE,
        )
    }

}

interface IUsersRepository {
    suspend fun searchUsersDefault(
        query: String,
        page: Int,
    ): List<UserDto.UserDtoItem>
}