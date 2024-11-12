package com.example.composegitapp.ui.design_system

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.composegitapp.common.extension.ContextExt.hideKeyboard
import com.gureev.ComposeAppGitHub.R

@Composable
fun SearchTextField(
    label: String = stringResource(id = R.string.search),
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val enterWords = stringResource(id = R.string.enter_words)
    val context = LocalContext.current

    OutlinedTextField(
        value = query,
        onValueChange = { newQuery ->
            onQueryChange(newQuery)
        },
        modifier = modifier
            .fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(enterWords) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = onSearch) { // Иконка поиска
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                context.hideKeyboard()
                onSearch()
            }
        )
    )
}

@Preview
@Composable
private fun PreviewSearchTextField() {
    SearchTextField(
        label = "Search",
        query = "",
        onQueryChange = { _ -> },
        onSearch = { -> },
        modifier = Modifier
    )
}