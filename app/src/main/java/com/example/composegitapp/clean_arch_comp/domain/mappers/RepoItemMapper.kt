package com.example.composegitapp.clean_arch_comp.domain.mappers

import com.example.composegitapp.clean_arch_comp.data.dto.RepoDto
import com.example.composegitapp.clean_arch_comp.domain.models.RepoItemDomain
import javax.inject.Inject

class RepoItemMapper@Inject constructor(): IRepoItemMapper {
    override fun map(item: RepoDto.RepoItemDto): RepoItemDomain {
        return RepoItemDomain(
            name = item.name.orEmpty(),
            author = item.owner?.login.orEmpty(),
            description = item.description.orEmpty(),
            htmlUrl = item.htmlUrl.orEmpty(),
            branchesNames = listOf(),
            downloadLinks = listOf()
        )
    }
}

interface IRepoItemMapper{
    fun map(item: RepoDto.RepoItemDto): RepoItemDomain
}