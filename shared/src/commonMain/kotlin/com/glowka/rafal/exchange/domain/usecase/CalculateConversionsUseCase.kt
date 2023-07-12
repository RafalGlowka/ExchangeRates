package com.glowka.rafal.exchange.domain.usecase

import com.glowka.rafal.exchange.domain.model.calculator.CurrencyCalculatorState
import com.glowka.rafal.exchange.domain.model.calculator.CurrencyConversion

interface CalculateConversionsUseCase : UseCase<CurrencyCalculatorState, List<CurrencyConversion>>

class CalculateConversionsUseCaseImpl : CalculateConversionsUseCase {
  override fun invoke(param: CurrencyCalculatorState): Result<List<CurrencyConversion>> {
//    println("invoke ${param.rates.size}")
    return try {
      val currencyFactor: Double = if (param.currency.isUSD()) {
        1.0
      } else {
//        println("looking for ${param.currency.code}")
        val rateToUsd = param.rates.first { usdRate -> usdRate.currency.code == param.currency.code }
        rateToUsd.usdRate
      }
      Result.success(
        param.rates.map { rate ->
          CurrencyConversion(
            currency = rate.currency,
            conversion = param.value * rate.usdRate / currencyFactor
          )
        }
      )
    } catch (e: Exception) {
      println("internal error: $e")
      Result.failure(e)
    }
  }
}