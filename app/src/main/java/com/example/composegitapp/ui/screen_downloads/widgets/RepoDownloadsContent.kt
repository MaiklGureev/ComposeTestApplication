package com.example.composegitapp.ui.screen_downloads.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composegitapp.ui.design_system.LoadingItem
import com.example.composegitapp.ui.screen_downloads.RepoDownloadsViewModel
import com.gureev.ComposeAppGitHub.R

@Composable
fun RepoDownloadsContent(
    innerPadding: PaddingValues,
    viewModel: RepoDownloadsViewModel
) {
    val uiState = viewModel.uiState.value

    FirstLoadingData(viewModel)


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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                )
            }

            uiState.repoDownloadsList.isNotEmpty() -> {
                LazyColumn {
                    items(items = uiState.repoDownloadsList) { item ->
                        val onCardClick: () -> Unit = {
                            viewModel.onItemClicked(item)
                        }

                        RepoDownloadViewItem(
                            dataViewModel = item,
                            onCardClick = onCardClick,
                        )
                    }
                }
            }

            else -> {
                Text(
                    text = stringResource(id = R.string.no_downloads),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                )
            }
        }
    }
}

@Composable
private fun FirstLoadingData(viewModel: RepoDownloadsViewModel) {
    val hasLaunched = rememberSaveable { mutableStateOf(false) }

    if (!hasLaunched.value) {
        viewModel.getRepoDownloads()
        hasLaunched.value = true
    }
}
