package com.glowka.rafal.exchange.flow.exchange.currencyPicker

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.glowka.rafal.exchange.domain.model.Currency
import com.glowka.rafal.exchange.utils.EventMonitor
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrencyPickerTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun pickingCurrencyEmitOnItemEvent() {
    val eventMonitor = EventMonitor<CurrencyPickerDialogViewModelToViewInterface.ViewEvents>()
    composeTestRule.setContent {
      CurrencyPickerScreenDialog(
        viewState = CurrencyPickerDialogViewModelToViewInterface.ViewState(
          items = listOf(Currency("USD"), Currency("PLN"))
        ),
        onViewEvent = eventMonitor::onEvent
      )
    }

    runBlocking {
      eventMonitor.events.test {
        expectNoEvents()

        composeTestRule
          .onNodeWithText("PLN")
          .assertExists()
          .performClick()
        awaitItem() shouldBe CurrencyPickerDialogViewModelToViewInterface.ViewEvents.OnItemClicked(1)
      }
    }
  }
}