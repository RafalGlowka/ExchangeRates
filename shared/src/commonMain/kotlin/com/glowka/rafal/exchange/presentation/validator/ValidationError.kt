package com.glowka.rafal.exchange.presentation.validator

interface ValidationError<T> {
  fun validate(data: T): Boolean
}