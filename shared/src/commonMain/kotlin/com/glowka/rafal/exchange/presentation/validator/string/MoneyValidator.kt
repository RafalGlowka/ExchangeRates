package com.glowka.rafal.exchange.presentation.validator.string

import com.glowka.rafal.exchange.presentation.validator.ValidationError
import com.glowka.rafal.exchange.presentation.validator.Validator

class MoneyValidator : Validator<String, Double>(
  errors = listOf(Errors.InvalidMoney),
  transform = { data -> data.toDouble() },
  specialCases = listOf("" to 0.0)
) {

  sealed interface Errors : ValidationError<String> {
    object InvalidMoney : Errors {
      val REGEX = "^([1-9][0-9]{0,19}|0)(\\.[0-9]{0,6})?\$".toRegex()
      override fun validate(data: String): Boolean {
        return data matches REGEX
      }
    }
  }
}