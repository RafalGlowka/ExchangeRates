package com.glowka.rafal.exchange.presentation.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.DialogFragment
import com.glowka.rafal.exchange.di.injectViewModel

interface ScreenDialogFragment<
    VIEW_STATE : ViewState,
    VIEW_EVENT : ViewEvent,
    >

abstract class BaseDialogFragment<
    VIEW_STATE : ViewState,
    VIEW_EVENT : ViewEvent,
    > : ScreenDialogFragment<VIEW_STATE, VIEW_EVENT>, DialogFragment() {
  protected val viewModel: ViewModelToViewInterface<VIEW_STATE, VIEW_EVENT> by injectViewModel()

  final override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
      setContent(content)
    }
  }

  abstract val content: @Composable () -> Unit

  fun onBackPressed(): Boolean {
    return viewModel.onBackPressed()
  }

  companion object {
    const val ARG_SCOPE = "scope"
    const val ARG_SCREEN_TAG = "screenTag"
  }
}