package com.meowmakers.pixel.presentation.screens.top_users.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meowmakers.pixel.Fixtures
import com.meowmakers.pixel.PixelApplication
import com.meowmakers.pixel.domain.entities.TopUser
import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.load
import com.meowmakers.pixel.presentation.design_system.network_image.LoadImageCallback
import com.meowmakers.pixel.presentation.design_system.network_image.NetworkImage
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersScreenState

@Composable
fun LoadedContent(
    modifier: Modifier,
    items: List<TopUser>,
    loadImage: LoadImageCallback
) {
    LazyColumn(modifier.testTag("loaded_content")) {
        items(count = items.size, key = { items[it].id }, itemContent = { index ->
            var isLiked by remember { mutableStateOf(false) }
            val item = items[index]
            Spacer(Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .testTag("user_item")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(Modifier.width(16.dp))

                    NetworkImage(
                        modifier = Modifier,
                        url = item.profileImageLink,
                        loadImage = loadImage
                    )

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )

                        Text(
                            text = "${item.reputation}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    FilledIconToggleButton(checked = isLiked, onCheckedChange = { isLiked = it }) {
                        if (isLiked) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description"
                            )
                        } else {
                            Icon(
                                Icons.Outlined.FavoriteBorder,
                                contentDescription = "Localized description"
                            )
                        }
                    }
                }
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoadedContent(
) {
    val users = Fixtures.topUsersJson.load<TopUsers>()

    PixelApplication { innerPadding ->
        TopUsersScreenContent(
            modifier = Modifier.padding(innerPadding),
            state = TopUsersScreenState.Loaded(users),
            onRetry = {},
            loadImage = { null }
        )
    }
}