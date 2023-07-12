package com.glowka.rafal.exchange.di.flow

import com.glowka.rafal.exchange.flow.exchange.MainFlow
import com.glowka.rafal.exchange.flow.exchange.MainFlowImpl
import com.glowka.rafal.exchange.presentation.architecture.businessFlow
import com.glowka.rafal.exchange.presentation.architecture.screen
import com.glowka.rafal.exchange.presentation.architecture.screenDialog
import org.koin.dsl.module

val mainFeatureModule = module {

  single<MainFlow> {
    MainFlowImpl()
  }

  businessFlow(
    scopeName = MainFlow.SCOPE_NAME,
  ) {
    screen(screen = MainFlow.Screens.Main)
    screenDialog(screen = MainFlow.ScreenDialogs.CurrencyPicker)
  }
}