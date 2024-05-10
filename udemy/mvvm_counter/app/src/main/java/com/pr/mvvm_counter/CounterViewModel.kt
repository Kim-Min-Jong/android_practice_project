package com.pr.mvvm_counter

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CounterViewModel(
    private val repository: CounterRepository
) : ViewModel() {
    private var _count = mutableStateOf(repository.getCounter().count)
    val count = _count

    fun increment() {
        repository.increment()
        _count.value = repository.getCounter().count
    }

    fun decrement() {
        repository.decrement()
        _count.value = repository.getCounter().count
    }
}
