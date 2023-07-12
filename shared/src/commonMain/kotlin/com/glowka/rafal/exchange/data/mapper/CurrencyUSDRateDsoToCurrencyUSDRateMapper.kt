package com.glowka.rafal.exchange.data.mapper

import com.glowka.rafal.exchange.data.dso.CurrencyUSDRateDso
import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.domain.model.CurrencyUSDRate

interface CurrencyUSDRateDsoToCurrencyUSDRateMapper : Mapper<CurrencyUSDRateDso, CurrencyUSDRate>

class CurrencyUSDRateDsoToCurrencyUSDRateMapperImpl : CurrencyUSDRateDsoToCurrencyUSDRateMapper {

 override fun invoke(data: CurrencyUSDRateDso): CurrencyUSDRate {
  return CurrencyUSDRate(
   currency = Currency(code = data.code),
   usdRate = data.usdRate,
  )
 }
}
