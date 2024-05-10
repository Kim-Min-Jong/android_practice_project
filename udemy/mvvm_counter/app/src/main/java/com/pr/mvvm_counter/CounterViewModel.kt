package com.pr.mvvm_counter

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CounterViewModel: ViewModel() {
    private var _count = mutableStateOf(0)
    val count = _count

    fun increment() = _count.value++

    fun decrement() = _count.value--
}
