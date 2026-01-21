package com.meowmakers.pixel.presentation.screens.top_users.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.meowmakers.pixel.Fixtures
import com.meowmakers.pixel.PixelApplication
import com.meowmakers.pixel.domain.entities.TopUser
import com.meowmakers.pixel.domain.entities.TopUsers
import com.meowmakers.pixel.load
import com.meowmakers.pixel.presentation.screens.top_users.view_models.TopUsersScreenState

@Composable
fun LoadedContent(modifier: Modifier, items: List<TopUser>) {
    LazyColumn(modifier.testTag("loaded_content")) {
        items(count = items.size, key = { items[it].id }, itemContent = { index ->
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

                    Box(
                        Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(
                                BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                                RoundedCornerShape(8.dp)
                            )
                    )

                    Spacer(Modifier.width(12.dp))

                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "${item.reputation}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
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
            onRetry = {}
        )
    }
}