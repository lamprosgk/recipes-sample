package com.lamprosgk.data

import android.util.Log
import com.lamprosgk.data.local.RecipesLocalDataSource
import com.lamprosgk.data.local.asDomainModel
import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val localDataSource: RecipesLocalDataSource,
    private val remoteDataSource: RecipesRemoteDataSource
) : RecipesRepository {

    override fun getRecipes(): Flow<Result<List<Recipe>>> = flow {
        emit(Result.Loading)

        try {
            val remoteRecipes = remoteDataSource.getRecipes()
            localDataSource.insertRecipes(remoteRecipes.map { it.asEntityModel() })
        } catch (e: Exception) {
            Log.e(
                "RecipesRepository",
                "Error while fetching recipes, loading local data: ${e.message}"
            )
            val cachedRecipes = localDataSource.getAllRecipes().first()
            if (cachedRecipes.isEmpty()) {
                emit(Result.Error(e))
                return@flow
            }
        }

        // Collect and emit from database
        localDataSource.getAllRecipes().collect { entities ->
            emit(Result.Success(entities.map { it.asDomainModel() }))
        }
    }

    override fun getRecipe(id: Int): Flow<Result<Recipe>> = flow {
        emit(Result.Loading)

        try {
            val recipe = localDataSource.getRecipe(id).first()
            emit(Result.Success(recipe.asDomainModel()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}