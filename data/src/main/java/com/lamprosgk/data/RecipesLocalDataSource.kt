package com.lamprosgk.data

interface RecipesLocalDataSource {
    suspend fun getRecipes(): List<RecipeData>
}