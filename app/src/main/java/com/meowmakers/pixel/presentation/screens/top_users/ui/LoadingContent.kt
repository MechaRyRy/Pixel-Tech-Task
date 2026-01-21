package com.meowmakers.pixel.presentation.screens.top_users.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.meowmakers.pixel.PixelApplication

@Composable
fun LoadingContent(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingContent(
) {
    PixelApplication { innerPadding ->
        TopUsersScreen(
            modifier = Modifier.padding(innerPadding),
            state = TopUsersScreenState.Loading
        )
    }
}