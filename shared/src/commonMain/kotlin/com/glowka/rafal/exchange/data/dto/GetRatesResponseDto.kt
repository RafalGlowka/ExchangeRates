package com.glowka.rafal.exchange.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetRatesResponseDto(

  // TODO:  Consider timestamp from BE as the timestamp for logic. Task is saying about limiting requests,
  //  but in real life data timestamp would be more usefull for user

  @SerialName("base")
  val base: String?,

  @SerialName("rates")
  val rates: Map<String, Double?>?
)
