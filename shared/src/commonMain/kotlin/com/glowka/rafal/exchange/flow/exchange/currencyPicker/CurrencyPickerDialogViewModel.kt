package com.glowka.rafal.exchange.flow.exchange.currencyPicker

import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.repository.ExchangeRepository
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToFlowInterface.Input
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToFlowInterface.Output
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToViewInterface.ViewEvents
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToViewInterface.ViewState
import com.glowka.rafal.exchange.presentation.architecture.BaseViewModel
import com.glowka.rafal.exchange.presentation.architecture.ScreenInput
import com.glowka.rafal.exchange.presentation.architecture.ScreenOutput
import com.glowka.rafal.exchange.presentation.architecture.ViewEvent
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToViewInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface CurrencyPickerDialogViewModelToFlowInterface : ViewModelToFlowInterface<Input, Output> {

  sealed interface Input : ScreenInput {
    data class Init(val selected: Currency) : Input
  }

  sealed interface Output : ScreenOutput {
    data class Picked(val picked: Currency) : Output
    object Back : Output
  }
}

interface CurrencyPickerDialogViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  sealed class ViewEvents : ViewEvent {
    data class OnItemClicked(val position: Int) : ViewEvents()
    object Back : ViewEvents()
  }

  data class ViewState(
    val selectedIndex: Int = 0,
    val items: List<Currency> = emptyList(),
  ) : com.glowka.rafal.exchange.presentation.architecture.ViewState
}

class CurrencyPickerDialogViewModelImpl(
  val exchangeRepository: ExchangeRepository,
) : CurrencyPickerDialogViewModelToFlowInterface,
  CurrencyPickerDialogViewModelToViewInterface,
  BaseViewModel<Input, Output, ViewState, ViewEvents>(
    backPressedOutput = Output.Back
  ) {
  override val viewState = exchangeRepository.rates.value.let { rates ->
    MutableStateFlow(
      ViewState(
        selectedIndex = rates.indexOfFirst { rate -> rate.currency.isUSD() },
        items = rates.map { rate ->
          rate.currency
        },
      )
    )
  }

  override fun onInput(input: Input) {
    when (input) {
      is Input.Init -> {
        viewState.update { state ->
          state.copy(
            selectedIndex = exchangeRepository.rates.value.indexOfFirst { rate ->
              rate.currency == input.selected
            }
          )
        }
      }
    }
  }

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      is ViewEvents.OnItemClicked -> {
        sendOutput(
          output = Output.Picked(
            picked = viewState.value.items[event.position],
          )
        )
      }

      ViewEvents.Back -> {
        sendOutput(output = Output.Back)
      }
    }
  }
}
