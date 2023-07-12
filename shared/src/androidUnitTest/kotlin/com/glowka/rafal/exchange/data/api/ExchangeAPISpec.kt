package com.glowka.rafal.exchange.data.api

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json

class ExchangeAPISpec : DescribeSpec() {
  init {

    val httpClient = HttpClient(
      engine = MockEngine { _ ->
        respond(
          content = ByteReadChannel(
            "{\n" +
                "  \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n" +
                "  \"license\": \"https://openexchangerates.org/license\",\n" +
                "  \"timestamp\": 1686513604,\n" +
                "  \"base\": \"USD\",\n" +
                "  \"rates\": {\n" +
                "    \"AED\": 3.67297,\n" +
                "    \"AFN\": 85.625378,\n" +
                "    \"ALL\": 98.184006,\n" +
                "    \"AMD\": 385.304871,\n" +
                "    \"USD\": 1.0\n" +
                "    }\n" +
                "  }"
          ),
          status = HttpStatusCode.OK,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }
    ) {
      install(ContentNegotiation) {
        json(
          Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
          }
        )
      }
    }
    val api = ExchangeApiImpl(httpClient = httpClient)

    it("calls backend and parser response") {
      val rates = api.getRates()
      rates.base shouldBe "USD"
      rates.rates?.keys shouldBe setOf("AED", "AFN", "ALL", "AMD", "USD")
      rates.rates?.get("AED") shouldBe 3.67297
      rates.rates?.get("AFN") shouldBe 85.625378
      rates.rates?.get("ALL") shouldBe 98.184006
      rates.rates?.get("AMD") shouldBe 385.304871
      rates.rates?.get("USD") shouldBe 1.0
    }
  }
}