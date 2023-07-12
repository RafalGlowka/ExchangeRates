package com.glowka.rafal.exchange.data.time

import com.glowka.rafal.exchange.domain.time.SystemClock
import kotlinx.datetime.Clock

class SystemClockImpl : SystemClock {
  override fun now() = Clock.System.now()
}