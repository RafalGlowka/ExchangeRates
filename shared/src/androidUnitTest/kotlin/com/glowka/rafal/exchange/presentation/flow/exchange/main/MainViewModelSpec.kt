package com.glowka.rafal.exchange.presentation.flow.exchange.main

import app.cash.turbine.test
import com.glowka.rafal.exchange.data.repository.FakeExchangeRepository
import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.calculator.CurrencyConversion
import com.glowka.rafal.exchange.domain.model.currencyUSDRate
import com.glowka.rafal.exchange.domain.usecase.CalculateConversionsUseCaseImpl
import com.glowka.rafal.exchange.domain.utils.mockLogs
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelImpl
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToFlowInterface
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.ViewModelSpec
import com.glowka.rafal.exchange.presentation.utils.testScreenOutputs
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class MainViewModelSpec : ViewModelSpec() {

  init {
    mockLogs()

    val exchangeRepository = FakeExchangeRepository()
    val calculateConversionsUseCase = CalculateConversionsUseCaseImpl()

    fun createViewModel() = MainViewModelImpl(
      exchangeRepository = exchangeRepository,
      calculateConversionsUseCase = calculateConversionsUseCase,
    )

    val usd = currencyUSDRate()
    val pln = currencyUSDRate(code = "PLN", usdRate = 2.0)

    describe("initialization") {

      val viewModel = createViewModel()

      it("start observing album data when init is called") {
        viewModel.viewState.test {
          exchangeRepository.rates.value.size shouldBe 0
          awaitItem().conversions.shouldBeEmpty()

          expectNoEvents()
          exchangeRepository.setReloadResponse(Result.success(listOf(usd, pln)))
          exchangeRepository.reloadFromBackend()
          exchangeRepository.rates.value.size shouldBe 2

          awaitItem().conversions.shouldContainExactlyInAnyOrder(
            CurrencyConversion(Currency("USD"), 0.0),
            CurrencyConversion(Currency("PLN"), 0.0),
          )
        }
      }

      it("refreshes view state when rates are changed in repository") {
        viewModel.viewState.test {
          awaitItem().conversions.size shouldBe 2
          exchangeRepository.setReloadResponse(Result.success(listOf(usd)))
          exchangeRepository.reloadFromBackend()

          awaitItem().conversions.shouldContainExactlyInAnyOrder(
            CurrencyConversion(Currency("USD"), 0.0),
          )
        }
      }
    }

    describe("Flow inputs") {

      it("updates currency on change currency input") {
        val viewModel = createViewModel()
        exchangeRepository.setReloadResponse(Result.success(listOf(usd, pln)))
        exchangeRepository.reloadFromBackend()
        viewModel.viewState.test {
          awaitItem().currency shouldBe Currency("USD")

          viewModel.onInput(MainViewModelToFlowInterface.Input.ChangeCurrency(Currency("PLN")))

          awaitItem().currency shouldBe Currency("PLN")

          viewModel.onInput(MainViewModelToFlowInterface.Input.ChangeCurrency(Currency("USD")))

          awaitItem().currency shouldBe Currency("USD")
        }
      }
    }

    describe("View events") {

      exchangeRepository.setReloadResponse(Result.success(listOf(usd, pln)))
      exchangeRepository.reloadFromBackend()

      it("updates value with a delay") {
        val viewModel = createViewModel()
        viewModel.viewState.test {
          awaitItem().value shouldBe 0.toDouble()

          viewModel.onViewEvent(MainViewModelToViewInterface.ViewEvents.OnInputChange(1.0))
          expectNoEvents()

          advanceTimeBy(400)
          expectNoEvents()

          advanceTimeBy(410)
          awaitItem().value shouldBe 1.0
        }
      }

      it("restarts delay if new event appear during delay") {
        val viewModel = createViewModel()
        viewModel.viewState.test {
          awaitItem().value shouldBe 0.toDouble()

          viewModel.onViewEvent(MainViewModelToViewInterface.ViewEvents.OnInputChange(1.0))
          expectNoEvents()

          advanceTimeBy(400)
          viewModel.onViewEvent(MainViewModelToViewInterface.ViewEvents.OnInputChange(2.0))
          expectNoEvents()

          advanceTimeBy(410)
          expectNoEvents()

          advanceTimeBy(400)
          awaitItem().value shouldBe 2.0
        }
      }

      it("emits back output on back view event") {
        val viewModel = createViewModel()
        viewModel.testScreenOutputs {
          expectNoEvents()
          viewModel.onViewEvent(MainViewModelToViewInterface.ViewEvents.Close)
          awaitItem() shouldBe MainViewModelToFlowInterface.Output.Back
        }
      }

      it("emits change currency output on change currency event") {
        val viewModel = createViewModel()
        viewModel.testScreenOutputs {
          expectNoEvents()
          viewModel.onViewEvent(MainViewModelToViewInterface.ViewEvents.ChangeCurrency)
          awaitItem() shouldBe MainViewModelToFlowInterface.Output.ChangeCurrency(
            currency = viewModel.viewState.value.currency
          )
        }
      }
    }
  }
}