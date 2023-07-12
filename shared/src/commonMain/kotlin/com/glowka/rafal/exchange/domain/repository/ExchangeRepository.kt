package com.glowka.rafal.exchange.domain.repository

import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate
import kotlinx.coroutines.flow.StateFlow

interface ExchangeRepository {

  val rates: StateFlow<List<CurrencyUSDRate>>
  suspend fun initWithLocalStorage(): Result<Boolean>
  suspend fun reloadFromBackend(): Result<List<CurrencyUSDRate>>
}
