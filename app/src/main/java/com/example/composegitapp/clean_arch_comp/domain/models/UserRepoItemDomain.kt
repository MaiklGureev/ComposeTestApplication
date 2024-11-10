package com.example.composegitapp.clean_arch_comp.domain.models

import com.example.composegitapp.common.base.IPagingSourceModel

data class UserRepoItemDomain(
    val id:Int,
    val author:String,
    val description:String,
    val name:String,
    val fullName:String,
    val url:String
): IPagingSourceModel