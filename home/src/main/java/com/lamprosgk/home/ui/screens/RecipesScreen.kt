package com.lamprosgk.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.shared.mvi.MviIntent
import com.lamprosgk.shared.mvi.MviModel
import com.lamprosgk.shared.ui.ErrorMessage
import com.lamprosgk.ui.R

sealed interface RecipesViewState : MviModel {
    data class Success(val recipes: List<Recipe> = emptyList()) : RecipesViewState
    data object Loading : RecipesViewState
    data class Error(val message: String) : RecipesViewState
}

sealed interface RecipesIntent : MviIntent {
    class RecipeClickedIntent(val id: Int) : RecipesIntent
}

@Composable
fun RecipesScreen(
    state: RecipesViewState,
    onIntent: (RecipesIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                RecipesViewState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )
                }

                is RecipesViewState.Success -> {
                    if (state.recipes.isEmpty()) {
                        EmptyRecipesText()
                    } else {
                        RecipesList(
                            recipes = state.recipes,
                            onRecipeClick = { recipeId ->
                                onIntent(RecipesIntent.RecipeClickedIntent(recipeId))
                            }
                        )
                    }
                }

                is RecipesViewState.Error -> {
                    ErrorMessage(
                        message = state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyRecipesText() {
    Text(
        text = stringResource(R.string.recipes_empty_message),
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun RecipesList(
    recipes: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = recipes,
            key = { recipe -> recipe.id }
        ) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = { onRecipeClick(recipe.id) }
            )
        }
    }
}

@Composable
private fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                if (recipe.isFavourite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite recipe",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                text = recipe.tags.take(3).joinToString(" • "),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (recipe.totalTimeMinutes > 0) {
                        Text(
                            text = stringResource(
                                R.string.recipe_item_info_time,
                                recipe.totalTimeMinutes
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Text(
                        text = stringResource(R.string.recipe_detail_info_calories, recipe.calories),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun RecipeScreenPreview() {
    val sampleRecipe = Recipe(
        id = 1,
        name = "Chicken Stir Fry",
        description = "Quick and easy stir fry",
        ingredients = listOf("Chicken", "Broccoli", "Soy sauce"),
        instructionsSteps = listOf("Step 1", "Step 2"),
        prepTimeMinutes = 10,
        totalTimeMinutes = 25,
        numServings = 4,
        calories = 350,
        thumbnailUrl = "",
        tags = listOf("Asian", "Quick", "Healthy"),
        isFavourite = true
    )

    MaterialTheme {
        RecipesScreen(
            state = RecipesViewState.Success(listOf(sampleRecipe)),
            onIntent = {}
        )
    }
}