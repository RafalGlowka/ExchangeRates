package com.glowka.rafal.exchange

import android.app.Application
import com.glowka.rafal.exchange.di.AndroidDIHelper
import com.glowka.rafal.exchange.modules.androidModule

class ExchangeApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    AndroidDIHelper.init(this@ExchangeApplication, androidModule = androidModule)
  }
}