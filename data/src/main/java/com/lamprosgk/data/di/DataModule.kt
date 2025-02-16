package com.lamprosgk.data.di

import com.lamprosgk.data.RecipesRemoteDataSource
import com.lamprosgk.data.RecipesRemoteDataSourceImpl
import com.lamprosgk.data.RecipesRepositoryImpl
import com.lamprosgk.data.RecipesService
import com.lamprosgk.data.local.RecipeDao
import com.lamprosgk.data.local.RecipesLocalDataSource
import com.lamprosgk.data.local.RecipesLocalDataSourceImpl
import com.lamprosgk.domain.repository.RecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        recipesService: RecipesService
    ): RecipesRemoteDataSource {
        return RecipesRemoteDataSourceImpl(recipesService)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        recipesDao: RecipeDao
    ): RecipesLocalDataSource {
        return RecipesLocalDataSourceImpl(recipesDao)
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(
        localDataSource: RecipesLocalDataSource,
        remoteDataSource: RecipesRemoteDataSource
    ): RecipesRepository {
        return RecipesRepositoryImpl(localDataSource, remoteDataSource)
    }
}