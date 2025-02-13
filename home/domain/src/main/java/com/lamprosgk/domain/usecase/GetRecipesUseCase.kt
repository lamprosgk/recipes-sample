package com.lamprosgk.domain.usecase

import com.lamprosgk.domain.DataResult
import com.lamprosgk.domain.model.Recipe
import com.lamprosgk.domain.repository.RecipesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val recipesRepository: RecipesRepository
) {
    suspend operator fun invoke(): Flow<DataResult<List<Recipe>>> {
        return recipesRepository.getRecipes()
    }
}