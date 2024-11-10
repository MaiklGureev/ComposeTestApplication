package com.example.composegitapp.ui.screen_user_repos

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.composegitapp.ui.screen_user_repos.widgets.SearchUserReposContent

@Composable
fun SearchUserReposScreen(
    userName: String,
    viewModel: SearchUserReposViewModel,
    navHostController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = userName) })
        }
    ) { innerPadding ->
        SearchUserReposContent(
            userName = userName,
            innerPadding = innerPadding,
            viewModel = viewModel,
            navHostController = navHostController
        )
    }
}
