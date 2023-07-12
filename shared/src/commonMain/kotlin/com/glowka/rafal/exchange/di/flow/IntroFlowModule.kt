package com.glowka.rafal.exchange.di.flow

import com.glowka.rafal.exchange.flow.intro.IntroFlow
import com.glowka.rafal.exchange.flow.intro.IntroFlowImpl
import com.glowka.rafal.exchange.presentation.architecture.businessFlow
import com.glowka.rafal.exchange.presentation.architecture.screen
import org.koin.dsl.module

val introFeatureModule = module {

  single<IntroFlow> {
    IntroFlowImpl(mainFlow = get())
  }

  businessFlow(
    scopeName = IntroFlow.SCOPE_NAME,
  ) {
    screen(screen = IntroFlow.Screens.Start)
  }
}