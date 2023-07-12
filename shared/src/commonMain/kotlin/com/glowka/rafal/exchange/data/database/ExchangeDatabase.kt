package com.glowka.rafal.exchange.data.database

import com.glowka.rafal.exchange.data.dso.ConfigKeyDso
import com.glowka.rafal.exchange.data.dso.CurrencyUSDRateDso
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy

interface ExchangeDatabase {

  suspend fun insertCurrencyUSDRate(c: CurrencyUSDRateDso)

  suspend fun getCurrencyUSDRates(): List<CurrencyUSDRateDso>

  suspend fun deleteCurrencyUSDRates()

  suspend fun getConfigKey(key: String): String?
  suspend fun setConfigKey(key: String, value: String)
}

// Consider internal
class ExchangeDatabaseImpl(
  private val realm: Realm,
) : ExchangeDatabase {

  override suspend fun insertCurrencyUSDRate(c: CurrencyUSDRateDso) {
    realm.write {
      copyToRealm(c, UpdatePolicy.ALL)
    }
  }

  override suspend fun getCurrencyUSDRates(): List<CurrencyUSDRateDso> {
    return realm.query(CurrencyUSDRateDso::class).find()
  }

  override suspend fun deleteCurrencyUSDRates() {
    realm.write {
      val ratesToDelete = query(CurrencyUSDRateDso::class).find()
      delete(ratesToDelete)
    }
  }

  override suspend fun getConfigKey(key: String): String? {
//    log.d("getKey $key")
    return try {
      realm.query(ConfigKeyDso::class, "key == $0", key).find().firstOrNull()?.value
    } catch (e: NullPointerException) {
      null
    }
  }

  override suspend fun setConfigKey(key: String, value: String) {
//    log.d("setKey $key")
    realm.write {
      val configKey = ConfigKeyDso().apply {
        this.key = key
        this.value = value
      }
      copyToRealm(configKey, UpdatePolicy.ALL)
    }
  }
}