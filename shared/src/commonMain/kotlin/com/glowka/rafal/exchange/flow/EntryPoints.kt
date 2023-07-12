package com.glowka.rafal.exchange.flow

import com.glowka.rafal.exchange.di.inject
import com.glowka.rafal.exchange.flow.intro.IntroFlow
import com.glowka.rafal.exchange.flow.intro.IntroResult
import com.glowka.rafal.exchange.presentation.architecture.ScreenNavigator
import com.glowka.rafal.exchange.presentation.utils.EmptyParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

public fun startMainFlow() {
  val navigator: ScreenNavigator by inject()
  val introFlow: IntroFlow by inject()
  introFlow.start(
    screenNavigator = navigator,
    param = EmptyParam.EMPTY,
  ) { result ->
    when (result) {
      IntroResult.Terminated -> navigator.exit()
    }
  }
}