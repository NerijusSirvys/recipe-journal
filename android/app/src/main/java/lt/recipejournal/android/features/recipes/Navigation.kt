package lt.recipejournal.android.features.recipes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
object RecipesGraph {
    @Serializable
    object RecipeList
}

fun NavGraphBuilder.recipesGraph(
    navController: NavController,
    contentPadding: PaddingValues
) {
    navigation<RecipesGraph>(startDestination = RecipesGraph.RecipeList) {
        composable<RecipesGraph.RecipeList> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Recipe List Screen")
            }
        }
    }
}