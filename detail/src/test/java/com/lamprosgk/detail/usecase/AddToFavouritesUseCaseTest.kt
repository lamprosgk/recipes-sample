package com.lamprosgk.detail.usecase

import com.lamprosgk.domain.Result
import com.lamprosgk.domain.repository.RecipesRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddToFavouritesUseCaseTest {

    private lateinit var recipesRepository: RecipesRepository
    private lateinit var addToFavouritesUseCase: AddToFavouritesUseCase

    @Before
    fun setup() {
        recipesRepository = mockk()
        addToFavouritesUseCase = AddToFavouritesUseCase(recipesRepository)
    }

    @Test
    fun `invoke() returns success when recipe added to favourites successfully`() = runTest {
        val recipeId = 1
        coEvery { recipesRepository.addToFavourites(recipeId) } returns Result.Success(Unit)

        val result = addToFavouritesUseCase(recipeId)

        assertTrue(result is Result.Success)
    }

    @Test
    fun `invoke() returns error when adding recipe to favourites fails`() = runTest {
        val recipeId = 1
        val error = Exception("Failed to add to favorites")
        coEvery { recipesRepository.addToFavourites(recipeId) } returns Result.Error(error)

        val result = addToFavouritesUseCase(recipeId)

        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).exception)
    }
}