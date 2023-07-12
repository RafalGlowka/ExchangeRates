package com.glowka.rafal.exchange

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.glowka.rafal.exchange.flow.intro.intro.IntroScreenStructure
import com.glowka.rafal.exchange.presentation.FragmentFactoryImpl
import com.glowka.rafal.exchange.presentation.architecture.FragmentFactory
import com.glowka.rafal.exchange.presentation.architecture.ScreenDialogStructure
import com.glowka.rafal.exchange.presentation.architecture.ScreenStructure
import com.glowka.rafal.exchange.presentation.flow.intro.AndroidIntroFragment

class AndroidFragmentFactory(private val baseFactory: FragmentFactoryImpl) : FragmentFactory {
  override fun createScreen(screenStructure: ScreenStructure<*, *, *, *>): Fragment {
    return when (screenStructure) {
      IntroScreenStructure -> AndroidIntroFragment()
      else -> baseFactory.createScreen(screenStructure)
    }
  }

  override fun createScreenDialog(screenDialogStructure: ScreenDialogStructure<*, *, *, *>): DialogFragment {
    return baseFactory.createScreenDialog(screenDialogStructure)
  }
}