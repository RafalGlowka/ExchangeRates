package com.glowka.rafal.exchange.domain.model.calculator

import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate

data class CurrencyCalculatorState(
  val rates: List<CurrencyUSDRate>,
  val input: String = "0",
  val value: Double = 0.0,
  val currency: Currency = Currency("USD"),
)