package com.glowka.rafal.exchange.presentation.architecture

import androidx.compose.runtime.Composable

open class ScreenDialogBottomFragment<
    VIEW_STATE : ViewState,
    VIEW_EVENT : ViewEvent,
    >(
  val screenStructure: ScreenDialogStructure<*, *, VIEW_STATE, VIEW_EVENT>
) : BaseBottomSheetDialogFragment<VIEW_STATE, VIEW_EVENT>() {

  override val content: @Composable () -> Unit = {
    screenStructure.content(viewModel)
  }
}