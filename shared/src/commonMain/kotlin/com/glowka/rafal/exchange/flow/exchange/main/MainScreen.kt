package com.glowka.rafal.exchange.flow.exchange.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glowka.rafal.exchange.domain.model.calculator.CurrencyConversion
import com.glowka.rafal.exchange.presentation.compose.field.BackIcon
import com.glowka.rafal.exchange.presentation.compose.field.Dropdown
import com.glowka.rafal.exchange.presentation.compose.field.InputField
import com.glowka.rafal.exchange.presentation.compose.getAppTypography
import com.glowka.rafal.exchange.presentation.compose.navigationBarsPadding
import com.glowka.rafal.exchange.presentation.compose.systemBarsPadding
import com.glowka.rafal.exchange.presentation.validator.string.MoneyValidator

private val moneyValidator = MoneyValidator()

@Composable
internal fun MainScreen(
  viewState: MainViewModelToViewInterface.ViewState,
  onViewEvent: (MainViewModelToViewInterface.ViewEvents) -> Unit
) {
  MaterialTheme(
    typography = getAppTypography()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .navigationBarsPadding(),
      horizontalAlignment = Alignment.End,
    ) {
      BackIcon(
        modifier = Modifier.align(Alignment.Start),
        onBackClick = { onViewEvent(MainViewModelToViewInterface.ViewEvents.Close) }
      )
      Row(
        modifier = Modifier.fillMaxWidth(),
      ) {
        InputField(
          modifier = Modifier.padding(start = 10.dp, end = 10.dp).weight(1.0f),
          validator = moneyValidator,
          onTextChange = { newValue ->
            onViewEvent(
              MainViewModelToViewInterface.ViewEvents.OnInputChange(
                value = newValue
              )
            )
          },
        )
        Dropdown(
          modifier = Modifier.wrapContentSize(),
          text = viewState.currency.code,
          onClick = {
            onViewEvent(MainViewModelToViewInterface.ViewEvents.ChangeCurrency)
          }
        )
      }
      LazyVerticalGrid(
        modifier = Modifier.padding(10.dp),
        columns = GridCells.Adaptive(minSize = 150.dp)
      ) {
        items(viewState.conversions.size) { index ->
          val item = viewState.conversions[index]
            ConversionItem(item = item)
        }
      }
    }
  }
}

@Composable
private fun ConversionItem(item: CurrencyConversion) {
  Text(
    modifier = Modifier.padding(10.dp),
    text = "${item.conversion}\n${item.currency.code}",
    textAlign = TextAlign.End
  )
}