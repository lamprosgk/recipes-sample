package com.lamprosgk.domain.repository

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
     fun getRecipes(): Flow<Result<List<Recipe>>>
}