package com.lamprosgk.home.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.home.usecase.GetRecipesUseCase
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
class RecipesViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel(), MviViewModel<RecipesViewState, RecipesIntent> {

    private val _state =
        MutableStateFlow<RecipesViewState>(RecipesViewState.Loading)
    override val state: StateFlow<RecipesViewState> = _state.asStateFlow()

    init {
        getRecipes()
    }

    override fun onIntent(intent: RecipesIntent) {
        when (intent) {
            is RecipesIntent.RecipeClickedIntent -> handleReceiptClicked(intent.id)
        }


    }

    private fun getRecipes() {
        viewModelScope.launch {
            getRecipesUseCase()
                .catch { handleGetRecipesError(it) }
                .collect { handleGetRecipesResult(it) }
        }
    }

    private fun handleReceiptClicked(id: Int) {

    }

    private fun handleGetRecipesError(exception: Throwable) {
        _state.update { RecipesViewState.Error(exception.message ?: "An error occurred") }
    }

    private fun handleGetRecipesResult(result: Result<List<Recipe>>) {
        _state.update {
            when (result) {
                is Result.Success -> RecipesViewState.Success(result.data)
                is Result.Error -> RecipesViewState.Error(
                    result.exception.message ?: "An error occurred"
                )

                Result.Loading -> RecipesViewState.Loading
            }
        }
    }
}