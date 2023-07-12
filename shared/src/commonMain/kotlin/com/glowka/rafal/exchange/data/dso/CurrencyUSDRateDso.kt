package com.glowka.rafal.exchange.data.dso

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class CurrencyUSDRateDso : RealmObject {
  @PrimaryKey
  var code: String = ""
  var usdRate: Double = Double.NaN

  // Required for Tests/Fakes
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as CurrencyUSDRateDso

    if (code != other.code) return false
    return usdRate == other.usdRate
  }

  override fun hashCode(): Int {
    var result = code.hashCode()
    result = 31 * result + usdRate.hashCode()
    return result
  }
}
