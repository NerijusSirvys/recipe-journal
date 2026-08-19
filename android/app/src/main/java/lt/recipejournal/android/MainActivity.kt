package lt.recipejournal.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import lt.recipejournal.android.features.recipes.RecipesGraph
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = RecipesGraph) {
                        recipesGraph(navController = navController, contentPadding = innerPadding)
                        shoppingGraph(navController = navController, contentPadding = innerPadding)
                    }
                }
            }
        }
    }
}