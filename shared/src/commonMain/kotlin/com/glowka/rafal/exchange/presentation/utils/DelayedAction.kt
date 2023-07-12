package com.glowka.rafal.exchange.presentation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DelayedAction(private val coroutineScope: CoroutineScope) {

  private var runningJob: Job? = null

  fun replaceAction(delay: Long = 1000, action: () -> Unit) {
    runningJob?.cancel()
    runningJob = coroutineScope.launch {
      delay(delay)
      action()
      runningJob = null
    }
  }
}