package com.pr.wishlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pr.wishlist.data.Wish
import com.pr.wishlist.data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class WishViewModel(
    private val wishRepository: WishRepository
): ViewModel() {

    // 입력 텍스트의 상태를 나타낼 변수
    var wishTitleState by mutableStateOf("")
    var wishDescriptionState by mutableStateOf("")

    fun onWishTitleChanged(newString: String) {
        wishTitleState = newString
    }

    fun onWishDescriptionChanged(newString: String) {
        wishDescriptionState = newString
    }

    // db

    lateinit var getAllWishes: Flow<List<Wish>>

    init {
        viewModelScope.launch {
            getAllWishes = wishRepository.getWish()
        }
    }

    // IO 디스패처를 통해 입출력최적화를 시행
    fun addWish(wish: Wish) = viewModelScope.launch(Dispatchers.IO) {
        wishRepository.addWish(wish)
    }

    fun updateWish(wish: Wish) = viewModelScope.launch(Dispatchers.IO) {
        wishRepository.updateWish(wish)
    }

    fun deleteWish(wish: Wish) = viewModelScope.launch(Dispatchers.IO) {
        wishRepository.deleteWish(wish)
    }

    fun getWishById(id: Long): Flow<Wish> = wishRepository.getWishById(id)





}
