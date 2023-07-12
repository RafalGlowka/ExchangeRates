package com.glowka.rafal.exchange.flow.intro

import androidx.compose.material.SnackbarDuration
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.glowka.rafal.exchange.flow.intro.intro.IntroScreen
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.compose.SnackbarEvent
import com.glowka.rafal.exchange.presentation.compose.ViewStateEvent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IntroScreenTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun appNameIsVisibleOnScreen() {
    composeTestRule.setContent {
      IntroScreen(
        viewState = IntroViewModelToViewInterface.State(snackbarEvent = ViewStateEvent.Idle()),
        onEvent = {}
      )
    }

    composeTestRule
      .onNodeWithText("Exchange")
      .assertExists()
  }

  @Test
  fun snackBarVisibleOnScreen() {
    composeTestRule.setContent {
      IntroScreen(
        viewState = IntroViewModelToViewInterface.State(
          snackbarEvent = ViewStateEvent.Triggered<SnackbarEvent<IntroViewModelToViewInterface.ViewEvents>>(
            SnackbarEvent(
              "Snackbar test 1 - message",
              onShowEvent = IntroViewModelToViewInterface.ViewEvents.SnackbarShown,
              duration = SnackbarDuration.Short,
            )
          )
        ),
        onEvent = {},
      )
    }

    composeTestRule
      .onNodeWithText("Snackbar test 1 - message")
      .assertExists()
  }
}