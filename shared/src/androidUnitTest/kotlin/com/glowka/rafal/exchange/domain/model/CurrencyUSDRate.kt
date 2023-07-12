package com.glowka.rafal.exchange.domain.model

fun currencyUSDRate(
  code: String = "USD",
  usdRate: Double = 1.0,
) = CurrencyUSDRate(
  currency = Currency(code = code),
  usdRate = usdRate
)