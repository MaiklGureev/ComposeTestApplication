package com.example.composegitapp.ui

sealed class NavigationScreens(val name: String) {

    data object Splash : NavigationScreens("Splash")

    data object Menu : NavigationScreens("Menu")

    data object SearchRepoScreen : NavigationScreens("SearchRepoScreen")

    data object SearchUserScreen : NavigationScreens("SearchUserScreen")

    data object UserReposWithNickName : NavigationScreens("UserReposWithNickName/{NickName}") {
        fun toRoute(nickName: String): String {
            return "UserReposWithNickName/$nickName"
        }

        const val key = "NickName"
    }

    data object RepoBranches : NavigationScreens("RepoBranches/{RepoName}/{UserName}") {
        fun toRoute(repoName: String, userName: String): String {
            return "RepoBranches/$repoName/$userName"
        }

        const val keyRepo = "RepoName"
        const val keyUser = "UserName"
    }

}