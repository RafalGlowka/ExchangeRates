package com.glowka.rafal.exchange.domain.usecase

open class FakeUseCase<PARAM, RESULT>(val result: Result<RESULT>) : UseCase<PARAM, RESULT> {
  override fun invoke(param: PARAM): Result<RESULT> {
    return result
  }
}