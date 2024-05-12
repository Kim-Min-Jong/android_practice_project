package com.pr.recipe_app

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pr.recipe_app.data.Category
import com.pr.recipe_app.data.retrofitService
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _categoryState = mutableStateOf(RecipeState())
    val categoryState: State<RecipeState> = _categoryState

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                // get data from api
                val response = retrofitService.getCategories()
                // change state from data
                _categoryState.value = _categoryState.value.copy(
                    list = response.categories,
                    loading = false,
                    error = null
                )
            } catch (e: Exception) {
                _categoryState.value = _categoryState.value.copy(
                    loading = false,
                    error = "Error for ${e.message}"
                )
            }
        }
    }


    // state for ui, response
    data class RecipeState(
        val loading: Boolean = true,
        val list: List<Category> = emptyList(),
        val error: String? = null
    )
}
