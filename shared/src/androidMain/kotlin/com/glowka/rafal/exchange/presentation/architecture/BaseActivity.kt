package com.glowka.rafal.exchange.presentation.architecture

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.glowka.rafal.exchange.di.inject

abstract class BaseActivity : AppCompatActivity() {

  private val navigator: FragmentNavigator by inject()

  @Suppress("DEPRECATION")
  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.setDecorFitsSystemWindows(false)
    } else {
      window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
         View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
         View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
  }

  override fun onResume() {
    super.onResume()
    navigator.attach(this)
  }

  override fun onPause() {
    navigator.detach()
    super.onPause()
  }

  override fun onBackPressed() {
    val currentFragment = supportFragmentManager.fragments.lastBaseFragment()
    currentFragment?.onBackPressed()
    return
  }
}

fun List<Fragment>.lastBaseFragment(): BaseFragment<*, *>? {
  return lastOrNull { fragment -> fragment is BaseFragment<*, *> } as? BaseFragment<*, *>
}