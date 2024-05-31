package com.pr.wishlist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Room 사용을 위해 @Entity 적용
@Entity(tableName="wish_table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "wish_title")
    val title: String = "",
    @ColumnInfo(name = "wish_desc")
    val description: String = ""
)


object DummyWish {
    val wishList = listOf(
        Wish(title = "asdfasdf", description ="Lorem ipsum Lorem ipsumLorem ipsumLorem ipsumLorem ipsum"),
        Wish(title = "asdfasdf", description ="Lorem ipsum Lorem ipsumLorem ipsumLorem ipsumLorem ipsum"),
        Wish(title = "asdfasdf", description ="Lorem ipsum Lorem ipsumLorem ipsumLorem ipsumLorem ipsum"),
        Wish(title = "asdfasdf", description ="Lorem ipsum Lorem ipsumLorem ipsumLorem ipsumLorem ipsum"),
        Wish(title = "asdfasdf", description ="Lorem ipsum Lorem ipsumLorem ipsumLorem ipsumLorem ipsum"),
    )
}
