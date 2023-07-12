package com.glowka.rafal.exchange.domain.usecase

interface UseCase<PARAM, RESULT> {
  operator fun invoke(param: PARAM): Result<RESULT>
}

interface CoroutineUseCase<PARAM, RESULT> {
  suspend operator fun invoke(param: PARAM): Result<RESULT>
}