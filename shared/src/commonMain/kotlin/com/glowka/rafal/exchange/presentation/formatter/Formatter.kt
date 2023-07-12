package com.glowka.rafal.exchange.presentation.formatter

interface Formatter<FROM, TO> {
  fun format(data: FROM): TO
}
