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

}