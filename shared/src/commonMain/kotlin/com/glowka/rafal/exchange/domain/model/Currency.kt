package com.glowka.rafal.exchange.domain.model

data class Currency(val code: String) {
  fun isUSD(): Boolean = code.uppercase() == "USD"
}