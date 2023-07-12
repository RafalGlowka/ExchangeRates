package com.glowka.rafal.exchange.presentation.validator

class ValidationException(val error: ValidationError<*>) : Exception(error.toString())

class TransformationException(reason: Throwable) : Exception(reason)