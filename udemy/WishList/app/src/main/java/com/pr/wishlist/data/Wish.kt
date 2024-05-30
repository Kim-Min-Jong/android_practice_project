package com.pr.wishlist.data

data class Wish(
    val id: Long = 0L,
    val title: String = "",
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
