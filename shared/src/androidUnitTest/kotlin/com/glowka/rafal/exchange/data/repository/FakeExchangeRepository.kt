package com.glowka.rafal.exchange.data.repository

import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate
import com.glowka.rafal.exchange.domain.repository.ExchangeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow

class FakeExchangeRepository : ExchangeRepository {

  private val _initializing = MutableSharedFlow<Unit>()
  val initializing: SharedFlow<Unit> = _initializing

  private val _reloading = MutableSharedFlow<Unit>()
  val reloading: SharedFlow<Unit> = _reloading

  private var initResponse: Result<Boolean> =
    Result.failure(IllegalStateException("Missing response"))
  private var initDelayMs = 0L
  private var reloadResponse: Result<List<CurrencyUSDRate>> =
    Result.failure(IllegalStateException("Missing response"))
  private var reloadDelayMs = 0L

  override val rates = MutableStateFlow<List<CurrencyUSDRate>>(emptyList())

  override suspend fun initWithLocalStorage(): Result<Boolean> {
    _initializing.emit(Unit)
    delay(initDelayMs)
    return initResponse
  }

  override suspend fun reloadFromBackend(): Result<List<CurrencyUSDRate>> {
    _reloading.emit(Unit)
    delay(reloadDelayMs)
    return reloadResponse.onSuccess { list ->
      rates.emit(list)
    }
  }

  fun setInitResponse(
    initResponse: Result<Boolean>,
    delayMs: Long = 0L,
  ) {
    this.initResponse = initResponse
    this.initDelayMs = delayMs
  }

  fun setReloadResponse(
    reloadResponse: Result<List<CurrencyUSDRate>>,
    delayMs: Long = 0L,
  ) {
    this.reloadResponse = reloadResponse
    this.reloadDelayMs = delayMs
  }
}