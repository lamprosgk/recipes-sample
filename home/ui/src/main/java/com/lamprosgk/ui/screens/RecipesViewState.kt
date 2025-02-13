package com.lamprosgk.ui.screens

import com.lamprosgk.domain.model.Recipe


sealed interface RecipesViewState {
    data class Success(val recipes: List<Recipe> = emptyList()) : RecipesViewState
    data object Loading : RecipesViewState
    data class Error(val message: String) : RecipesViewState
}
