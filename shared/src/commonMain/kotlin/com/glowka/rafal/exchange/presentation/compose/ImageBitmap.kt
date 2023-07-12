package com.glowka.rafal.exchange.presentation.compose

import androidx.compose.ui.graphics.ImageBitmap

expect fun createImageBitmap(data: ByteArray): ImageBitmap?
