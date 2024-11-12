package com.example.composegitapp.ui.screen_repo_branches

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.composegitapp.ui.screen_repo_branches.widgets.RepoBranchesContent

@Composable
fun UserRepoBranchesScreen(
    userName: String,
    repoName: String,
    viewModel: UserRepoBranchesViewModel,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = repoName) })
        }
    ) { innerPadding ->
        RepoBranchesContent(
            userName = userName,
            repoName = repoName,
            innerPadding = innerPadding,
            viewModel = viewModel,
        )
    }
}
