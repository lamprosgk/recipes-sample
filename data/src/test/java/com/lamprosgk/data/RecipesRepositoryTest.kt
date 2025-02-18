package com.lamprosgk.data

import com.lamprosgk.data.local.RecipeEntity
import com.lamprosgk.data.local.RecipesLocalDataSource
import com.lamprosgk.data.remote.RecipeData
import com.lamprosgk.data.remote.RecipesRemoteDataSource
import com.lamprosgk.data.repository.RecipesRepositoryImpl
import com.lamprosgk.domain.Result
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipesRepositoryImplTest {

    private lateinit var repository: RecipesRepositoryImpl
    private lateinit var localDataSource: RecipesLocalDataSource
    private lateinit var remoteDataSource: RecipesRemoteDataSource

    private val testRecipeData = RecipeData(
        id = 1,
        name = "Test Recipe",
        description = "Test Description",
        prepTimeMinutes = 15,
        totalTimeMinutes = 30,
        numServings = 4,
        thumbnailUrl = "https://example.com/image.jpg"
    )

    private val testRecipeEntity = RecipeEntity(
        id = 1,
        name = "Test Recipe",
        description = "Test Description",
        instructionsSteps = "Step 1||Step 2",
        ingredients = "Ingredient 1||Ingredient 2",
        prepTimeMinutes = 15,
        totalTimeMinutes = 30,
        numServings = 4,
        calories = 300,
        thumbnailUrl = "https://example.com/image.jpg",
        tags = "tag1,tag2",
        isFavourite = false
    )

    @Before
    fun setup() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        repository = RecipesRepositoryImpl(localDataSource, remoteDataSource)

        mockkStatic(android.util.Log::class)
        every { android.util.Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getRecipes() should emit loading and success states with data from local source`() =
        runTest {
            val remoteRecipes = listOf(testRecipeData)
            val localRecipes = listOf(testRecipeEntity)

            coEvery { remoteDataSource.getRecipes() } returns remoteRecipes
            coEvery { localDataSource.upsertRecipes(any()) } just Runs
            every { localDataSource.getAllRecipes() } returns flowOf(localRecipes)

            val results = repository.getRecipes().toList()

            assertEquals(2, results.size)
            assertEquals(Result.Loading, results[0])
            assert(results[1] is Result.Success)

            coVerify {
                remoteDataSource.getRecipes()
                localDataSource.upsertRecipes(any())
                localDataSource.getAllRecipes()
            }
        }

    @Test
    fun `getRecipes() should emit local data when remote fetch fails`() = runTest {
        val localRecipes = listOf(testRecipeEntity)
        coEvery { remoteDataSource.getRecipes() } throws Exception("Network error")
        every { localDataSource.getAllRecipes() } returns flowOf(localRecipes)

        val results = repository.getRecipes().toList()

        assertEquals(2, results.size)
        assertEquals(Result.Loading, results[0])
        assert(results[1] is Result.Success)

        coVerify {
            remoteDataSource.getRecipes()
            localDataSource.getAllRecipes()
        }
    }

    @Test
    fun `getRecipe() should emit loading and success states`() = runTest {
        val recipeId = 1
        every { localDataSource.getRecipe(recipeId) } returns flowOf(testRecipeEntity)

        val results = repository.getRecipe(recipeId).toList()

        assertEquals(2, results.size)
        assertEquals(Result.Loading, results[0])
        assert(results[1] is Result.Success)

        verify { localDataSource.getRecipe(recipeId) }
    }

    @Test
    fun `getRecipe() should emit error when local source fails`() = runTest {
        val recipeId = 1
        val exception = Exception("Database error")
        every { localDataSource.getRecipe(recipeId) } throws exception

        val results = repository.getRecipe(recipeId).toList()

        assertEquals(2, results.size)
        assertEquals(Result.Loading, results[0])
        assert(results[1] is Result.Error)
        assertEquals(exception, (results[1] as Result.Error).exception)
    }

    @Test
    fun `addToFavourites() should return success when recipe added to favourites`() = runTest {
        val recipeId = 1
        coEvery { localDataSource.addToFavourites(recipeId) } just Runs

        val result = repository.addToFavourites(recipeId)

        assert(result is Result.Success)
        coVerify { localDataSource.addToFavourites(recipeId) }
    }

    @Test
    fun `addToFavourites() should return error  when adding to favourites fails`() = runTest {
        val recipeId = 1
        val exception = Exception("Database error")
        coEvery { localDataSource.addToFavourites(recipeId) } throws exception

        val result = repository.addToFavourites(recipeId)

        assert(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }

    @Test
    fun `removeFromFavourite()s should return success when recipe removed from favourites`() = runTest {
        val recipeId = 1
        coEvery { localDataSource.removeFromFavourites(recipeId) } just Runs

        val result = repository.removeFromFavourites(recipeId)

        assert(result is Result.Success)
        coVerify { localDataSource.removeFromFavourites(recipeId) }
    }

    @Test
    fun `removeFromFavourites() should return error when remove from favourites fails`() = runTest {
        val recipeId = 1
        val exception = Exception("Database error")
        coEvery { localDataSource.removeFromFavourites(recipeId) } throws exception

        val result = repository.removeFromFavourites(recipeId)

        assert(result is Result.Error)
        assertEquals(exception, (result as Result.Error).exception)
    }
}

