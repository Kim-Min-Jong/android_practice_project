package com.pr.wishlist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// data access object 생성 - 디비에 접근하는 메소드
@Dao
interface WishDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addWish(wish: Wish)

    @Query("SELECT * FROM `WISH_TABLE`")
    fun getAllWishes(): Flow<List<Wish>>

    @Update
    fun updateWish(wish: Wish)

    @Delete
    fun deleteWish(wish: Wish)

    @Query("SELECT * FROM `WISH_TABLE` WHERE id = :id")
    fun getWishById(id: Long): Flow<Wish>

}
