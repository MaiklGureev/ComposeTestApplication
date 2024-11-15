package com.example.composegitapp.ui.screen_menu.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gureev.ComposeAppGitHub.R

@Composable
fun MenuContent(
    onSearchReposClicked: () -> Unit = {},
    onSearchUsersClicked: () -> Unit = {},
    onDownloadsClicked: () -> Unit = {},
    onResetTokenClicked: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 20.dp)
        ) {
            MenuButton(
                onClick = onSearchReposClicked,
                text = stringResource(id = R.string.search_repos)
            )
            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                onClick = onSearchUsersClicked,
                text = stringResource(id = R.string.search_users)
            )
            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                onClick = onDownloadsClicked,
                text = stringResource(id = R.string.downloads)
            )
            Spacer(modifier = Modifier.height(20.dp))

            MenuButton(
                onClick = onResetTokenClicked,
                text = stringResource(id = R.string.reset_token)
            )
        }
    }

}


@Preview
@Preview()
@Composable
private fun PreviewMenuContent(): Unit {
    MenuContent()
}