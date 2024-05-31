package com.pr.wishlist

import android.content.Context
import androidx.room.Room
import com.pr.wishlist.data.WishDatabase
import com.pr.wishlist.data.WishRepository

// for db initializing
object Graph {

    lateinit var database: WishDatabase

    val wishRepository by lazy {
        WishRepository(wishDao = database.wishDao())
    }

    // 초기화
    fun provide(context: Context) {
        database = Room.databaseBuilder(context, WishDatabase::class.java, "wishlist.db")
            .build()
    }
}
