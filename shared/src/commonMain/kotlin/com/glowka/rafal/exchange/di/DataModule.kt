package com.glowka.rafal.exchange.di

import com.glowka.rafal.exchange.data.database.initRealm
import com.glowka.rafal.exchange.data.time.SystemClockImpl
import com.glowka.rafal.exchange.domain.time.SystemClock
import com.glowka.rafal.exchange.presentation.compose.imageURL.ImageURLDBCache
import com.glowka.rafal.exchange.presentation.compose.imageURL.ImageURLDBCacheImpl
import com.glowka.rafal.exchange.presentation.compose.imageURL.ImageURLMemoryCache
import com.glowka.rafal.exchange.presentation.compose.imageURL.ImageURLMemoryCacheImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.realm.kotlin.Realm
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import kotlin.time.Duration.Companion.days

@Suppress("MagicNumber")
val dataModule = module {
  single<Realm> {
    initRealm()
  }

  single {
    HttpClient {
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
          }
        )
      }
    }
  }

  single<ImageURLMemoryCache> {
    ImageURLMemoryCacheImpl(40)
  }

  single<ImageURLDBCache> {
    ImageURLDBCacheImpl(
      realm = get(),
      dataValidDuration = 30.days
    )
  }

  single<SystemClock> {
    SystemClockImpl()
  }
}
