package com.example.composegitapp.ui.design_system

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gureev.ComposeAppGitHub.R

@Composable
fun IconButtonDownload(
    status: DownloadStatus,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.size(30.dp)) {
        when (status) {
            DownloadStatus.NoDownloaded -> {
                IconButton(
                    onClick = onClick
                ) {
                    Icon(
                        modifier = Modifier.matchParentSize(),
                        painter = painterResource(id = R.drawable.ic_download),
                        contentDescription = null,
                        tint = Color.Blue
                    )
                }
            }

            DownloadStatus.Progress -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .matchParentSize()
                        .align(Alignment.Center),
                )
            }

            DownloadStatus.Downloaded -> {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.matchParentSize(),
                        painter = painterResource(id = R.drawable.ic_download_done),
                        contentDescription = null,
                        tint = Color.Blue
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewIconButtonDownloadDownloaded() {
    IconButtonDownload(status = DownloadStatus.Downloaded, onClick = { -> })
}

@Preview
@Composable
private fun PreviewIconButtonDownloadNoDownloaded() {
    IconButtonDownload(status = DownloadStatus.NoDownloaded, onClick = { -> })
}

@Preview
@Composable
private fun PreviewIconButtonDownloadProgress() {
    IconButtonDownload(status = DownloadStatus.Progress, onClick = { -> })
}