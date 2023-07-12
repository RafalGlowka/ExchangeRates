package com.glowka.rafal.exchange.utils

import com.glowka.rafal.exchange.presentation.architecture.ViewEvent
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class EventMonitor<Event : ViewEvent> {

  private val _events = MutableSharedFlow<Event>()
  val events: SharedFlow<Event> = _events

  @OptIn(DelicateCoroutinesApi::class)
  fun onEvent(event: Event) {
    GlobalScope.launch {
      _events.emit(event)
    }
  }
}