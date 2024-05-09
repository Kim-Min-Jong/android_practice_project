package com.pr.shoppinglist.data

// Item Class
data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    // 수정 중인지
    var isEditing: Boolean = false
)
