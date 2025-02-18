package com.lamprosgk.detail

import androidx.lifecycle.SavedStateHandle
import com.lamprosgk.detail.nav.RecipeDetailNavArgs.RECIPE_ID_ARG
import com.lamprosgk.detail.ui.screens.RecipeDetailIntent
import com.lamprosgk.detail.ui.screens.RecipeDetailViewModel
import com.lamprosgk.detail.ui.screens.RecipeDetailViewState
import com.lamprosgk.detail.usecase.AddToFavouritesUseCase
import com.lamprosgk.detail.usecase.GetRecipeUseCase
import com.lamprosgk.detail.usecase.RemoveFromFavouritesUseCase
import com.lamprosgk.domain.Result
import com.lamprosgk.domain.model.Recipe
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var getRecipeUseCase: GetRecipeUseCase
    private lateinit var addToFavouritesUseCase: AddToFavouritesUseCase
    private lateinit var removeFromFavouritesUseCase: RemoveFromFavouritesUseCase
    private lateinit var viewModel: RecipeDetailViewModel

    private val testRecipeId = 1

    // Sample test data
    private val sampleRecipe = Recipe(
        id = testRecipeId,
        name = "Recipe 1",
        description = "Description 1",
        ingredients = listOf("ingredient1", "ingredient2"),
        instructionsSteps = listOf("step1", "step2"),
        prepTimeMinutes = 10,
        totalTimeMinutes = 20,
        numServings = 2,
        calories = 300,
        thumbnailUrl = "url1",
        tags = listOf("tag1"),
        isFavourite = false
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle().apply {
            set(RECIPE_ID_ARG, testRecipeId)
        }
        getRecipeUseCase = mockk()
        addToFavouritesUseCase = mockk()
        removeFromFavouritesUseCase = mockk()


    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        coEvery { getRecipeUseCase(testRecipeId) } returns flowOf(Result.Loading)

        viewModel = createViewModel()

        assertTrue(viewModel.state.value is RecipeDetailViewState.Loading)
    }

    @Test
    fun `when recipe fetched successfully state updates to Success`() = runTest {
        coEvery { getRecipeUseCase(testRecipeId) } returns flowOf(Result.Success(sampleRecipe))

        viewModel = createViewModel()

        assertTrue(viewModel.state.value is RecipeDetailViewState.Success)
        assertEquals(
            sampleRecipe,
            (viewModel.state.value as RecipeDetailViewState.Success).recipe
        )
    }

    @Test
    fun `when error occurs while fetching recipe state updates to Error`() = runTest {
        val errorMessage = "Network error"
        coEvery { getRecipeUseCase(testRecipeId) } returns flowOf(Result.Error(Exception(errorMessage)))

        viewModel = createViewModel()

        assertTrue(viewModel.state.value is RecipeDetailViewState.Error)
        assertEquals(
            errorMessage,
            (viewModel.state.value as RecipeDetailViewState.Error).message
        )
    }

    @Test
    fun `add to favorites success maintains current state`() = runTest {
        coEvery { getRecipeUseCase(testRecipeId) } returns flowOf(Result.Success(sampleRecipe))
        coEvery { addToFavouritesUseCase(testRecipeId) } returns Result.Success(Unit)

        viewModel = createViewModel()
        val initialState = viewModel.state.value

        viewModel.onIntent(RecipeDetailIntent.AddToFavouritesIntent(testRecipeId))

        assertEquals(initialState, viewModel.state.value)
    }

    @Test
    fun `add to favorites failure updates state to Error`() = runTest {
        coEvery { getRecipeUseCase(testRecipeId) } returns flowOf(Result.Success(sampleRecipe))
        coEvery { addToFavouritesUseCase(testRecipeId) } returns Result.Error(Exception("Failed to add"))

        viewModel = createViewModel()

        viewModel.onIntent(RecipeDetailIntent.AddToFavouritesIntent(testRecipeId))

        assertTrue(viewModel.state.value is RecipeDetailViewState.Error)
        assertEquals(
            "Failed to add to favorites: Failed to add",
            (viewModel.state.value as RecipeDetailViewState.Error).message
        )
    }

    @Test
    fun `remove from favorites success maintains current state`() = runTest {
        coEvery { getRecipeUseCase(testRecipeId) } returns flowOf(Result.Success(sampleRecipe))
        coEvery { removeFromFavouritesUseCase(testRecipeId) } returns Result.Success(Unit)

        viewModel = createViewModel()
        val initialState = viewModel.state.value

        viewModel.onIntent(RecipeDetailIntent.RemoveFromFavouritesIntent(testRecipeId))

        assertEquals(initialState, viewModel.state.value)
    }

    @Test
    fun `remove from favorites failure updates state to Error`() = runTest {
        coEvery { getRecipeUseCase(testRecipeId) } returns flowOf(Result.Success(sampleRecipe))
        coEvery { removeFromFavouritesUseCase(testRecipeId) } returns Result.Error(Exception("Failed to remove"))

        viewModel = createViewModel()

        viewModel.onIntent(RecipeDetailIntent.RemoveFromFavouritesIntent(testRecipeId))

        assertTrue(viewModel.state.value is RecipeDetailViewState.Error)
        assertEquals(
            "Failed to remove from favorites: Failed to remove",
            (viewModel.state.value as RecipeDetailViewState.Error).message
        )
    }

    private fun createViewModel() = RecipeDetailViewModel(
        savedStateHandle = savedStateHandle,
        getRecipeUseCase = getRecipeUseCase,
        addToFavouritesUseCase = addToFavouritesUseCase,
        removeFromFavouritesUseCase = removeFromFavouritesUseCase
    )
}