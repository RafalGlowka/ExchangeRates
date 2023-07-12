@file:Suppress("Filename")

package com.glowka.rafal.exchange

import co.touchlab.kermit.Logger
import com.glowka.rafal.exchange.di.IOSDIHelper
import com.glowka.rafal.exchange.flow.startMainFlow
import com.glowka.rafal.exchange.presentation.architecture.NavigationControllerNavigatorImpl
import com.glowka.rafal.exchange.presentation.architecture.ScreenNavigator
import com.glowka.rafal.exchange.presentation.architecture.UIViewFactory
import com.glowka.rafal.exchange.presentation.compose.initScreenData
import com.glowka.rafal.exchange.presentation.style.initFonts
import org.koin.dsl.module
import platform.UIKit.UINavigationController
import platform.UIKit.UIViewController

@Suppress("FunctionNaming", "Filename")
fun InitMainViewController(viewFactory: UIViewFactory): UIViewController {
  val navigationController = UINavigationController()
  try {
    IOSDIHelper.init(
      iosModule = module {
        single<ScreenNavigator> {
          NavigationControllerNavigatorImpl(navigationController, viewFactory)
        }
      }
    )
    initFonts()
    initScreenData()
    startMainFlow()
  } catch (e: Exception) {
    Logger.e("InitMainViewController", e)
  }
  return navigationController
}

fun globalLog(message: String) {
  Logger.d(message)
}
