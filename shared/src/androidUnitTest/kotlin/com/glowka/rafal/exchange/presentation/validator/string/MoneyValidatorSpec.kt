package com.glowka.rafal.exchange.presentation.validator.string

import com.glowka.rafal.exchange.presentation.validator.shouldBeValidationError
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.result.shouldBeSuccess

class MoneyValidatorSpec : DescribeSpec() {

  init {

    val validator = MoneyValidator()

    describe("Checking money validator") {
      it("returns 0 for empty string") {
        validator.validate("") shouldBeSuccess 0.0
      }

      it("checks correct money format") {
        listOf(
          "6" to 6.0,
          "100" to 100.0,
          "12" to 12.0,
          "10.2" to 10.2,
          "123.45" to 123.45,
          "0.123456" to 0.123456,
          "123456789" to 123456789.toDouble(),
          "123456789.123456" to 123456789.123456
        ).forEach { data ->
          println("${data.first} -> ${data.second}")
          validator.validate(data.first) shouldBeSuccess data.second
        }
      }

      it("fails on incorrect strings") {
        listOf(
          "13z",
          "12d",
          "10f",
          "12.12.12.12.12",
          "123..",
          "12.234.234",
          ".21",
        ).forEach { data ->
          println(data)
          validator.validate(data) shouldBeValidationError MoneyValidator.Errors.InvalidMoney
        }
      }
    }
  }
}