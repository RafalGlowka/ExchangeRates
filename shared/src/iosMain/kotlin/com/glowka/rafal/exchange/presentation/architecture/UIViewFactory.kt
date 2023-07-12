package com.glowka.rafal.exchange.presentation.architecture

import platform.UIKit.UIViewController

interface UIViewFactory {
  fun createViewController(screenStructure: ScreenStructure<*, *, *, *>): UIViewController?
}
