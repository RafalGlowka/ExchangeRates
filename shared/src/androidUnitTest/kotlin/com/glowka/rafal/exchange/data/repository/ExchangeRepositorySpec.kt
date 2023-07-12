package com.glowka.rafal.exchange.data.repository

import com.glowka.rafal.exchange.data.api.ExchangeApi
import com.glowka.rafal.exchange.data.database.FakeExchangeDatabase
import com.glowka.rafal.exchange.data.dso.CurrencyUSDRateDso
import com.glowka.rafal.exchange.data.dto.GetRatesResponseDto
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateDsoToCurrencyUSDRateMapperImpl
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateToCurrencyUSDRateDsoMapperImpl
import com.glowka.rafal.exchange.data.time.FakeSystemClock
import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.time.Duration.Companion.minutes

class ExchangeRepositorySpec : DescribeSpec() {

  init {
    describe("Checking initialization flow & stored state") {

      val exchangeApi: ExchangeApi = mockk()
      val systemClock = FakeSystemClock()
      val exchangeDatabase = FakeExchangeDatabase()
      val currencyUSDRateToCurrencyUSDRateDsoMapper = CurrencyUSDRateToCurrencyUSDRateDsoMapperImpl()
      val currencyUSDRateDsoToCurrencyUSDRateMapper = CurrencyUSDRateDsoToCurrencyUSDRateMapperImpl()

      val repository = ExchangeRepositoryImpl(
        exchangeApi = exchangeApi,
        systemClock = systemClock,
        exchangeDatabase = exchangeDatabase,
        currencyUSDRateToCurrencyUSDRateDsoMapper = currencyUSDRateToCurrencyUSDRateDsoMapper,
        currencyUSDRateDsoToCurrencyUSDRateMapper = currencyUSDRateDsoToCurrencyUSDRateMapper,
      )

      it("Should be not initialized from database if database is empty") {
        repository.initWithLocalStorage().shouldBeSuccess().shouldBeFalse()
      }

      it("Should call backend during reload method call ") {
        val response = GetRatesResponseDto(
          base = "USD",
          rates = mutableMapOf("USD" to 1.0, "PLN" to 4.20)
        )
        coEvery { exchangeApi.getRates() } returns response
        repository.reloadFromBackend().shouldBeSuccess().run {
          shouldContainExactlyInAnyOrder(
            CurrencyUSDRate(Currency("USD"), 1.0),
            CurrencyUSDRate(Currency("PLN"), 4.20),
          )
        }

        coVerify { exchangeApi.getRates() }
      }

      it("Should store data in database after success backend call") {
        exchangeDatabase.getCurrencyUSDRates().shouldContainExactlyInAnyOrder(
          CurrencyUSDRateDso().apply {
            code = "USD"
            usdRate = 1.0
          },
          CurrencyUSDRateDso().apply {
            code = "PLN"
            usdRate = 4.20
          },
        )
      }

      it("should be initialized from database after success backend call") {
        repository.initWithLocalStorage().shouldBeSuccess().shouldBeTrue()
      }

      it("should not be initialized from database if data are older then 30 minutes") {
        systemClock.shiftTime(31.minutes)
        repository.initWithLocalStorage().shouldBeSuccess().shouldBeFalse()
      }

      it("should not initialize from database if there is no currency data in it") {
        systemClock.shiftTime((-20).minutes)
        repository.initWithLocalStorage().shouldBeSuccess().shouldBeTrue()
        exchangeDatabase.deleteCurrencyUSDRates()
        repository.initWithLocalStorage().shouldBeSuccess().shouldBeFalse()
      }
    }
  }
}