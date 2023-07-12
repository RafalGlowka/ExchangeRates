package com.glowka.rafal.exchange.presentation.flow.intro

import androidx.compose.material.SnackbarDuration
import app.cash.turbine.test
import com.glowka.rafal.exchange.data.repository.FakeExchangeRepository
import com.glowka.rafal.exchange.domain.model.currencyUSDRate
import com.glowka.rafal.exchange.domain.utils.mockLogs
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelImpl
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToFlowInterface
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.ViewModelSpec
import com.glowka.rafal.exchange.presentation.compose.SnackbarEvent
import com.glowka.rafal.exchange.presentation.compose.ViewStateEvent
import com.glowka.rafal.exchange.presentation.utils.testScreenOutputs
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf
import java.io.IOException

class IntroViewModelSpec : ViewModelSpec() {

  init {
    mockLogs()
    val exchangeRepository = FakeExchangeRepository()
    fun createViewModel(): IntroViewModelImpl {
      val viewModel = IntroViewModelImpl(
        exchangeRepository = exchangeRepository,
      )
      return viewModel
    }

    describe("screen activation") {

      it("do not call backend, but waits 4 seconds if list was initialized from local database") {
        val viewModel = createViewModel()
        viewModel.testScreenOutputs {
          exchangeRepository.setInitResponse(Result.success(true))
          exchangeRepository.setReloadResponse(Result.failure(IOException("connection error")))

          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          expectNoEvents()
          advanceTimeBy(3000)
          expectNoEvents()
          advanceTimeBy(1100)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Output.Finished
        }
      }

      it("call backend if not initialized from local database") {
        val viewModel = createViewModel()
        exchangeRepository.setInitResponse(Result.success(false))
        exchangeRepository.setReloadResponse(Result.success(listOf(currencyUSDRate())), 10000)
        viewModel.testScreenOutputs {
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          advanceTimeBy(5000)
          expectNoEvents()
          advanceTimeBy(6000)
          awaitItem() shouldBe IntroViewModelToFlowInterface.Output.Finished
        }
      }

      it("shows error if initialization and calling backend fails") {
        val viewModel = createViewModel()
        exchangeRepository.setInitResponse(Result.failure(IllegalStateException("initialization error")))
        exchangeRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }
        }
      }

      it("call repository again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        exchangeRepository.setInitResponse(Result.failure(IllegalStateException("initialization error")))
        exchangeRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }

          exchangeRepository.setInitResponse(Result.success(true))
          exchangeRepository.initializing.test {
            viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.SnackbarRetry)
            awaitItem()
          }

          expectNoEvents()
        }
      }

      it("shows error if getting data from backend fails") {
        val viewModel = createViewModel()
        exchangeRepository.setInitResponse(Result.success(false))
        exchangeRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }
        }
      }

      it("call backend again if retry action on snackbar is clicked") {
        val viewModel = createViewModel()
        exchangeRepository.setInitResponse(Result.success(false))
        exchangeRepository.setReloadResponse(Result.failure(IOException("connection error")))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "connection error"
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }

          exchangeRepository.setReloadResponse(Result.success(listOf(currencyUSDRate())))
          exchangeRepository.reloading.test {
            viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.SnackbarRetry)
            awaitItem()
          }

          expectNoEvents()
        }
      }

      it("shows error with retry if response from backend is empty") {
        val viewModel = createViewModel()
        exchangeRepository.setInitResponse(Result.success(false))
        exchangeRepository.setReloadResponse(Result.success(emptyList()))

        viewModel.viewState.test {
          awaitItem()
          viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
          awaitItem().run {
            snackbarEvent.shouldBeTypeOf<ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>>().data.run {
              message shouldBe "Something went wrong."
              duration shouldBe SnackbarDuration.Indefinite
              actionLabel shouldBe "Retry"
            }
          }

          exchangeRepository.setReloadResponse(Result.success(listOf(currencyUSDRate())))
          exchangeRepository.reloading.test {
            viewModel.onViewEvent(IntroViewModelToViewInterface.ViewEvents.SnackbarRetry)
            awaitItem()
          }

          expectNoEvents()
        }
      }
    }
  }
}