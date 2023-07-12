@file:Suppress("SwallowedException", "ArgumentListWrapping")

package com.glowka.rafal.exchange.di

import com.glowka.rafal.exchange.presentation.architecture.BaseBottomSheetDialogFragment
import com.glowka.rafal.exchange.presentation.architecture.BaseDialogFragment
import com.glowka.rafal.exchange.presentation.architecture.BaseFragment
import com.glowka.rafal.exchange.presentation.architecture.BaseViewModel
import com.glowka.rafal.exchange.presentation.architecture.ViewEvent
import com.glowka.rafal.exchange.presentation.architecture.ViewModelToViewInterface
import com.glowka.rafal.exchange.presentation.architecture.ViewState
import com.glowka.rafal.exchange.presentation.utils.logTag
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.qualifier.StringQualifier
import org.koin.java.KoinJavaComponent.getKoin

fun BaseFragment<*, *>.getScopeName() = arguments?.getString(BaseFragment.ARG_SCOPE)
fun BaseFragment<*, *>.getScreenTag() = arguments?.getString(BaseFragment.ARG_SCREEN_TAG)
fun BaseDialogFragment<*, *>.getScopeName() = arguments?.getString(BaseDialogFragment.ARG_SCOPE)
fun BaseDialogFragment<*, *>.getScreenTag() =
  arguments?.getString(BaseDialogFragment.ARG_SCREEN_TAG)

fun BaseBottomSheetDialogFragment<*, *>.getScopeName() =
  arguments?.getString(BaseDialogFragment.ARG_SCOPE)

fun BaseBottomSheetDialogFragment<*, *>.getScreenTag() =
  arguments?.getString(BaseDialogFragment.ARG_SCREEN_TAG)

fun <VMSTATE : ViewState, VMEVENTS : ViewEvent>
    BaseFragment<VMSTATE, VMEVENTS>.injectViewModel(): Lazy<ViewModelToViewInterface<VMSTATE, VMEVENTS>> {
  return lazy(LazyThreadSafetyMode.NONE) {
    val scopeName = getScopeName() ?: throw KoinInjectionException("Missing scopeName for ${this::logTag}")
    val screenTag = getScreenTag() ?: throw KoinInjectionException("Missing screenTag for ${this::logTag}")
    val scope = getKoin().getScopeOrNull(scopeName) ?: throw KoinInjectionException(
      "Missing scope $scopeName definition in Di for ${this::logTag}"
    )
    val qualifier = StringQualifier(screenTag)

    val viewModel = try {
      scope.get(clazz = BaseViewModel::class, qualifier = qualifier, parameters = null) as? BaseViewModel<*, *, *, *>
        ?: throw TypeCastException("Incorrect ViewModel base type")
    } catch (error: NoBeanDefFoundException) {
      throw java.lang.RuntimeException("Missing viewModel for screen $screenTag in scope $scopeName")
    }

//    viewModel.lifecycleOwner = this

    @Suppress("UNCHECKED_CAST")
    return@lazy viewModel as? ViewModelToViewInterface<VMSTATE, VMEVENTS> ?: throw TypeCastException("Incorrect ViewModel type")
  }
}

fun <VMSTATE : ViewState, VEVENTS : ViewEvent>
    BaseDialogFragment<VMSTATE, VEVENTS>.injectViewModel(): Lazy<ViewModelToViewInterface<VMSTATE, VEVENTS>> {
  return lazy(LazyThreadSafetyMode.NONE) {
    val scopeName = getScopeName() ?: throw KoinInjectionException("Missing scopeName for ${this::logTag}")
    val screenTag = getScreenTag() ?: throw KoinInjectionException("Missing screenTag for ${this::logTag}")
    val scope = getKoin().getScopeOrNull(scopeName) ?: throw KoinInjectionException(
      "Missing scope $scopeName definition in Di for ${this::logTag}"
    )
    val qualifier = StringQualifier(screenTag)

    val viewModel = try {
      scope.get(clazz = BaseViewModel::class, qualifier = qualifier, parameters = null) as? BaseViewModel<*, *, *, *>
        ?: throw TypeCastException("Incorrect ViewModel base type")
    } catch (error: NoBeanDefFoundException) {
      throw java.lang.RuntimeException("Missing viewModel for screen $screenTag in scope $scopeName")
    }

//    viewModel.lifecycleOwner = this

    @Suppress("UNCHECKED_CAST")
    return@lazy viewModel as? ViewModelToViewInterface<VMSTATE, VEVENTS> ?: throw TypeCastException("Incorrect ViewModel type")
  }
}

fun <VMSTATE : ViewState, VEVENTS : ViewEvent>
    BaseBottomSheetDialogFragment<VMSTATE, VEVENTS>.injectViewModel(): Lazy<ViewModelToViewInterface<VMSTATE, VEVENTS>> {
  return lazy(LazyThreadSafetyMode.NONE) {
    val scopeName = getScopeName() ?: throw KoinInjectionException("Missing scopeName for ${this::logTag}")
    val screenTag = getScreenTag() ?: throw KoinInjectionException("Missing screenTag for ${this::logTag}")
    val scope = getKoin().getScopeOrNull(scopeName) ?: throw KoinInjectionException(
      "Missing scope $scopeName definition in Di for ${this::logTag}"
    )
    val qualifier = StringQualifier(screenTag)

    val viewModel = try {
      scope.get(clazz = BaseViewModel::class, qualifier = qualifier, parameters = null) as? BaseViewModel<*, *, *, *>
        ?: throw TypeCastException("Incorrect ViewModel base type")
    } catch (error: NoBeanDefFoundException) {
      throw java.lang.RuntimeException("Missing viewModel for screen $screenTag in scope $scopeName")
    }

//    viewModel.lifecycleOwner = this

    @Suppress("UNCHECKED_CAST")
    return@lazy viewModel as? ViewModelToViewInterface<VMSTATE, VEVENTS> ?: throw TypeCastException("Incorrect ViewModel type")
  }
}