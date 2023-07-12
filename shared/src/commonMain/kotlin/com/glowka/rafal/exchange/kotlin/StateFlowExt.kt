package com.glowka.rafal.exchange.kotlin

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

inline fun <T, R> StateFlow<T>.mapState(crossinline transformation: (T) -> R): StateFlow<R> {
  return object : StateFlow<R> {
    private val sourceStream = this@mapState

    override val replayCache: List<R>
      get() = listOf(transformation(sourceStream.value))
    override val value: R
      get() = transformation(sourceStream.value)

    override suspend fun collect(collector: FlowCollector<R>): Nothing {
      coroutineScope {
        sourceStream.map { data -> transformation(data) }
          .stateIn(this)
          .collect(collector)
      }
    }
  }
}