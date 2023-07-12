package com.glowka.rafal.exchange.flow.exchange.currencyPicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.glowka.rafal.exchange.presentation.architecture.DialogType
import com.glowka.rafal.exchange.presentation.architecture.ScreenDialogStructure
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToViewInterface
import org.koin.core.scope.Scope

object CurrencyPickerScreenDialogStructure : ScreenDialogStructure<
    CurrencyPickerDialogViewModelToFlowInterface.Input,
    CurrencyPickerDialogViewModelToFlowInterface.Output,
    CurrencyPickerDialogViewModelToViewInterface.ViewState,
    CurrencyPickerDialogViewModelToViewInterface.ViewEvents
    >() {

  override val type = DialogType.BOTTOM

  override val content =
    @Composable { viewModel: ViewModelToViewInterface<
        CurrencyPickerDialogViewModelToViewInterface.ViewState,
        CurrencyPickerDialogViewModelToViewInterface.ViewEvents
        > ->
      val viewState by viewModel.viewState.collectAsState()
      CurrencyPickerScreenDialog(viewState, viewModel::onViewEvent)
    }

  override fun Scope.viewModelCreator() = CurrencyPickerDialogViewModelImpl(
    exchangeRepository = get()
  )
}