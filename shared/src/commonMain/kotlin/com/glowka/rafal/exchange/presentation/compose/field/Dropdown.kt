package com.glowka.rafal.exchange.presentation.compose.field

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.glowka.rafal.exchange.presentation.style.Colors

@Composable
fun Dropdown(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  OutlinedButton(
    modifier = modifier,
    onClick = onClick,
  ) {
    Text(
      modifier = Modifier.wrapContentSize(),
      text = text
    )
    Icon(
      imageVector = Icons.Filled.ArrowDropDown,
      contentDescription = null,
      modifier = Modifier.height(40.dp).align(Alignment.CenterVertically),
      tint = Colors.black
    )
  }
}