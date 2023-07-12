package com.glowka.rafal.exchange.flow.exchange.main

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.glowka.rafal.exchange.utils.EventMonitor
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun closeButtonEmitCloseEvent() {
    val eventMonitor = EventMonitor<MainViewModelToViewInterface.ViewEvents>()
    composeTestRule.setContent {
      MainScreen(
        viewState = MainViewModelToViewInterface.ViewState(),
        onViewEvent = eventMonitor::onEvent
      )
    }

    runBlocking {
      eventMonitor.events.test {
        expectNoEvents()

        composeTestRule
          .onNodeWithContentDescription("Back button")
          .assertExists()
          .performClick()

//        composeTestRule.onRoot().printToLog("ROOT")

        awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.Close
      }
    }
  }

  @Test
  fun curencyPickerEmitChangeCurrencyEvent() {
    val eventMonitor = EventMonitor<MainViewModelToViewInterface.ViewEvents>()
    composeTestRule.setContent {
      MainScreen(
        viewState = MainViewModelToViewInterface.ViewState(),
        onViewEvent = eventMonitor::onEvent
      )
    }

    runBlocking {
      eventMonitor.events.test {
        expectNoEvents()

        composeTestRule
          .onNodeWithText("USD")
          .assertExists()
          .performClick()

        awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.ChangeCurrency
      }
    }
  }

  @Test
  fun inputFieldEmitInputChangeEvent() {
    val eventMonitor = EventMonitor<MainViewModelToViewInterface.ViewEvents>()
    composeTestRule.setContent {
      MainScreen(
        viewState = MainViewModelToViewInterface.ViewState(),
        onViewEvent = eventMonitor::onEvent
      )
    }

    val inputField = composeTestRule
      .onNodeWithText("Insert value")
      .assertExists()

    runBlocking {
      eventMonitor.events.test {
        listOf(
          "100" to 100.0,
          "10.2" to 10.2,
          "a2" to null,
          "1a0v3g" to null,
          "1.213.2321" to null,
        ).forEachIndexed { index, testData ->

          println(" $index. ${testData.first} -> ${testData.second}")

          expectNoEvents()

          inputField.performTextInput(testData.first)

          if (testData.second == null) {
            expectNoEvents()
          } else {
            awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.OnInputChange(testData.second as Double)
          }

          inputField.performTextClearance()

          awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.OnInputChange(0.0)
        }
      }
    }
  }

  @Test
  fun inputFieldFiltersCharactersBeforeSendingInputChangeEvent() {
    val eventMonitor = EventMonitor<MainViewModelToViewInterface.ViewEvents>()
    composeTestRule.setContent {
      MainScreen(
        viewState = MainViewModelToViewInterface.ViewState(),
        onViewEvent = eventMonitor::onEvent
      )
    }

    val inputField = composeTestRule
      .onNodeWithText("Insert value")
      .assertExists()

    runBlocking {
      eventMonitor.events.test {
        inputField.performTextInputKeyByKey("ab12d.q23")

        awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.OnInputChange(1.0)
        awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.OnInputChange(12.0)
        // . will not change the value
        awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.OnInputChange(12.0)
        awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.OnInputChange(12.2)
        awaitItem() shouldBe MainViewModelToViewInterface.ViewEvents.OnInputChange(12.23)
        expectNoEvents()
      }
    }
  }
}

fun SemanticsNodeInteraction.performTextInputKeyByKey(text: String): SemanticsNodeInteraction {
  text.forEach { char ->
    performTextInput(char.toString())
  }
  return this
}