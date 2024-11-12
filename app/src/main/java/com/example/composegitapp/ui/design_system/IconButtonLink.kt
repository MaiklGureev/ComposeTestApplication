package com.example.composegitapp.ui.design_system

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.composegitapp.common.extension.ContextExt.openUrl
import com.gureev.ComposeAppGitHub.R

enum class DownloadStatus { NoDownloaded, Progress, Downloaded }

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
