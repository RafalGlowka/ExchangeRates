package com.glowka.rafal.exchange.presentation.flow.exchange

import app.cash.turbine.test
import com.glowka.rafal.exchange.data.repository.FakeExchangeRepository
import com.glowka.rafal.exchange.domain.model.currency
import com.glowka.rafal.exchange.domain.repository.ExchangeRepository
import com.glowka.rafal.exchange.domain.usecase.CalculateConversionsUseCase
import com.glowka.rafal.exchange.domain.usecase.CalculateConversionsUseCaseImpl
import com.glowka.rafal.exchange.flow.exchange.MainFlow
import com.glowka.rafal.exchange.flow.exchange.MainFlowImpl
import com.glowka.rafal.exchange.flow.exchange.MainFlowResult
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToFlowInterface
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.FlowSpec
import com.glowka.rafal.exchange.presentation.architecture.businessFlow
import com.glowka.rafal.exchange.presentation.architecture.screen
import com.glowka.rafal.exchange.presentation.utils.EmptyParam
import com.glowka.rafal.exchange.presentation.utils.FakeScreenNavigator
import com.glowka.rafal.exchange.presentation.utils.emitScreenDialogOutput
import com.glowka.rafal.exchange.presentation.utils.emitScreenOutput
import com.glowka.rafal.exchange.presentation.utils.shouldBeHideScreenDialog
import com.glowka.rafal.exchange.presentation.utils.shouldBeNavigationReplaceWithScreen
import com.glowka.rafal.exchange.presentation.utils.shouldBeNavigationToScreenDialog
import io.kotest.matchers.booleans.shouldBeTrue
import org.koin.core.module.Module

class MainFlowSpec : FlowSpec() {

  init {

    fun createFlow() = MainFlowImpl()
    val navigator = FakeScreenNavigator()

    it("starts with main screen and terminated on main screen event back") {
      val flow = createFlow()
      var flowTerminated = false
      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) { result ->
          when (result) {
            MainFlowResult.Terminated -> flowTerminated = true
          }
        }
        awaitItem().shouldBeNavigationReplaceWithScreen(
          screen = MainFlow.Screens.Main,
          onShowInput = null,
        ).emitScreenOutput(MainViewModelToFlowInterface.Output.Back)
        flowTerminated.shouldBeTrue()
      }
    }

    it("Shows currency picker if currency was clicked on screen") {
      val flow = createFlow()
      val currency = currency()

      navigator.navigationEvents.test {
        flow.start(navigator, EmptyParam.EMPTY) {}
        awaitItem().shouldBeNavigationReplaceWithScreen(
          screen = MainFlow.Screens.Main,
          onShowInput = null,
        ).emitScreenOutput(MainViewModelToFlowInterface.Output.ChangeCurrency(currency))

        awaitItem().shouldBeNavigationToScreenDialog(
          screenDialog = MainFlow.ScreenDialogs.CurrencyPicker,
          param = CurrencyPickerDialogViewModelToFlowInterface.Input.Init(selected = currency)
        )
          .emitScreenDialogOutput(CurrencyPickerDialogViewModelToFlowInterface.Output.Picked(currency))

        awaitItem().shouldBeHideScreenDialog(
          screenDialog = MainFlow.ScreenDialogs.CurrencyPicker
        )
      }
    }
  }

  override fun Module.prepareKoinContext() {
    single<ExchangeRepository> {
      FakeExchangeRepository()
    }

    businessFlow(MainFlow.SCOPE_NAME) {
      screen(MainFlow.Screens.Main)
    }

    factory<CalculateConversionsUseCase> {
      CalculateConversionsUseCaseImpl()
    }
  }
}