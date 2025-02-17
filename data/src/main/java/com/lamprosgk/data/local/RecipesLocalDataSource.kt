package com.lamprosgk.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RecipesLocalDataSource {
    fun getAllRecipes(): Flow<List<RecipeEntity>> // flow to observe for db changes
    fun getRecipe(id: Int): Flow<RecipeEntity>
    suspend fun upsertRecipes(recipes: List<RecipeEntity>)
    suspend fun addToFavourites(recipeId: Int)
    suspend fun removeFromFavourites(recipeId: Int)
}

class RecipesLocalDataSourceImpl @Inject constructor(private val recipeDao: RecipeDao) :
    RecipesLocalDataSource {

    override fun getAllRecipes(): Flow<List<RecipeEntity>> = recipeDao.getAllRecipes()

    override suspend fun upsertRecipes(recipes: List<RecipeEntity>) =
        recipeDao.upsertRecipes(recipes)

    override fun getRecipe(id: Int): Flow<RecipeEntity> = recipeDao.getRecipe(id)

    override suspend fun addToFavourites(recipeId: Int) = recipeDao.addToFavourites(recipeId)

    override suspend fun removeFromFavourites(recipeId: Int) = recipeDao.removeFromFavourites(recipeId)
}