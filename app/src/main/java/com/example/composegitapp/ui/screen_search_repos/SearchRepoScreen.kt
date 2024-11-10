package com.example.composegitapp.ui.screen_search_repos

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.composegitapp.ui.screen_search_repos.widgets.SearchRepoContent
import com.example.composegitapp.ui.design_system.SearchTextField
import com.gureev.ComposeAppGitHub.R

@Composable
fun SearchRepoScreen(
    viewModel: SearchRepoViewModel,
    navHostController: NavHostController,
) {
    val query by viewModel.query.collectAsState(initial = "")
    Scaffold(
        topBar = {
            SearchTextField(
                label = stringResource(id = R.string.search_repos),
                query = query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::onSearch,
                modifier = Modifier.padding(20.dp)
            )
        }
    ) { innerPadding ->
        SearchRepoContent(
            query = query,
            innerPadding = innerPadding,
            viewModel = viewModel,
            navHostController = navHostController)
    }

}
