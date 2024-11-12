package com.example.composegitapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composegitapp.ui.screen_menu.MenuScreen
import com.example.composegitapp.ui.screen_repo_branches.UserRepoBranchesScreen
import com.example.composegitapp.ui.screen_repo_branches.UserRepoBranchesViewModel
import com.example.composegitapp.ui.screen_search_repos.SearchRepoScreen
import com.example.composegitapp.ui.screen_search_repos.SearchRepoViewModel
import com.example.composegitapp.ui.screen_search_users.SearchUsersScreen
import com.example.composegitapp.ui.screen_search_users.SearchUsersViewModel
import com.example.composegitapp.ui.screen_splash.SplashScreen
import com.example.composegitapp.ui.screen_user_repos.SearchUserReposScreen
import com.example.composegitapp.ui.screen_user_repos.UserReposViewModel

@Composable
fun NavHostCreator(viewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationScreens.Splash.name) {
        composable(NavigationScreens.Splash.name) {
            SplashScreen(navHostController = navController, delay = 500)
        }

        composable(NavigationScreens.Menu.name) {
            MenuScreen(
                navHostController = navController,
            )
        }

        composable(NavigationScreens.SearchRepoScreen.name) {
            val viewModel: SearchRepoViewModel = viewModel(factory = viewModelFactory)
            SearchRepoScreen(viewModel, navController)
        }

        composable(NavigationScreens.SearchUserScreen.name) {
            val viewModel: SearchUsersViewModel = viewModel(factory = viewModelFactory)
            SearchUsersScreen(viewModel, navController)
        }

        composable(
            route = NavigationScreens.UserReposWithNickName.name,
            arguments = listOf(navArgument(NavigationScreens.UserReposWithNickName.key) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userName: String = backStackEntry.arguments?.getString(
                NavigationScreens.UserReposWithNickName.key
            ).orEmpty()

            val viewModel: UserReposViewModel = viewModel(factory = viewModelFactory)
            SearchUserReposScreen(userName, viewModel, navController)
        }

        composable(
            route = NavigationScreens.RepoBranches.name,
            arguments = listOf(
                navArgument(NavigationScreens.RepoBranches.keyRepo) {
                    type = NavType.StringType
                },
                navArgument(NavigationScreens.RepoBranches.keyUser) {
                    type = NavType.StringType
                })
        ) { backStackEntry ->

            val userName: String = backStackEntry.arguments?.getString(
                NavigationScreens.RepoBranches.keyUser
            ).orEmpty()


            val repoName: String = backStackEntry.arguments?.getString(
                NavigationScreens.RepoBranches.keyRepo
            ).orEmpty()

            val viewModel: UserRepoBranchesViewModel = viewModel(factory = viewModelFactory)
            UserRepoBranchesScreen(userName = userName, repoName = repoName, viewModel = viewModel)
        }
    }
}

