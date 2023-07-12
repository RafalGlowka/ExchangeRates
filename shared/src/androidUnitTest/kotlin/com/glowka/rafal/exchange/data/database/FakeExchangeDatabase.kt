package com.glowka.rafal.exchange.data.database

import com.glowka.rafal.exchange.data.dso.ConfigKeyDso
import com.glowka.rafal.exchange.data.dso.CurrencyUSDRateDso

class FakeExchangeDatabase : ExchangeDatabase {

  private val currencyUSDRates = mutableListOf<CurrencyUSDRateDso>()
  private val configKeys = mutableListOf<ConfigKeyDso>()
  override suspend fun insertCurrencyUSDRate(c: CurrencyUSDRateDso) {
    val index = currencyUSDRates.indexOfFirst { data -> data == c }
    if (index > -1) {
      currencyUSDRates[index] = c
    } else {
      currencyUSDRates.add(c)
    }
  }

  override suspend fun getCurrencyUSDRates(): List<CurrencyUSDRateDso> {
    return currencyUSDRates
  }

  override suspend fun deleteCurrencyUSDRates() {
    currencyUSDRates.clear()
  }

  override suspend fun getConfigKey(key: String): String? {
    return configKeys.firstOrNull { keyDso -> keyDso.key == key }?.value
  }

  override suspend fun setConfigKey(key: String, value: String) {
    val index = configKeys.indexOfFirst { data -> data.key == key }
    if (index > -1) {
      configKeys[index].value = value
    } else {
      configKeys.add(
        ConfigKeyDso().apply {
          this.key = key
          this.value = value
        }
      )
    }
  }
}