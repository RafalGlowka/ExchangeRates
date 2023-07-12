package com.glowka.rafal.exchange.domain.time

import kotlinx.datetime.Instant

interface SystemClock {
  fun now(): Instant
}