@file:Suppress("MaxLineLength")

package com.glowka.rafal.exchange.presentation

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerDialogViewModelToViewInterface
import com.glowka.rafal.exchange.flow.exchange.currencyPicker.CurrencyPickerScreenDialogStructure
import com.glowka.rafal.exchange.flow.exchange.main.MainScreenStructure
import com.glowka.rafal.exchange.flow.exchange.main.MainViewModelToViewInterface
import com.glowka.rafal.exchange.flow.intro.intro.IntroScreenStructure
import com.glowka.rafal.exchange.flow.intro.intro.IntroViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.architecture.FragmentFactory
import com.glowka.rafal.exchange.presentation.architecture.ScreenDialogBottomFragment
import com.glowka.rafal.exchange.presentation.architecture.ScreenDialogStructure
import com.glowka.rafal.exchange.presentation.architecture.ScreenFragment
import com.glowka.rafal.exchange.presentation.architecture.ScreenStructure
import com.glowka.rafal.exchange.presentation.utils.logTag

internal class IntroFragment : ScreenFragment<IntroViewModelToViewInterface.State, IntroViewModelToViewInterface.ViewEvents>(
  screenStructure = IntroScreenStructure
)

internal class MainFragment : ScreenFragment<MainViewModelToViewInterface.ViewState, MainViewModelToViewInterface.ViewEvents>(
  screenStructure = MainScreenStructure
)

internal class CurrencyPickerDialog : ScreenDialogBottomFragment<
    CurrencyPickerDialogViewModelToViewInterface.ViewState,
    CurrencyPickerDialogViewModelToViewInterface.ViewEvents
    >(
  screenStructure = CurrencyPickerScreenDialogStructure
)

class FragmentFactoryImpl : FragmentFactory {

  override fun createScreen(screenStructure: ScreenStructure<*, *, *, *>): Fragment {
    return when (screenStructure) {
      is IntroScreenStructure -> IntroFragment()
      is MainScreenStructure -> MainFragment()
      else -> throw RuntimeException("Missing fragment class for ${screenStructure.logTag}")
    }
  }

  override fun createScreenDialog(screenDialogStructure: ScreenDialogStructure<*, *, *, *>): DialogFragment {
    return when (screenDialogStructure) {
      is CurrencyPickerScreenDialogStructure -> CurrencyPickerDialog()
      else -> throw RuntimeException("Missing fragment class for ${screenDialogStructure.logTag}")
    }
  }
}