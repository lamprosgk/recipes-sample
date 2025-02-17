package com.lamprosgk.detail.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextAlign
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
                        Text(
                            text = name,
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )

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

@Composable
private fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        modifier = modifier.padding(16.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center
    )
}