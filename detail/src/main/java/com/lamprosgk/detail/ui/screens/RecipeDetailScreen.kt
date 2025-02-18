package com.lamprosgk.detail.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.shared.mvi.MviIntent
import com.lamprosgk.shared.mvi.MviModel
import com.lamprosgk.ui.ErrorMessage

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {


        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                is RecipeDetailViewState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is RecipeDetailViewState.Success -> {
                    with (state.recipe) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // recipe image
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                AsyncImage(
                                    model = thumbnailUrl,
                                    contentDescription = "Image of recipe",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(240.dp),
                                    contentScale = ContentScale.Crop,
                                    placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // title
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                }

                                // recipe info row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    RecipeInfoItem("Time", "$totalTimeMinutes min")
                                    RecipeInfoItem("Servings", "$numServings")
                                    RecipeInfoItem("Calories", "$calories cal")
                                }

                                // description
                                Text(
                                    text = description,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                // Ingredients
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "What you'll need",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    ingredients.forEach { ingredient ->
                                        Text(
                                            text = "• $ingredient",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                // instructions
                                Column(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "Let's make it!",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    instructionsSteps.forEachIndexed { index, step ->
                                        Text(
                                            text = "${index + 1}. $step",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }

                        FavoriteButton(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            isFavourite = isFavourite,
                            onFavoriteClick = {
                                val intent = if (isFavourite) {
                                    RecipeDetailIntent.RemoveFromFavouritesIntent(id)
                                } else {
                                    RecipeDetailIntent.AddToFavouritesIntent(id)
                                }
                                onIntent(intent)
                            }
                        )
                    }
                }

                is RecipeDetailViewState.Error -> {
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
private fun RecipeInfoItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onFavoriteClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onFavoriteClick
    ) {
        Icon(
            imageVector = if (isFavourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFavourite) "Remove from favourites" else "Add to favourites"
        )
    }
}

private val sampleRecipe = Recipe(
    id = 1,
    name = "Spaghetti",
    description = "Simple and delicious pasta dish.",
    ingredients = listOf(
        "Spaghetti",
        "Tomato sauce",
        "Parmesan"
    ),
    instructionsSteps = listOf(
        "Boil pasta",
        "Heat sauce",
        "Combine and serve"
    ),
    prepTimeMinutes = 10,
    totalTimeMinutes = 20,
    numServings = 2,
    calories = 400,
    thumbnailUrl = "https://example.com/spaghetti.jpg",
    tags = listOf("Pasta", "Italian"),
    isFavourite = false
)

@Preview(
    showBackground = true,
    showSystemUi = true
)@Composable
fun RecipeDetailScreenPreview() {
    MaterialTheme {
        RecipeDetailScreen(
            state = RecipeDetailViewState.Success(sampleRecipe),
            onIntent = {}
        )
    }
}