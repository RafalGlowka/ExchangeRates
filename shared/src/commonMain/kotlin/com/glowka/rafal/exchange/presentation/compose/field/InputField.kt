package com.glowka.rafal.exchange.presentation.compose.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import co.touchlab.kermit.Logger
import com.glowka.rafal.exchange.MainRes
import com.glowka.rafal.exchange.presentation.style.Colors
import com.glowka.rafal.exchange.presentation.validator.Validator

@Composable
fun <T> InputField(
  onTextChange: (T) -> Unit,
  validator: Validator<String, T>,
  modifier: Modifier = Modifier.fillMaxWidth(),
) {
  var textValue by remember {
    mutableStateOf("")
  }
//  val kc = LocalSoftwareKeyboardController.current
  TextField(
    modifier = modifier,
    value = textValue,
    onValueChange = { newText ->
      validator.validate(newText)
        .onSuccess { newValue ->
          textValue = newText
          onTextChange(newValue)
        }.onFailure { error ->
          Logger.d("Input error $error")
        }
    },
    label = {
      Text(
        text = MainRes.string.insert_value
      )
    },
    colors = TextFieldDefaults.textFieldColors(
      backgroundColor = Colors.white_transparent
    ),
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
  )
}
