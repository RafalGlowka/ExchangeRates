package com.glowka.rafal.exchange.modules

import com.glowka.rafal.exchange.AndroidFragmentFactory
import com.glowka.rafal.exchange.R
import com.glowka.rafal.exchange.presentation.FragmentFactoryImpl
import com.glowka.rafal.exchange.presentation.architecture.FragmentFactory
import com.glowka.rafal.exchange.presentation.architecture.FragmentNavigator
import com.glowka.rafal.exchange.presentation.architecture.FragmentNavigatorImpl
import com.glowka.rafal.exchange.presentation.architecture.ScreenNavigator
import org.koin.dsl.bind
import org.koin.dsl.module

val androidModule = module {
    single<FragmentFactory> {
        AndroidFragmentFactory(FragmentFactoryImpl())
    }

    single<FragmentNavigator> {
        FragmentNavigatorImpl(
            containerId = R.id.fragment_container,
            fragmentFactory = get()
        )
    } bind ScreenNavigator::class
}