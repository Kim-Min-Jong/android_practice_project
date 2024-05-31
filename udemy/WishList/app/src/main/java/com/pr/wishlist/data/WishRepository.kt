package com.pr.wishlist.data

import kotlinx.coroutines.flow.Flow

// repository를 통해 데이터를 중계하면서 뷰모델은 데이터의 출처가 어디인지 알 필요없지 데이터만 사용하면 됨
class WishRepository(
    private val wishDao: WishDao
) {
    suspend fun addWish(wish: Wish) = wishDao.addWish(wish)

    suspend fun getWish(): Flow<List<Wish>> = wishDao.getAllWishes()

    suspend fun getWishById(id: Long): Flow<Wish> = wishDao.getWishById(id)

    suspend fun updateWish(wish: Wish) = wishDao.updateWish(wish)

    suspend fun deleteWish(wish: Wish) = wishDao.deleteWish(wish)
}
