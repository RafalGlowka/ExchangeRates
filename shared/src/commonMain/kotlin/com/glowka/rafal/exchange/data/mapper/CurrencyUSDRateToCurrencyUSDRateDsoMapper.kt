package com.glowka.rafal.exchange.data.mapper

import com.glowka.rafal.exchange.data.dso.CurrencyUSDRateDso
import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate

interface CurrencyUSDRateToCurrencyUSDRateDsoMapper : Mapper<CurrencyUSDRate, CurrencyUSDRateDso>

class CurrencyUSDRateToCurrencyUSDRateDsoMapperImpl : CurrencyUSDRateToCurrencyUSDRateDsoMapper {

  override fun invoke(data: CurrencyUSDRate): CurrencyUSDRateDso {
    return CurrencyUSDRateDso().apply {
      code = data.currency.code
      usdRate = data.usdRate
    }
  }
}
