package com.glowka.rafal.exchange.data.api

import com.glowka.rafal.exchange.data.dto.GetRatesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface ExchangeApi {
  suspend fun getRates(): GetRatesResponseDto
}

class ExchangeApiImpl(private val httpClient: HttpClient) : ExchangeApi {

  private val baseUrl = "https://openexchangerates.org"
  private val appId = "8c8b7dc79c7d41d68fd89db256b165d2"

  override suspend fun getRates(): GetRatesResponseDto {
    return httpClient.get(
      urlString = "$baseUrl/api/latest.json"
    ) {
      url {
        parameters
          .append("app_id", appId)
      }
    }.body()
  }
}
