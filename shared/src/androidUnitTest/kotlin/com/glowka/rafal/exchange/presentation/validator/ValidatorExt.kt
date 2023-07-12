package com.glowka.rafal.exchange.presentation.validator

import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeTypeOf

infix fun <T, R> Result<R>.shouldBeValidationError(validationError: ValidationError<T>) =
  shouldBeFailure().shouldBeTypeOf<ValidationException>().error shouldBe validationError