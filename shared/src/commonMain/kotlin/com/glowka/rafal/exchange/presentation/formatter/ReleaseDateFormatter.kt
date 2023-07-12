package com.glowka.rafal.exchange.presentation.formatter

import com.glowka.rafal.exchange.domain.utils.EMPTY
import kotlinx.datetime.toLocalDate

interface ReleaseDateFormatter : Formatter<String, String>

class ReleaseDateFormatterImpl : ReleaseDateFormatter {
  override fun format(data: String): String {
    return try {
      val date = data.toLocalDate()
      val day = date.dayOfMonth
      val month = date.month.name
      val year = date.year

      "Released $month ${day.zeroPrefixed(2)}, $year"
    } catch (t: Throwable) {
      String.EMPTY
    }
  }
}
