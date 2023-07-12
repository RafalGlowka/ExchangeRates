package com.glowka.rafal.exchange.di

import org.koin.core.Koin
import org.koin.core.context.startKoin
import org.koin.core.module.Module

private lateinit var globalKoin: Koin

object IOSDIHelper {
  fun init(iosModule: Module) {
    globalKoin = startKoin {
      modules(commonModules)
      modules(iosModule)
      createEagerInstances()
    }.koin
  }
}

actual fun getGlobalKoin(): Koin = globalKoin
