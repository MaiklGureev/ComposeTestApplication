package com.example.composegitapp.ui.screen_downloads

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.composegitapp.ui.screen_downloads.widgets.RepoDownloadsContent
import com.gureev.ComposeAppGitHub.R

@Composable
fun RepoDownloadsScreen(
    viewModel: RepoDownloadsViewModel,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.downloads)) })
        }
    ) { innerPadding ->
        RepoDownloadsContent(
            innerPadding = innerPadding,
            viewModel = viewModel,
        )
    }
}

