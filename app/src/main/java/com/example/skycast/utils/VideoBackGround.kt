package com.example.skycast.utils

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun VideoBackground(
    videoResId: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    looping: Boolean = true
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val dataSource = RawResourceDataSource(context)
            val mediaItem = MediaItem.fromUri(
                RawResourceDataSource.buildRawResourceUri(videoResId)
            )
            setMediaItem(mediaItem)
            repeatMode = if (looping) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        },
        modifier = modifier.fillMaxSize(),
        update = { view ->
            view.player = exoPlayer
        }
    )
}