package com.glowka.rafal.exchange.presentation

import com.glowka.rafal.exchange.presentation.utils.CoroutineErrorHandler
import io.kotest.core.spec.style.DescribeSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
open class ViewModelSpec : DescribeSpec() {

  val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

  init {
    Dispatchers.setMain(dispatcher)

    beforeSpec {
      initKoinForViewModelTest()
    }
    afterSpec {
      cleanUpKoin()
    }
  }

  private fun initKoinForViewModelTest() {
    startKoin {
      modules(
        listOf<Module>(
          module {
            single<CoroutineErrorHandler> {
              CoroutineErrorHandler()
            }
          }
        )
      )
      createEagerInstances()
    }.koin
  }

  private fun cleanUpKoin() {
    stopKoin()
  }

  fun advanceTimeBy(delayTimeMs: Long) {
    dispatcher.scheduler.advanceTimeBy(delayTimeMs)
  }
}