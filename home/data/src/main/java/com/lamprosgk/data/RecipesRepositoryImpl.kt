package com.lamprosgk.data

import com.lamprosgk.domain.DataResult
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val remoteDataSource: RecipesRemoteDataSource
): RecipesRepository {

    override suspend fun getRecipes(): Flow<DataResult<List<Recipe>>> = flow {
        emit(DataResult.Loading)

        try {
            val remoteRecipes = remoteDataSource.getRecipes()
            emit(DataResult.Success(remoteRecipes.map { it.asDomainModel() }))
        } catch (e: Exception) {
            emit(DataResult.Error(e))
        }
    }
}