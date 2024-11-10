package com.example.composegitapp.ui.screen_user_repos.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.composegitapp.ui.screen_user_repos.SearchUserReposViewModel
import com.gureev.ComposeAppGitHub.R

@Composable
fun SearchUserReposContent(
    userName: String,
    innerPadding: PaddingValues,
    viewModel: SearchUserReposViewModel,
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
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
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
                        val onCardClick:() -> Unit = {
                            item
                        }
                        UserRepoItem(dataViewModel = item,onCardClick=onCardClick)
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

