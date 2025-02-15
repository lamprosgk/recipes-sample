package com.lamprosgk.data.local

import com.lamprosgk.data.RecipeData

interface RecipesLocalDataSource {
    suspend fun getRecipes(): List<RecipeData>
}