package com.lamprosgk.detail.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.shared.mvi.MviIntent
import com.lamprosgk.shared.mvi.MviModel

sealed interface RecipeDetailViewState : MviModel {
    data class Success(val recipe: Recipe) : RecipeDetailViewState
    data object Loading : RecipeDetailViewState
    data class Error(val message: String) : RecipeDetailViewState
}

sealed interface RecipeDetailIntent : MviIntent {
    class AddToFavouritesIntent(val id: Int) : RecipeDetailIntent
    class RemoveFromFavouritesIntent(val id: Int) : RecipeDetailIntent
}

@Composable
fun RecipeDetailScreen(
    state: RecipeDetailViewState,
    onIntent: (RecipeDetailIntent) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Text(
        text = "Hello from RecipeDetailScreen",
    )
}