package com.glowka.rafal.exchange.domain.model.calculator

import com.glowka.rafal.exchange.domain.model.Currency

data class CurrencyConversion(
  val currency: Currency,
  val conversion: Double,
)