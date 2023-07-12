package com.glowka.rafal.exchange.flow.exchange

import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToFlowInterface
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerScreenDialogStructure
import com.glowka.rafal.exchange.flow.exchange.main.MainScreenStructure
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.architecture.Flow
import com.glowka.rafal.exchange.presentation.architecture.Screen
import com.glowka.rafal.exchange.presentation.architecture.ScreenDialog
import com.glowka.rafal.exchange.presentation.architecture.ScreenDialogStructure
import com.glowka.rafal.exchange.presentation.architecture.ScreenInput
import com.glowka.rafal.exchange.presentation.architecture.ScreenOutput
import com.glowka.rafal.exchange.presentation.architecture.ScreenStructure
import com.glowka.rafal.exchange.presentation.utils.EmptyParam

sealed class MainFlowResult {
  object Terminated : MainFlowResult()
}

@Suppress("MaxLineLength")
interface MainFlow : Flow<EmptyParam, MainFlowResult> {

  companion object {
    const val SCOPE_NAME = "Main"
  }

  sealed class Screens<INPUT : ScreenInput, OUTPUT : ScreenOutput>(
    screenTag: String,
    screenStructure: ScreenStructure<INPUT, OUTPUT, *, *>
  ) : Screen<INPUT, OUTPUT>(
    flowScopeName = SCOPE_NAME,
    screenTag = screenTag,
    screenStructure = screenStructure,
  ) {

    object Main :
      Screens<MainViewModelToFlowInterface.Input, MainViewModelToFlowInterface.Output>(
        screenTag = "Main",
        screenStructure = MainScreenStructure,
      )
  }

  sealed class ScreenDialogs<INPUT : ScreenInput, OUTPUT : ScreenOutput>(
    screenTag: String,
    screenStructure: ScreenDialogStructure<INPUT, OUTPUT, *, *>
  ) :
    ScreenDialog<INPUT, OUTPUT>(
      flowScopeName = SCOPE_NAME,
      screenTag = screenTag,
      screenStructure = screenStructure,
    ) {
    object CurrencyPicker :
      ScreenDialogs<CurrencyPickerDialogViewModelToFlowInterface.Input, CurrencyPickerDialogViewModelToFlowInterface.Output>(
        screenTag = "Currency",
        screenStructure = CurrencyPickerScreenDialogStructure,
      )
  }
}
