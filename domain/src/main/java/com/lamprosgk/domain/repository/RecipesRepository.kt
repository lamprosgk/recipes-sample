package com.lamprosgk.domain.repository

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
     fun getRecipes(): Flow<Result<List<Recipe>>>
     fun getRecipe(id: Int): Flow<Result<Recipe>>
     suspend fun addToFavourites(id: Int): Result<Unit>
     suspend fun removeFromFavourites(id: Int): Result<Unit>
}