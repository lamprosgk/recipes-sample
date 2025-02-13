package com.lamprosgk.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamprosgk.domain.DataResult
import com.lamprosgk.domain.usecase.GetRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _state =
        MutableStateFlow<RecipesViewState>(RecipesViewState.Loading)
    val state: StateFlow<RecipesViewState> = _state.asStateFlow()

    init {
        getRecipes()
    }

    fun getRecipes() {
        viewModelScope.launch {
            getRecipesUseCase().collect { recipes ->
                _state.value = when (recipes) {
                    is DataResult.Success -> RecipesViewState.Success(recipes.data)
                    is DataResult.Error -> RecipesViewState.Error(recipes.exception.message ?: "")
                    DataResult.Loading -> RecipesViewState.Loading
                }
            }
        }
    }
}