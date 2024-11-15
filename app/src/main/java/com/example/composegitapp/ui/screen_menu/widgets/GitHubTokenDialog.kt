package com.example.composegitapp.ui.screen_menu.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gureev.ComposeAppGitHub.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GitHubTokenDialog(
    onTokenEntered: (String) -> Unit,
    onExitApp: () -> Unit,
    onRestartApp: () -> Unit,
    isTokenFilled: Boolean
) {

    if (isTokenFilled) return

    var token by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    AlertDialog(
        onDismissRequest = onExitApp,
        title = {
            Text(text = stringResource(R.string.enter_github_token))
        },
        text = {
            Column {
                Text(text = stringResource(R.string.enter_github_token_for_continue))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it },
                    label = { Text(stringResource(R.string.github_token)) },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = token.isNotBlank(),
                onClick = {
                    if (token.isBlank()) {
                        onExitApp()
                    } else {
                        scope.launch {
                            onTokenEntered(token)
                            delay(500)
                            onRestartApp()
                        }
                    }
                }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onExitApp()
            }) {
                Text(text = stringResource(R.string.exit))
            }
        }
    )
}
