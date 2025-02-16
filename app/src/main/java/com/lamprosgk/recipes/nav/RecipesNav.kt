package com.lamprosgk.recipes.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lamprosgk.recipes.nav.NavRoutes.RECIPES
import com.lamprosgk.recipes.nav.NavRoutes.RECIPE_DETAIL
import com.lamprosgk.home.ui.screens.RecipesScreen
import com.lamprosgk.home.ui.screens.RecipesViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavDestination.Recipes.route,
        modifier = modifier
    ) {
        composable(NavDestination.Recipes.route) {
            val viewModel: RecipesViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            RecipesScreen(state = state, onIntent = viewModel::onIntent)
        }
        composable(NavDestination.RecipeDetail.route) {
//            DetailsScreen.View()
        }
    }
}

object NavRoutes {
    const val RECIPES = "recipes"
    const val RECIPE_DETAIL = "recipe_detail"
}

object NavArgs {
    const val RECIPE_ID = "recipe_id"
}

sealed class NavDestination(val route: String) {
    data object Recipes : NavDestination(RECIPES)
    data object RecipeDetail : NavDestination(RECIPE_DETAIL)
}