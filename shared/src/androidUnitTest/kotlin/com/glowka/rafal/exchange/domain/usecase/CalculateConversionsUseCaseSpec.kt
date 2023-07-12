package com.glowka.rafal.exchange.domain.usecase

import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate
import com.glowka.rafal.exchange.domain.model.calculator.CurrencyCalculatorState
import com.glowka.rafal.exchange.domain.model.calculator.CurrencyConversion
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.result.shouldBeSuccess

class CalculateConversionsUseCaseSpec : DescribeSpec() {

  init {

    val usd = Currency("USD")
    val pln = Currency("PLN")
    val gbp = Currency("GBP")

    describe("Conversions") {
      val useCase = CalculateConversionsUseCaseImpl()

      it("calculates straight conversion rates for USD") {
        val param = CurrencyCalculatorState(
          input = "0",
          value = 10.0,
          currency = usd,
          rates = listOf(CurrencyUSDRate(usd, 1.0), CurrencyUSDRate(gbp, 0.8), CurrencyUSDRate(pln, 4.0))
        )
        useCase(param = param).shouldBeSuccess()
          .shouldContainExactlyInAnyOrder(
            CurrencyConversion(usd, 10.0),
            CurrencyConversion(gbp, 8.0),
            CurrencyConversion(pln, 40.0),
          )
      }

      it("calculates conversion rates with usdRateFactor for currencies different than USD") {
        val param = CurrencyCalculatorState(
          input = "0",
          value = 10.0,
          currency = gbp,
          rates = listOf(CurrencyUSDRate(usd, 1.0), CurrencyUSDRate(gbp, 0.8), CurrencyUSDRate(pln, 4.0))
        )
        useCase(param = param).shouldBeSuccess()
          .shouldContainExactlyInAnyOrder(
            CurrencyConversion(usd, 12.5),
            CurrencyConversion(gbp, 10.0),
            CurrencyConversion(pln, 50.0),
          )
      }
    }
  }
}