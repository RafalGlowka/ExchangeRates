package com.glowka.rafal.exchange.data.time

import com.glowka.rafal.exchange.domain.time.SystemClock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

class FakeSystemClock : SystemClock {

  private var timeOffset: Duration = Duration.ZERO
  override fun now(): Instant {
    return Clock.System.now().plus(timeOffset)
  }

  fun shiftTime(duration: Duration) {
    this.timeOffset += duration
  }
}