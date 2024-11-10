package com.example.composegitapp.ui.screen_search_users.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composegitapp.clean_arch_comp.domain.models.UserItemDomain
import com.example.composegitapp.common.extension.ContextExt.openUrl
import com.example.composegitapp.ui.design_system.DataTextView
import com.example.composegitapp.ui.screen_search_users.SearchUsersViewModel
import com.gureev.ComposeAppGitHub.R

@Composable
fun UserViewItem(
    dataViewModel: SearchUsersViewModel.UserUiModel,
    onCardClick: () -> Unit = { -> }
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
            DataTextView(
                key = stringResource(id = R.string.user_login),
                value = dataViewModel.name,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            )
            IconButtonLink(
                url = dataViewModel.url,
            )
        }
    }
}

@Composable
fun IconButtonLink(url: String) {
    val context = LocalContext.current
    IconButton(onClick = { context.openUrl(url) }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_browser),
            contentDescription = null,
            tint = Color.Blue
        )
    }
}


@Composable
@Preview
private fun PreviewUserViewItem() {
    UserViewItem(
        dataViewModel = SearchUsersViewModel.UserUiModel(
            id = 9367,
            name = "Hang",
            url = ""
        ),
        onCardClick = { -> }
    )
}