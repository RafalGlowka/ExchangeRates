package com.glowka.rafal.exchange.presentation.validator

open class Validator<T, R>(
  private val errors: List<ValidationError<T>>,
  private val transform: (T) -> R,
  private val specialCases: List<Pair<T, R>> = emptyList()
) {
  fun validate(data: T): Result<R> {
    specialCases.forEach { specialCase ->
      if (specialCase.first == data) {
        return Result.success(specialCase.second)
      }
    }
    errors.forEach { validationError ->
      if (!validationError.validate(data)) return Result.failure(ValidationException(validationError))
    }
    return try {
      val result = transform(data)
      Result.success(result)
    } catch (throwable: Throwable) {
      Result.failure(TransformationException(throwable))
    }
  }
}