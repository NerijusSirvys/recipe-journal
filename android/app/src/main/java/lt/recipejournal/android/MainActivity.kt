package lt.recipejournal.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import lt.recipejournal.android.components.BottomNavigationBar
import lt.recipejournal.android.components.destinations
import lt.recipejournal.android.core.navigation.Destinations
import lt.recipejournal.android.features.recipes.recipesGraph
import lt.recipejournal.android.features.shopping.shoppingGraph
import lt.recipejournal.android.ui.theme.AndroidTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AndroidTheme {
                val navController = rememberNavController()

                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination

                val currentGraph = destinations
                    .map { it.destinationGraph }
                    .firstOrNull { graph -> currentDestination?.hierarchy?.any { it.hasRoute(graph::class) } == true }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            currentGraph = currentGraph,
                            onNavigate = {
                                navController.navigate(it) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.RecipesGraph
                    ) {
                        recipesGraph(navController = navController, contentPadding = innerPadding)
                        shoppingGraph(navController = navController, contentPadding = innerPadding)
                    }
                }
            }
        }
    }
}