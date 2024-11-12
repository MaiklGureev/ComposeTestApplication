package com.example.composegitapp.clean_arch_comp.domain.mappers

import com.example.composegitapp.clean_arch_comp.data.dto.RepoListDto
import com.example.composegitapp.clean_arch_comp.domain.models.UserRepoItemDomain
import javax.inject.Inject

class UserRepoItemMapper @Inject constructor() : IUserRepoItemMapper {
    override fun map(item: RepoListDto.RepoListDtoItem): UserRepoItemDomain {
        return UserRepoItemDomain(
            id = item.id ?: item.fullName.hashCode(),
            author = item.owner?.login.orEmpty(),
            description = item.description.orEmpty(),
            name = item.name.orEmpty(),
            fullName = item.fullName.orEmpty(),
            url = item.htmlUrl.orEmpty()
        )
    }
}

interface IUserRepoItemMapper {
    fun map(item: RepoListDto.RepoListDtoItem): UserRepoItemDomain
}