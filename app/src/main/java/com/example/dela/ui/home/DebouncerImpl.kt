package com.example.dela.ui.home

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DebouncerImpl: CoroutineDebouncer {
    private var job : Job? = null
    override fun invoke(delay: Long, coroutineScope: CoroutineScope, function: suspend () -> Unit) {
        job?.cancel()
        job = coroutineScope.launch {
            delay(delay)
            function()
        }
    }

}