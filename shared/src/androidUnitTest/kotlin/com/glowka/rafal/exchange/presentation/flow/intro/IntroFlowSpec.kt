package com.glowka.rafal.exchange.presentation.flow.intro

import app.cash.turbine.test
import com.glowka.rafal.exchange.flow.exchange.MainFlow
import com.glowka.rafal.exchange.flow.exchange.MainFlowResult
import com.glowka.rafal.exchange.flow.intro.IntroFlow
import com.glowka.rafal.exchange.flow.intro.IntroFlowImpl
import com.glowka.rafal.exchange.flow.intro.IntroResult
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.FlowSpec
import com.glowka.rafal.exchange.presentation.utils.EmptyParam
import com.glowka.rafal.exchange.presentation.utils.FakeFlow
import com.glowka.rafal.exchange.presentation.utils.FakeScreenNavigator
import com.glowka.rafal.exchange.presentation.utils.emitScreenOutput
import com.glowka.rafal.exchange.presentation.utils.shouldBeNavigationPushToScreen
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class IntroFlowSpec : FlowSpec() {

  init {
    val mainFlow = object : MainFlow, FakeFlow<EmptyParam, MainFlowResult>() {}

    val flow = IntroFlowImpl(
      mainFlow = mainFlow
    )

    val navigator = FakeScreenNavigator()

    it("shows intro screen after which it starts main flow and terminates with main flow") {
      var flowFinished = false
      mainFlow.startEvents.test {
        val mainFlowStartEvents = this
        navigator.navigationEvents.test {

          flow.start(navigator, EmptyParam.EMPTY) { event ->
            when (event) {
              IntroResult.Terminated -> flowFinished = true
            }
          }

          mainFlowStartEvents.expectNoEvents()
          awaitItem()
            .shouldBeNavigationPushToScreen(
              screen = IntroFlow.Screens.Start,
              onShowInput = null,
            )
            .emitScreenOutput(IntroViewModelToFlowInterface.Output.Finished)

          mainFlowStartEvents.awaitItem().run {
            param shouldBe EmptyParam.EMPTY
            flowFinished.shouldBeFalse()
            onResult(MainFlowResult.Terminated)
            flowFinished.shouldBeTrue()
          }
        }
      }
    }
  }
}