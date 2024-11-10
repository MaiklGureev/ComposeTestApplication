package com.example.composegitapp.ui.design_system

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun DataTextView(
    key: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(
            fontWeight = FontWeight.Bold,
            fontStyle = MaterialTheme.typography.titleMedium.fontStyle)) {
            append(key)
        }
        withStyle(style = SpanStyle(fontStyle = MaterialTheme.typography.bodyMedium.fontStyle)) {
            append(value)
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Start,
    )
}
@Preview
@Composable
private fun PreviewTextDataView(): Unit {
    DataTextView(key = "NAME: ", value = "Kristofer")
}