package com.glowka.rafal.exchange

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.glowka.rafal.exchange.flow.startMainFlow
import com.glowka.rafal.exchange.presentation.architecture.BaseActivity
import com.glowka.rafal.exchange.presentation.compose.initScreenData

class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContentView(R.layout.activity_main)
    initScreenData()

    if (savedInstanceState == null) {
      startMainFlow()
    }
    /*
     TODO: Checking if activity was no restored from state after process restart and we have
     fragment stack without proper scopes and objects in DI
     Warning !!
        It need to be solved before production release, for POC/chalange it's just not supported
        edge case.
     */
  }
}