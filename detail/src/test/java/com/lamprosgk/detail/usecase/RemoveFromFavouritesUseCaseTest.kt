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

class RemoveFromFavouritesUseCaseTest {

    private lateinit var recipesRepository: RecipesRepository
    private lateinit var removeFromFavouritesUseCase: RemoveFromFavouritesUseCase

    @Before
    fun setup() {
        recipesRepository = mockk()
        removeFromFavouritesUseCase = RemoveFromFavouritesUseCase(recipesRepository)
    }

    @Test
    fun `invoke() returns success when recipe removing from favourites succeeds`() = runTest {
        val recipeId = 1
        coEvery { recipesRepository.removeFromFavourites(recipeId) } returns Result.Success(Unit)

        val result = removeFromFavouritesUseCase(recipeId)

        assertTrue(result is Result.Success)
    }

    @Test
    fun `invoke() returns error when recipe removing from favourites fails`() = runTest {
        val recipeId = 1
        val error = Exception("Failed to remove from favorites")
        coEvery { recipesRepository.removeFromFavourites(recipeId) } returns Result.Error(error)

        val result = removeFromFavouritesUseCase(recipeId)

        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).exception)
    }
}