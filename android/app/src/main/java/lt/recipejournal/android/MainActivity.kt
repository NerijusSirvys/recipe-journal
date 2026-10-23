package lt.recipejournal.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import lt.recipejournal.android.components.BottomNavigationBar
import lt.recipejournal.android.components.destinations
import lt.recipejournal.android.core.navigation.Destinations
import lt.recipejournal.android.core.navigation.Graph
import lt.recipejournal.android.features.recipes.recipesGraph
import lt.recipejournal.android.features.shopping.shoppingGraph
import lt.recipejournal.android.ui.theme.AndroidTheme
import lt.recipejournal.android.ui.theme.LightBackground
import lt.recipejournal.android.ui.theme.LightInverseSurface


class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = LightBackground.toArgb(),
                darkScrim = LightInverseSurface.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = LightBackground.toArgb(),
                darkScrim = LightInverseSurface.toArgb()
            )
        )
        setContent {
            AndroidTheme {
                val navController = rememberNavController()

                val currentDestination =
                    navController.currentBackStackEntryAsState().value?.destination

                val currentGraph = destinations
                    .map { it.destinationGraph }
                    .firstOrNull { graph -> currentDestination?.hierarchy?.any { it.hasRoute(graph::class) } == true }

                // Bumped each time the already selected tab is tapped again
                val tabReselections = remember { mutableStateMapOf<Graph, Int>() }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            currentGraph = currentGraph,
                            onNavigate = { graph ->
                                if (graph == currentGraph) {
                                    tabReselections[graph] = (tabReselections[graph] ?: 0) + 1
                                } else {
                                    navController.navigate(graph) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.RecipesGraph
                    ) {
                        recipesGraph(
                            navController = navController,
                            contentPadding = innerPadding,
                            reselectCount = { tabReselections[Destinations.RecipesGraph] ?: 0 }
                        )
                        shoppingGraph(navController = navController, contentPadding = innerPadding)
                    }
                }
            }
        }
    }
}