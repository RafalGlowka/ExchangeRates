package com.glowka.rafal.exchange.di

import com.glowka.rafal.exchange.di.flow.introFeatureModule
import com.glowka.rafal.exchange.di.flow.mainFeatureModule
import org.koin.core.module.Module

val commonModules = listOf<Module>(
  dataModule,
  exchangeModule,
  introFeatureModule,
  mainFeatureModule,
)