package com.lamprosgk.domain.repository

import com.lamprosgk.domain.DataResult
import com.lamprosgk.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    suspend fun getRecipes(): Flow<DataResult<List<Recipe>>>
}