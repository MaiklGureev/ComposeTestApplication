package com.example.composegitapp.clean_arch_comp.domain.models

import com.example.composegitapp.common.base.IPagingSourceModel

data class UserItemDomain(
    val id: Int,
    val name: String,
    val url: String
) : IPagingSourceModel