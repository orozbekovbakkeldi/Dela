package com.example.dela.fake

import com.example.dela.ui.home.CoroutineDebouncer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

class CoroutinesDebouncerFake : CoroutineDebouncer {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(delay: Long, coroutineScope: CoroutineScope, function: suspend () -> Unit) {
        runTest { function() }
    }
}