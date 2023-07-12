package com.glowka.rafal.exchange.flow.exchange

import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToFlowInterface
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.architecture.BaseFlow
import com.glowka.rafal.exchange.presentation.architecture.Screen
import com.glowka.rafal.exchange.presentation.architecture.sendInput
import com.glowka.rafal.exchange.presentation.utils.EmptyParam

class MainFlowImpl :
  BaseFlow<EmptyParam, MainFlowResult>(flowScopeName = MainFlow.SCOPE_NAME), MainFlow {

  override fun onStart(param: EmptyParam): Screen<*, *> {
    return showScreen(
      screen = MainFlow.Screens.Main,
      onShowInput = null,
      replace = true,
    ) { output ->
      when (output) {
        MainViewModelToFlowInterface.Output.Back -> finish(result = MainFlowResult.Terminated)
        is MainViewModelToFlowInterface.Output.ChangeCurrency -> showCurrencyPickerDialog(output)
      }
    }
  }

  private fun showCurrencyPickerDialog(event: MainViewModelToFlowInterface.Output.ChangeCurrency) {
    showScreenDialog(
      screen = MainFlow.ScreenDialogs.CurrencyPicker,
      onShowInput = CurrencyPickerDialogViewModelToFlowInterface.Input.Init(selected = event.currency),
      onOutput = { output ->
        when (output) {
          CurrencyPickerDialogViewModelToFlowInterface.Output.Back ->
            hideScreenDialog(MainFlow.ScreenDialogs.CurrencyPicker)

          is CurrencyPickerDialogViewModelToFlowInterface.Output.Picked -> {
            hideScreenDialog(MainFlow.ScreenDialogs.CurrencyPicker)
            sendInput(
              screen = MainFlow.Screens.Main,
              input = MainViewModelToFlowInterface.Input.ChangeCurrency(
                currency = output.picked
              )
            )
          }
        }
      }
    )
  }
}