package lt.recipejournal.android.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.ShortNavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import lt.recipejournal.android.R
import lt.recipejournal.android.core.navigation.Destinations
import lt.recipejournal.android.core.navigation.Graph

data class BottomNavBarDestination(
    @StringRes val name: Int,
    @DrawableRes val icon: Int,
    val destinationGraph: Graph
)

val destinations = listOf(
    BottomNavBarDestination(
        name = R.string.bottom_nav_recipes_destination,
        icon = R.drawable.book_open_text,
        destinationGraph = Destinations.RecipesGraph
    ),

    BottomNavBarDestination(
        name = R.string.bottom_nav_shopping_list_destination,
        icon = R.drawable.shopping_basket,
        destinationGraph = Destinations.ShoppingGraph
    )
)

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    onNavigate: (Graph) -> Unit,
    currentGraph: Graph?
) {
    ShortNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            ShortNavigationBarItem(
                colors = ShortNavigationBarItemDefaults.colors().copy(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary
                ),
                selected = destination.destinationGraph == currentGraph,
                onClick = { onNavigate(destination.destinationGraph) },
                icon = {
                    Icon(
                        painter = painterResource(destination.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(destination.name)
                    )
                }
            )
        }
    }
}
