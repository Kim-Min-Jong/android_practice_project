package com.pr.recipe_app.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface ApiService {

    @GET("/categories.php")
    suspend fun getCategories(): CategoriesResponses
}

// retrofit object
private val retrofit = Retrofit.Builder()
    .baseUrl("https://www.themealdb.com/api/json/v1/1")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

// exposed retroift object (create object)
val retrofitService = retrofit.create(ApiService::class.java)

