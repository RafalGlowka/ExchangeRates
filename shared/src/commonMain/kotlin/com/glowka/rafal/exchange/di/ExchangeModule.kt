package com.glowka.rafal.exchange.di

import com.glowka.rafal.exchange.data.api.ExchangeApi
import com.glowka.rafal.exchange.data.api.ExchangeApiImpl
import com.glowka.rafal.exchange.data.database.ExchangeDatabase
import com.glowka.rafal.exchange.data.database.ExchangeDatabaseImpl
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateDsoToCurrencyUSDRateMapper
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateDsoToCurrencyUSDRateMapperImpl
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateToCurrencyUSDRateDsoMapper
import com.glowka.rafal.exchange.data.mapper.CurrencyUSDRateToCurrencyUSDRateDsoMapperImpl
import com.glowka.rafal.exchange.data.repository.ExchangeRepositoryImpl
import com.glowka.rafal.exchange.domain.repository.ExchangeRepository
import com.glowka.rafal.exchange.domain.usecase.CalculateConversionsUseCase
import com.glowka.rafal.exchange.domain.usecase.CalculateConversionsUseCaseImpl
import org.koin.dsl.module

val exchangeModule = module {

  factory<ExchangeApi> {
    ExchangeApiImpl(
      httpClient = get()
    )
  }

  single<ExchangeDatabase> {
    ExchangeDatabaseImpl(
      realm = get()
    )
  }

  factory<CurrencyUSDRateDsoToCurrencyUSDRateMapper> {
    CurrencyUSDRateDsoToCurrencyUSDRateMapperImpl()
  }

  factory<CurrencyUSDRateToCurrencyUSDRateDsoMapper> {
    CurrencyUSDRateToCurrencyUSDRateDsoMapperImpl()
  }

  factory<CalculateConversionsUseCase> {
    CalculateConversionsUseCaseImpl()
  }

  single<ExchangeRepository> {
    ExchangeRepositoryImpl(
      exchangeApi = get(),
      exchangeDatabase = get(),
      systemClock = get(),
      currencyUSDRateDsoToCurrencyUSDRateMapper = get(),
      currencyUSDRateToCurrencyUSDRateDsoMapper = get(),
    )
  }
}