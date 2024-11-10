package com.example.composegitapp.clean_arch_comp.data.dto


import com.google.gson.annotations.SerializedName

class RepoBranchesDto : ArrayList<RepoBranchesDto.RepoBranchesDtoItem>(){
    data class RepoBranchesDtoItem(
        @SerializedName("name") val name: String? = null,
        @SerializedName("commit") val commit: Commit? = null,
        @SerializedName("protected") val protectedValue: Boolean? = null
    ) {
        data class Commit(
            @SerializedName("sha") val sha: String? = null,
            @SerializedName("url") val url: String? = null
        )
    }
}