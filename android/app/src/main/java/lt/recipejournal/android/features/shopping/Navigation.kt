package lt.recipejournal.android.features.shopping

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
import lt.recipejournal.android.core.navigation.Destinations

fun NavGraphBuilder.shoppingGraph(
    navController: NavController,
    contentPadding: PaddingValues
) {
    navigation<Destinations.ShoppingGraph>(startDestination = Destinations.ShoppingGraph.ShoppingList) {
        composable<Destinations.ShoppingGraph.ShoppingList> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Shopping List Screen")
            }
        }
    }
}