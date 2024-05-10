package com.pr.mvvm_counter

data class CounterModel(
    var count: Int
)

// 데이터를 관리하는 곳
class CounterRepository {
    private var counter = CounterModel(0)

    fun getCounter() = counter

    fun increment()  = counter.count++

    fun decrement() = counter.count--
}
