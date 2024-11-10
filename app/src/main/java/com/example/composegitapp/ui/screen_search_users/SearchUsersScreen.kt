package com.example.composegitapp.ui.screen_search_users

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.composegitapp.ui.design_system.SearchTextField
import com.example.composegitapp.ui.screen_search_users.widgets.SearchUsersContent
import com.gureev.ComposeAppGitHub.R

@Composable
fun SearchUsersScreen(
    viewModel: SearchUsersViewModel,
    navHostController: NavHostController,
) {
    val query by viewModel.query.collectAsState(initial = "")
    Scaffold(
        topBar = {
            SearchTextField(
                label = stringResource(id = R.string.search_users),
                query = query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::onSearch,
                modifier = Modifier.padding(20.dp)
            )
        }
    ) { innerPadding ->
        SearchUsersContent(
            query = query,
            innerPadding = innerPadding,
            viewModel = viewModel,
            navHostController = navHostController
        )
    }

}
