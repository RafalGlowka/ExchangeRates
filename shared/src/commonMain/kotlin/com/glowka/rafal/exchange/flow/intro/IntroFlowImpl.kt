package com.glowka.rafal.exchange.flow.intro

import com.glowka.rafal.exchange.flow.exchange.MainFlow
import com.glowka.rafal.exchange.flow.exchange.MainFlowResult
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToFlowInterface
import com.glowka.rafal.exchange.presentation.architecture.BaseFlow
import com.glowka.rafal.exchange.presentation.architecture.Screen
import com.glowka.rafal.exchange.presentation.utils.EmptyParam

class IntroFlowImpl(
  val mainFlow: MainFlow,
) :
  BaseFlow<EmptyParam, IntroResult>(flowScopeName = IntroFlow.SCOPE_NAME), IntroFlow {

  override fun onStart(param: EmptyParam): Screen<*, *> {
    showScreen(
      screen = IntroFlow.Screens.Start,
      onShowInput = null,
      onScreenOutput = ::onStartEvent
    )
    return IntroFlow.Screens.Start
  }

  private fun onStartEvent(event: IntroViewModelToFlowInterface.Output) {
    when (event) {
      is IntroViewModelToFlowInterface.Output.Finished -> startMainFlow()
    }
  }

  private fun startMainFlow() {
    mainFlow.start(screenNavigator = screenNavigator, param = EmptyParam.EMPTY) { result ->
      when (result) {
        MainFlowResult.Terminated -> finish(result = IntroResult.Terminated)
      }
    }
  }
}
