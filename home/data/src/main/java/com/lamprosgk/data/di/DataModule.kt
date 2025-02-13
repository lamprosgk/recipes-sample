package com.lamprosgk.data.di

import com.lamprosgk.data.RecipesRemoteDataSource
import com.lamprosgk.data.RecipesRemoteDataSourceImpl
import com.lamprosgk.data.RecipesRepositoryImpl
import com.lamprosgk.data.RecipesService
import com.lamprosgk.domain.repository.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
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

//    @Provides
//    @Singleton
//    fun provideRepository(
//        remoteDataSource: RecipesRemoteDataSource
//    ): com.lamprosgk.domain.repository.RecipesRepository {
//        return RecipesRepositoryImpl(remoteDataSource)
//    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRecipesRepository(
        recipesRepositoryImpl: RecipesRepositoryImpl // The implementation
    ): RecipesRepository // The interface
}

