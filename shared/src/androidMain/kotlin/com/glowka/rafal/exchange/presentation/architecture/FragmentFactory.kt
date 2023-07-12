package com.glowka.rafal.exchange.presentation.architecture

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

interface FragmentFactory {
  fun createScreen(
    screenStructure: ScreenStructure<*, *, *, *>
  ): Fragment

  fun createScreenDialog(
    screenDialogStructure: ScreenDialogStructure<*, *, *, *>
  ): DialogFragment
}