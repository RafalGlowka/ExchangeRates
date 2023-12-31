package com.glowka.rafal.exchange.data.mapper

interface Mapper<DATA_IN : Any?, DATA_OUT : Any?> {
  operator fun invoke(data: DATA_IN): DATA_OUT
}
