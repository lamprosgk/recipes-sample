package com.lamprosgk.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lamprosgk.domain.DataResult
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.usecase.GetRecipesUseCase
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

    private fun handleGetRecipesResult(dataResult: DataResult<List<Recipe>>) {
        _state.update {
            when (dataResult) {
                is DataResult.Success -> RecipesViewState.Success(dataResult.data)
                is DataResult.Error -> RecipesViewState.Error(
                    dataResult.exception.message ?: "An error occurred"
                )

                DataResult.Loading -> RecipesViewState.Loading
            }
        }
    }
}