package com.lamprosgk.home.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
}

@Composable
fun RecipeDetailScreen() {
    var text by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { androidx.compose.material3.Text("Enter Text") }, // Optional label
            modifier = Modifier
                .padding(16.dp) // Add some padding around the text field
                .fillMaxSize() // Make it fill the screen (or adjust as needed)

        )
    }
}