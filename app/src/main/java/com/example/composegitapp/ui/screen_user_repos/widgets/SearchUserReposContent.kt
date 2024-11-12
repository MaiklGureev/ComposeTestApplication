package com.example.composegitapp.ui.screen_user_repos.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.composegitapp.ui.NavigationScreens
import com.example.composegitapp.ui.design_system.LoadingItem
import com.example.composegitapp.ui.screen_user_repos.UserReposViewModel
import com.gureev.ComposeAppGitHub.R

@Composable
fun SearchUserReposContent(
    userName: String,
    innerPadding: PaddingValues,
    viewModel: UserReposViewModel,
    navHostController: NavHostController
) {
    val uiState = viewModel.uiState.value
    LaunchedEffect(key1 = Unit) {
        viewModel.searchRepos(userName)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        when {
            uiState.isLoading -> {
                LoadingItem()
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            uiState.repoList.isNotEmpty() -> {
                LazyColumn {
                    items(uiState.repoList) { item ->
                        val onCardClick: () -> Unit = {
                            navHostController.navigate(
                                NavigationScreens.RepoBranches.toRoute(
                                    item.name,
                                    item.author
                                )
                            )
                        }
                        UserRepoViewItem(dataViewModel = item, onCardClick = onCardClick)
                    }
                }
            }

            else -> {
                Text(
                    text = stringResource(id = R.string.no_repos),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

