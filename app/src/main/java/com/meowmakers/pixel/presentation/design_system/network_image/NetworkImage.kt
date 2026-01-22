package com.meowmakers.pixel.presentation.design_system.network_image

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

typealias LoadImageCallback = (String) -> Bitmap?

@Composable
fun NetworkImage(modifier: Modifier, url: String, loadImage: LoadImageCallback) {
    val imageState = produceState<Bitmap?>(initialValue = null, url) {
        value = loadImage(url)
    }

    val bitmap = imageState.value

    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                    RoundedCornerShape(8.dp)
                ),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                    RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp), strokeWidth = 2.dp)
        }
    }
}
