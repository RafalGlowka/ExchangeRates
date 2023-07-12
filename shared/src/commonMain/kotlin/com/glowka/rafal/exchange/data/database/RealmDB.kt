package com.glowka.rafal.exchange.data.database

import com.glowka.rafal.exchange.data.dso.ConfigKeyDso
import com.glowka.rafal.exchange.data.dso.CurrencyUSDRateDso
import com.glowka.rafal.exchange.presentation.compose.imageURL.ImageDso
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

fun initRealm(): Realm {
  val config = RealmConfiguration.create(setOf(CurrencyUSDRateDso::class, ConfigKeyDso::class, ImageDso::class))
  return Realm.open(config)
}