package com.lamprosgk.data.repository

import android.util.Log
import com.lamprosgk.data.local.RecipesLocalDataSource
import com.lamprosgk.data.local.asDomainModel
import com.lamprosgk.data.remote.RecipesRemoteDataSource
import com.lamprosgk.data.remote.asEntityModel
import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val localDataSource: RecipesLocalDataSource,
    private val remoteDataSource: RecipesRemoteDataSource
) : RecipesRepository {

    override fun getRecipes(): Flow<Result<List<Recipe>>> = flow {
        emit(Result.Loading)

        try {
            // fetch data from remote and store it in database
            val remoteRecipes = remoteDataSource.getRecipes()
            localDataSource.upsertRecipes(remoteRecipes.map { it.asEntityModel() })
        } catch (e: Exception) {
            // log the error - could show a message too, but don't stop the flow
            Log.e("RecipesRepository", "Error fetching recipes: ${e.message}")
        }

        // emit the data from the database
        localDataSource.getAllRecipes().collect { entities ->
            emit(Result.Success(entities.map { it.asDomainModel() }))
        }
    }

    override fun getRecipe(id: Int): Flow<Result<Recipe>> = flow {
        emit(Result.Loading)

        try {
            localDataSource.getRecipe(id).collect { entity ->
                emit(Result.Success(entity.asDomainModel()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun addToFavourites(id: Int): Result<Unit> = try {
        localDataSource.addToFavourites(id)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    override suspend fun removeFromFavourites(id: Int): Result<Unit> = try {
        localDataSource.removeFromFavourites(id)
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }
}