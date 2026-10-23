package lt.recipejournal.android.features.recipes

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import lt.recipejournal.android.core.navigation.Destinations
import lt.recipejournal.android.features.recipes.list.RecipeListScreen


fun NavGraphBuilder.recipesGraph(
    navController: NavController,
    contentPadding: PaddingValues,
    reselectCount: () -> Int
) {
    navigation<Destinations.RecipesGraph>(startDestination = Destinations.RecipesGraph.RecipeList) {
        composable<Destinations.RecipesGraph.RecipeList> {
            RecipeListScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = contentPadding.calculateBottomPadding()),
                navController = navController,
                reselectCount = reselectCount
            )
        }
    }
}