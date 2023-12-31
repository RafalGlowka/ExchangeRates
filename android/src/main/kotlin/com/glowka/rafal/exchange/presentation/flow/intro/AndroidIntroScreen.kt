package com.glowka.rafal.exchange.presentation.flow.intro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.glowka.rafal.exchange.MainRes
import com.glowka.rafal.exchange.R
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.compose.SnackbarEvent
import com.glowka.rafal.exchange.presentation.compose.getAppTypography
import com.glowka.rafal.exchange.presentation.compose.idle
import com.glowka.rafal.exchange.presentation.compose.navigationBarsPadding
import com.glowka.rafal.exchange.presentation.compose.systemBarsPadding
import com.glowka.rafal.exchange.presentation.style.Colors
import com.glowka.rafal.exchange.presentation.style.FontSize

@Composable
internal fun AndroidIntroScreen(
  viewState: IntroViewModelToViewInterface.State,
  onEvent: (IntroViewModelToViewInterface.ViewEvents) -> Unit
) {
  LaunchedEffect(Unit) {
    onEvent(IntroViewModelToViewInterface.ViewEvents.ActiveScreen)
  }

  MaterialTheme(
    typography = getAppTypography()
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .navigationBarsPadding()
        .background(Colors.white)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.exchange))
        @Suppress("MagicNumber")
        LottieAnimation(
          modifier = Modifier
            .fillMaxWidth(1f)
            .fillMaxHeight(0.4f),
          composition = composition,
          iterations = LottieConstants.IterateForever,
        )
        Text(
          text = MainRes.string.app_name,
          modifier = Modifier.fillMaxWidth(),
          textAlign = TextAlign.Center,
          fontSize = FontSize.title,
          fontWeight = FontWeight.Bold
        )
      }
      SnackbarEvent(
        modifier = Modifier.align(Alignment.BottomCenter),
        snakbarEvent = viewState.snackbarEvent,
        onViewEvent = onEvent,
      )
    }
  }
}

@Preview
@Composable
fun AndroidIntroScreenPreview() {
  AndroidIntroScreen(viewState =
    IntroViewModelToViewInterface.State(idle()),
    onEvent = {})
}