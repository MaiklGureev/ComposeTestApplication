package com.example.composegitapp.ui.screen_search_repos.widgets

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.composegitapp.ui.NavigationScreens
import com.example.composegitapp.ui.design_system.ErrorItem
import com.example.composegitapp.ui.design_system.LoadingView
import com.example.composegitapp.ui.screen_search_repos.SearchRepoViewModel
import com.example.composegitapp.ui.screen_search_repos.SearchRepoViewModel.RepoUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRepoContent(
    query: String,
    innerPadding: PaddingValues,
    viewModel: SearchRepoViewModel,
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    val pagingItems: LazyPagingItems<RepoUiModel> = viewModel.items.collectAsLazyPagingItems()
    val refreshing by viewModel.isRefreshing.collectAsState()
    val context = LocalContext.current


    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = viewModel::refresh,
    ) {
        LazyColumn(
            modifier = modifier
                .padding(innerPadding)
        ) {

            items(pagingItems.itemCount) { userId ->
                val item = pagingItems[userId] ?: return@items
                key(userId) {
                    val onUserCardClick: () -> Unit = {
                        navHostController.navigate(
                            NavigationScreens.UserReposWithNickName.toRoute(item.author)
                        )
                    }

                    RepoViewItem(
                        dataViewModel = item,
                        onCardClick = onUserCardClick
                    )
                }
            }


            pagingItems.apply {

                when {
                    loadState.refresh is LoadState.Loading -> {
                        if (query.isNotBlank() || query.length >= 3) {
                            item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        if (query.isNotBlank() || query.length >= 3) {
                            item { LoadingView(modifier = Modifier.fillParentMaxWidth()) }
                        }
                    }

                    loadState.refresh is LoadState.Error -> {
                        val e = pagingItems.loadState.refresh as LoadState.Error
                        if (pagingItems.itemCount > 0) {
                            Toast.makeText(
                                context,
                                e.error.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        item {
                            if (pagingItems.itemCount == 0) {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    modifier = Modifier.fillParentMaxSize(),
                                    onClickRetry = { pagingItems.retry() }
                                )
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        val e = pagingItems.loadState.append as LoadState.Error
                        item {
                            ErrorItem(
                                message = e.error.localizedMessage!!,
                                onClickRetry = { pagingItems.retry() }
                            )
                        }
                    }
                }
            }
        }

    }
}

