package com.glowka.rafal.exchange.data.repository

import com.glowka.rafal.exchange.data.api.ExchangeApi
import com.glowka.rafal.exchange.data.api.makeApiCall
import com.glowka.rafal.exchange.data.database.ConfigKeys
import com.glowka.rafal.exchange.data.database.ExchangeDatabase
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateDsoToCurrencyUSDRateMapper
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateToCurrencyUSDRateDsoMapper
import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate
import com.glowka.rafal.exchange.domain.repository.ExchangeRepository
import com.glowka.rafal.exchange.domain.time.SystemClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes

class ExchangeRepositoryImpl(
  val exchangeApi: ExchangeApi,
  private val systemClock: SystemClock,
  private val exchangeDatabase: ExchangeDatabase,
  private val currencyUSDRateDsoToCurrencyUSDRateMapper: CurrencyUSDRateDsoToCurrencyUSDRateMapper,
  private val currencyUSDRateToCurrencyUSDRateDsoMapper: CurrencyUSDRateToCurrencyUSDRateDsoMapper,
) : ExchangeRepository {
  override val rates = MutableStateFlow(emptyList<CurrencyUSDRate>())

  override suspend fun initWithLocalStorage(): Result<Boolean> {
    return Result.success(
      withContext(Dispatchers.IO) {
        val lastUpdate = getLastUpdateDateFromLocalStorage()
        println("lastUpdate $lastUpdate")
        if (lastUpdate != null && lastUpdate > systemClock.now().minus(requestPeriod).toLocalDateTime(
            timeZone = TimeZone.currentSystemDefault()
          )
        ) {
          getRatesFromLocalStore()
        } else {
          false
        }
      }
    )
  }

  override suspend fun reloadFromBackend(): Result<List<CurrencyUSDRate>> {
    return makeApiCall {
      val response = exchangeApi.getRates()
      check(response.base == "USD")
      response.rates?.toList()?.map { ratesDto ->
        ratesDto.second?.let { rate ->
          CurrencyUSDRate(Currency(ratesDto.first), rate)
        }
      }?.filterNotNull() ?: emptyList()
    }.onSuccess { newList ->
      rates.emit(newList)
      withContext(Dispatchers.IO) {
        saveRatesToLocalStore(newList)
      }
    }
  }

  private suspend fun getLastUpdateDateFromLocalStorage(): LocalDateTime? {
    return exchangeDatabase.getConfigKey(ConfigKeys.LAST_REQUEST)
      ?.toLocalDateTime()
  }

  private suspend fun getRatesFromLocalStore(): Boolean {
    val list = exchangeDatabase.getCurrencyUSDRates()
      .map { rateDso ->
        currencyUSDRateDsoToCurrencyUSDRateMapper(rateDso)
      }
    if (list.isNotEmpty()) {
      rates.emit(list)
    }
    return list.isNotEmpty()
  }

  private suspend fun saveRatesToLocalStore(list: List<CurrencyUSDRate>): Boolean {
    list.firstOrNull()?.run {
      exchangeDatabase.deleteCurrencyUSDRates()
    }
    list.onEach { rate ->
      val rateDso = currencyUSDRateToCurrencyUSDRateDsoMapper(rate)
      exchangeDatabase.insertCurrencyUSDRate(rateDso)
    }
    exchangeDatabase.setConfigKey(
      key = ConfigKeys.LAST_REQUEST,
      value = systemClock.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
    )
    return true
  }

  companion object {
    val requestPeriod = 30.minutes
  }
}