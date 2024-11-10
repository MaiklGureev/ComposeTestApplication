package com.example.composegitapp.clean_arch_comp.domain.mappers

import com.example.composegitapp.clean_arch_comp.data.dto.UserDto
import com.example.composegitapp.clean_arch_comp.domain.models.UserItemDomain
import javax.inject.Inject

class UserItemMapper @Inject constructor(): IUserItemMapper {
    override fun map(item: UserDto.UserDtoItem): UserItemDomain {
        return UserItemDomain(
            id = item.id ?: item.login.hashCode(),
            name = item.login.orEmpty(),
            url = item.htmlUrl.orEmpty()
        )
    }
}

interface IUserItemMapper{
    fun map(item: UserDto.UserDtoItem): UserItemDomain
}