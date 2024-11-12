package com.example.composegitapp.clean_arch_comp.domain.mappers

import com.example.composegitapp.clean_arch_comp.data.dto.RepoBranchesDto
import com.example.composegitapp.clean_arch_comp.domain.models.RepoBranchItemDomain
import com.example.composegitapp.common.preferences.IAppSettings
import javax.inject.Inject

class UserRepoBranchItemMapper @Inject constructor(
    private val appSettings: IAppSettings
) : IUserRepoBranchItemMapper {
    override fun map(
        userName: String,
        repoName: String,
        item: RepoBranchesDto.RepoBranchesDtoItem,
    ): RepoBranchItemDomain {
        return RepoBranchItemDomain(
            id = item.name.hashCode(),
            name = item.name.orEmpty(),
            url = buildDownloadUrl(userName, repoName, item.commit?.sha.orEmpty())
        )
    }

    //https://api.github.com/repos/MaiklGureev/OtusApplication/zipball/master
    private fun buildDownloadUrl(userName: String, repoName: String, branchName: String): String {
        val base = appSettings.getCurrentApiUrl()
        return "${base}repos/$userName/$repoName/zipball/$branchName"
    }
}

interface IUserRepoBranchItemMapper {
    fun map(
        userName: String,
        repoName: String,
        item: RepoBranchesDto.RepoBranchesDtoItem,
    ): RepoBranchItemDomain
}