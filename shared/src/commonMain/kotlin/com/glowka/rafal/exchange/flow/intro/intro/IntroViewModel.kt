package com.glowka.rafal.exchange.flow.intro.intro

import androidx.compose.material.SnackbarDuration
import co.touchlab.kermit.Logger
import com.glowka.rafal.exchange.MainRes
import com.glowka.rafal.exchange.domain.repository.ExchangeRepository
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToFlowInterface.Input
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToFlowInterface.Output
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToViewInterface.State
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToViewInterface.ViewEvents
import com.glowka.rafal.exchange.presentation.architecture.BaseViewModel
import com.glowka.rafal.exchange.presentation.architecture.ScreenInput
import com.glowka.rafal.exchange.presentation.architecture.ScreenOutput
import com.glowka.rafal.exchange.presentation.architecture.ViewEvent
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.architecture.ViewState
import com.glowka.rafal.exchange.presentation.architecture.launch
import com.glowka.rafal.exchange.presentation.compose.SnackbarEvent
import com.glowka.rafal.exchange.presentation.compose.ViewStateEvent
import com.glowka.rafal.exchange.presentation.compose.idle
import com.glowka.rafal.exchange.presentation.compose.trigger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface IntroViewModelToFlowInterface : ViewModelToFlowInterface<Input, Output> {
  sealed interface Input : ScreenInput
  sealed interface Output : ScreenOutput {
    object Finished : Output
  }
}

interface IntroViewModelToViewInterface : ViewModelToViewInterface<State, ViewEvents> {
  data class State(
    val snackbarEvent: ViewStateEvent<SnackbarEvent<ViewEvents>>
  ) : ViewState

  sealed interface ViewEvents : ViewEvent {
    object ActiveScreen : ViewEvents
    object SnackbarShown : ViewEvents
    object SnackbarRetry : ViewEvents
  }
}

class IntroViewModelImpl(
  private val exchangeRepository: ExchangeRepository,
) : IntroViewModelToFlowInterface, IntroViewModelToViewInterface,
  BaseViewModel<Input, Output, State, ViewEvents>(
    backPressedOutput = null
  ) {

  private val log = Logger.withTag("IntroViewModel")

  private var animation = false
  private var data = false

  private fun loadDataFromStorageOrGetFromBackend() {
    launch {
      exchangeRepository.initWithLocalStorage()
        .recover { false }
        .mapCatching { result ->
          log.d("local storage init: $result")
          if (!result) {
            exchangeRepository.reloadFromBackend()
              .map { list ->
                log.d("received list: $list")
                list.isNotEmpty()
              }
              .getOrThrow()
          } else {
            true
          }
        }
        .onFailure { error ->
          showError(error.message ?: MainRes.string.error_initialization)
        }
        .onSuccess { result ->
          data = result
          log.d("onSuccess $result, $animation")
          if (result) {
            if (animation) showNext()
          } else {
            showError(MainRes.string.error_general)
          }
        }
    }
  }

  private fun showError(message: String) {
    log.e(message)
    viewState.update { state ->
      state.copy(
        snackbarEvent = trigger(
          SnackbarEvent(
            message = message,
            onShowEvent = ViewEvents.SnackbarShown,
            duration = SnackbarDuration.Indefinite,
            actionLabel = MainRes.string.retry,
            actionEvent = ViewEvents.SnackbarRetry
          )
        )
      )
    }
  }

  private fun showNext() {
    sendOutput(output = Output.Finished)
  }

  override val viewState = MutableStateFlow(State(snackbarEvent = idle()))

  override fun onViewEvent(event: ViewEvents) {
    when (event) {
      ViewEvents.ActiveScreen -> {
        loadDataFromStorageOrGetFromBackend()

        launch {
          delay(MIN_SHOW_TIME_MS)
          animation = true
          log.d("animation $data")
          if (data) showNext()
        }
      }

      ViewEvents.SnackbarShown -> {
        viewState.update { state -> state.copy(snackbarEvent = idle()) }
      }

      ViewEvents.SnackbarRetry -> {
        loadDataFromStorageOrGetFromBackend()
      }
    }
  }

  companion object {
    const val MIN_SHOW_TIME_MS = 4000L
  }
}
