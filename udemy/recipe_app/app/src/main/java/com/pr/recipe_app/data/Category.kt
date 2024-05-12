package com.pr.recipe_app.data

data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

data class CategoriesResponses(
    val categories: List<Category>
)
