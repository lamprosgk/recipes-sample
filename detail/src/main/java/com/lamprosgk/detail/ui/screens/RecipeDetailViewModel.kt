package com.lamprosgk.detail.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamprosgk.detail.nav.RecipeDetailNavArgs.RECIPE_ID_ARG
import com.lamprosgk.detail.usecase.AddToFavouritesUseCase
import com.lamprosgk.detail.usecase.GetRecipeUseCase
import com.lamprosgk.detail.usecase.RemoveFromFavouritesUseCase
import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.shared.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRecipeUseCase: GetRecipeUseCase,
    private val addToFavouritesUseCase: AddToFavouritesUseCase,
    private val removeFromFavouritesUseCase: RemoveFromFavouritesUseCase
) : ViewModel(), MviViewModel<RecipeDetailViewState, RecipeDetailIntent> {

    private val recipeId: Int = checkNotNull(savedStateHandle[RECIPE_ID_ARG])


    private val _state =
        MutableStateFlow<RecipeDetailViewState>(RecipeDetailViewState.Loading)

    override val state: StateFlow<RecipeDetailViewState> = _state.asStateFlow()

    init {
        getRecipe(recipeId)
    }

    private fun getRecipe(id: Int) {
        viewModelScope.launch {
            getRecipeUseCase(id)
                .catch { handleGetRecipesError(it) }
                .collect { handleGetRecipesResult(it) }
        }
    }

    private fun handleGetRecipesResult(result: Result<Recipe>) {
        _state.update {
            when (result) {
                is Result.Success -> RecipeDetailViewState.Success(result.data)
                is Result.Error -> RecipeDetailViewState.Error(
                    result.exception.message ?: "An error occurred"
                )

                Result.Loading -> RecipeDetailViewState.Loading
            }
        }
    }

    private fun handleGetRecipesError(throwable: Throwable) {
        _state.update {
            RecipeDetailViewState.Error(
                throwable.message ?: "An error occurred, could not fetch recipe."
            )
        }
    }


    override fun onIntent(intent: RecipeDetailIntent) {
        when (intent) {
            is RecipeDetailIntent.AddToFavouritesIntent -> addToFavorites(intent.id)
            is RecipeDetailIntent.RemoveFromFavouritesIntent -> removeFromFavorites(intent.id)
        }
    }

    private fun addToFavorites(recipeId: Int) {
        viewModelScope.launch {
            when (val result = addToFavouritesUseCase(recipeId)) {
                is Result.Success -> {
                    // TODO
                }

                is Result.Error -> {
                    _state.update {
                        RecipeDetailViewState.Error("Failed to add to favorites: ${result.exception.message}")
                    }
                }

                is Result.Loading -> {
                    // not relevant here
                }
            }
        }
    }

    private fun removeFromFavorites(recipeId: Int) {
        viewModelScope.launch {
            when (val result = removeFromFavouritesUseCase(recipeId)) {
                is Result.Success -> {
                    // TODO
                }

                is Result.Error -> {
                    _state.update {
                        RecipeDetailViewState.Error("Failed to add to favorites: ${result.exception.message}")
                    }
                }

                is Result.Loading -> {
                    // not relevant here
                }
            }
        }
    }


}
