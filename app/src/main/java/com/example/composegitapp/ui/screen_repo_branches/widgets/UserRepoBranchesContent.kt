package com.example.composegitapp.ui.screen_repo_branches.widgets

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composegitapp.common.extension.openAppPermissionSettings
import com.example.composegitapp.common.extension.showLongToast
import com.example.composegitapp.common.utils.downloads.IDownloadManager
import com.example.composegitapp.ui.design_system.LoadingItem
import com.example.composegitapp.ui.screen_repo_branches.UserRepoBranchesViewModel
import com.gureev.ComposeAppGitHub.R

@Composable
fun RepoBranchesContent(
    userName: String,
    repoName: String,
    innerPadding: PaddingValues,
    viewModel: UserRepoBranchesViewModel,
) {
    val uiState = viewModel.uiState.value

    val downloadEvent by viewModel.eventDownloads.collectAsState(initial = IDownloadManager.DownloadEvents.Empty)
    val context = LocalContext.current
    when (downloadEvent) {
        is IDownloadManager.DownloadEvents.PermissionError -> {
            context.openAppPermissionSettings()
        }

        is IDownloadManager.DownloadEvents.Downloaded -> {
            val text =
                "Загрузка завершена! Файл: ${(downloadEvent as IDownloadManager.DownloadEvents.Downloaded).task.fileName}"
            context.showLongToast(text)
        }

        is IDownloadManager.DownloadEvents.Error -> {
            val text =
                "Ошибка при загрузке! Файл: ${(downloadEvent as IDownloadManager.DownloadEvents.Error).task.fileName}"
            context.showLongToast(text)
        }

        else -> Unit
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getRepoBranches(userName, repoName)
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
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                )
            }

            uiState.repoBranchList.isNotEmpty() -> {
                LazyColumn {
                    items(items = uiState.repoBranchList) { item ->
                        val onCardClick: () -> Unit = {
                        }

                        val onDownloadClick: () -> Unit = {
                            viewModel.onDownloadIconClicked(item, userName, repoName)
                        }

                        RepoBranchViewItem(
                            dataViewModel = item,
                            onCardClick = onCardClick,
                            onDownloadClick = onDownloadClick
                        )
                    }
                }
            }

            else -> {
                Text(
                    text = stringResource(id = R.string.no_repos),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(20.dp)
                )
            }
        }
    }
}
