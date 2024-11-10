package com.example.composegitapp.ui.screen_search_repos.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composegitapp.ui.design_system.DataTextView
import com.example.composegitapp.ui.screen_search_repos.SearchRepoViewModel
import com.example.composegitapp.ui.screen_search_users.widgets.IconButtonLink
import com.gureev.ComposeAppGitHub.R

@Composable
fun RepoViewItem(
    dataViewModel: SearchRepoViewModel.RepoUiModel,
    onCardClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable(onClick = onCardClick)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            Column(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                DataTextView(
                    key = stringResource(id = R.string.user_login),
                    value = dataViewModel.author,
                    modifier = Modifier.wrapContentHeight()
                )
                DataTextView(
                    key = stringResource(id = R.string.repo_name),
                    value = dataViewModel.name,
                    modifier = Modifier.wrapContentHeight()
                )
                DataTextView(
                    key = stringResource(id = R.string.repo_description),
                    value = dataViewModel.description.ifBlank { stringResource(id = R.string.empty) },
                    modifier = Modifier.wrapContentHeight()
                )
            }

            // Icon button to open URL
            IconButtonLink(
                url = dataViewModel.htmlUrl
            )
        }
    }
}


@Composable
@Preview
private fun PreviewUserViewItem() {
    RepoViewItem(
        dataViewModel = SearchRepoViewModel.RepoUiModel(
            name = "Arika", author = "Cordell", htmlUrl = "Shaunda", description = "Karlee"
        ),
        onCardClick = { -> }
    )
}