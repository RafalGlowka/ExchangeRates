package com.glowka.rafal.exchange.presentation.flow.exchange.currencyPicker

import app.cash.turbine.test
import com.glowka.rafal.exchange.data.repository.FakeExchangeRepository
import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.currencyUSDRate
import com.glowka.rafal.exchange.domain.utils.mockLogs
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelImpl
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToFlowInterface
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.ViewModelSpec
import com.glowka.rafal.exchange.presentation.utils.testScreenOutputs
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe

class CurrencyPickerDialogViewModelSpec : ViewModelSpec() {

  init {
    mockLogs()

    val exchangeRepository = FakeExchangeRepository()

    runBlocking {
      val usd = currencyUSDRate()
      val pln = currencyUSDRate(code = "PLN", usdRate = 2.0)
      exchangeRepository.setReloadResponse(Result.success(listOf(usd, pln)))
      exchangeRepository.reloadFromBackend()
    }

    fun createViewModel() = CurrencyPickerDialogViewModelImpl(
      exchangeRepository = exchangeRepository,
    )

    describe("initialization") {

      val viewModel = createViewModel()

      it("sets selected index based on init input") {
        viewModel.viewState.test {
          awaitItem()

          expectNoEvents()

          viewModel.onInput(CurrencyPickerDialogViewModelToFlowInterface.Input.Init(selected = Currency("PLN")))

          awaitItem().selectedIndex shouldBe 1
        }
      }
    }

    describe("View events") {

      it("emits back output on back view event") {
        val viewModel = createViewModel()
        viewModel.testScreenOutputs {
          expectNoEvents()
          viewModel.onViewEvent(CurrencyPickerDialogViewModelToViewInterface.ViewEvents.Back)
          awaitItem() shouldBe CurrencyPickerDialogViewModelToFlowInterface.Output.Back
        }
      }

      it("emits change currency output on change currency event") {
        val viewModel = createViewModel()
        viewModel.testScreenOutputs {
          expectNoEvents()
          viewModel.onViewEvent(CurrencyPickerDialogViewModelToViewInterface.ViewEvents.OnItemClicked(1))
          awaitItem() shouldBe CurrencyPickerDialogViewModelToFlowInterface.Output.Picked(
            picked = Currency("PLN")
          )
        }
      }
    }
  }
}