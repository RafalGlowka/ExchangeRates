package com.glowka.rafal.exchange.presentation.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) = MaterialTheme(
  typography = getAppTypography()
) {
  content()
}