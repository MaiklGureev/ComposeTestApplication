package com.example.composegitapp.clean_arch_comp.domain.models

import com.example.composegitapp.common.base.IPagingSourceModel

data class RepoItemDomain(
    val author: String,
    val name: String,
    val description: String,
    val htmlUrl: String,
    val branchesNames: List<String>,
    val downloadLinks: List<Pair<String, String>>,
) : IPagingSourceModel