package com.glowka.rafal.exchange.flow.exchange.main

import co.touchlab.kermit.Logger
import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.calculator.CurrencyCalculatorState
import com.glowka.rafal.exchange.domain.model.calculator.CurrencyConversion
import com.glowka.rafal.exchange.domain.repository.ExchangeRepository
import com.glowka.rafal.exchange.domain.usecase.CalculateConversionsUseCase
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToFlowInterface.Input
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToFlowInterface.Output
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToViewInterface.ViewEvents
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToViewInterface.ViewState
import com.glowka.rafal.exchange.kotlin.mapState
import com.glowka.rafal.exchange.presentation.architecture.BaseViewModel
import com.glowka.rafal.exchange.presentation.architecture.ScreenInput
import com.glowka.rafal.exchange.presentation.architecture.ScreenOutput
import com.glowka.rafal.exchange.presentation.architecture.ViewEvent
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.architecture.launch
import com.glowka.rafal.exchange.presentation.utils.DelayedAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface MainViewModelToFlowInterface : ViewModelToFlowInterface<Input, Output> {

  sealed interface Input : ScreenInput {
    data class ChangeCurrency(val currency: Currency) : Input
  }

  sealed interface Output : ScreenOutput {
    object Back : Output
    data class ChangeCurrency(var currency: Currency) : Output
  }
}

interface MainViewModelToViewInterface : ViewModelToViewInterface<ViewState, ViewEvents> {
  data class ViewState(
    val value: Double = 0.0,
    val currency: Currency = Currency("USD"),
    val conversions: List<CurrencyConversion> = emptyList()
  ) : com.glowka.rafal.exchange.presentation.architecture.ViewState

  sealed class ViewEvents : ViewEvent {

    data class OnInputChange(val value: Double) : ViewEvents()
    object Close : ViewEvents()
    object ChangeCurrency : ViewEvents()
  }
}

class MainViewModelImpl(
  exchangeRepository: ExchangeRepository,
  private val calculateConversionsUseCase: CalculateConversionsUseCase,
) : MainViewModelToViewInterface, MainViewModelToFlowInterface,
  BaseViewModel<Input, Output, ViewState, ViewEvents>(
    backPressedOutput = Output.Back
  ) {

  private val log = Logger.withTag("MainViewModel")

  private val domainState = MutableStateFlow(CurrencyCalculatorState(rates = exchangeRepository.rates.value))
  override val viewState = domainState.mapState { state ->
    println("Update view state ${state.rates.size}")
    val result = state.toViewState()
    println("updated ${result.conversions.size}")
    result
  }

  private val inputAction = DelayedAction(viewModelScope)

  init {
    exchangeRepository.rates.onEach { rates ->
      domainState.update { state ->
        state.copy(rates = rates)
      }
      println("update rates: ${rates.size}")
    }.launchIn(viewModelScope)
    println("Subscribed")
  }

  override fun onInput(input: Input) {
    when (input) {
      is Input.ChangeCurrency -> {
        domainState.update { state ->
          state.copy(
            currency = input.currency,
          )
        }
      }
    }
  }

  private fun CurrencyCalculatorState.toViewState(): ViewState {
    var conversions = emptyList<CurrencyConversion>()

    calculateConversionsUseCase(this)
      .onSuccess { result ->
        println("converted: ${result.size}")
        conversions = result
      }
      .onFailure { error ->
        println("error : $error")
        log.e("$error")
      }

    return ViewState(
      value = value,
      currency = currency,
      conversions = conversions
    )
  }

  override fun onViewEvent(event: ViewEvents) {
    log.d("onEvent $event")
    when (event) {
      is ViewEvents.OnInputChange -> {
        inputAction.replaceAction(delay = RECALCULATION_DELAY) {
          domainState.update { state ->
            state.copy(
              value = event.value,
            )
          }
        }
      }

      ViewEvents.Close -> {
        launch {
          sendOutput(output = Output.Back)
        }
      }

      ViewEvents.ChangeCurrency -> {
        launch {
          sendOutput(output = Output.ChangeCurrency(currency = viewState.value.currency))
        }
      }
    }
  }

  companion object {
    const val RECALCULATION_DELAY = 800L
  }
}
