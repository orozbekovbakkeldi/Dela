package com.example.dela.ui.home

import kotlinx.coroutines.CoroutineScope

interface CoroutineDebouncer {


    operator fun invoke(
        delay: Long = 300,
        coroutineScope: CoroutineScope,
        function: suspend () -> Unit
    )
}