package com.glowka.rafal.exchange.flow.exchange.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.exchange.presentation.architecture.ScreenStructure
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToViewInterface
import org.koin.core.scope.Scope

object MainScreenStructure : ScreenStructure<
   MainViewModelToFlowInterface.Input,
   MainViewModelToFlowInterface.Output,
   MainViewModelToViewInterface.ViewState,
   MainViewModelToViewInterface.ViewEvents
   >() {
  override val content =
    @Composable { viewModel: ViewModelToViewInterface<
       MainViewModelToViewInterface.ViewState,
       MainViewModelToViewInterface.ViewEvents
       > ->
      val viewState by viewModel.viewState.collectAsState()
      MainScreen(viewState, viewModel::onViewEvent)
    }

  override fun Scope.viewModelCreator() = MainViewModelImpl(
    exchangeRepository = get(),
    calculateConversionsUseCase = get(),
  )
}