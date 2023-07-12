package com.glowka.rafal.exchange.presentation.compose.field

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.dp
import com.glowka.rafal.exchange.MainRes
import com.glowka.rafal.exchange.presentation.style.Colors
import io.github.skeptick.libres.compose.painterResource

@Composable
fun BackIcon(
  modifier: Modifier = Modifier,
  onBackClick: () -> Unit
) {
  IconButton(
    modifier = modifier
      .wrapContentSize()
      .padding(10.dp),
    onClick = onBackClick
  ) {
    Icon(
      modifier = Modifier
        .width(44.dp)
        .height(44.dp)
        .padding(10.dp)
        .drawBehind {
          drawCircle(
            color = Colors.buttonBack,
            radius = 22.dp.toPx()
          )
        },
      painter = painterResource(MainRes.image.ic_back),
      contentDescription = MainRes.string.content_description_back_button
    )
  }
}