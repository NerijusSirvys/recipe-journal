package lt.recipejournal.android.core.navigation

import kotlinx.serialization.Serializable

sealed interface Graph
data object Destinations {
    @Serializable
    data object RecipesGraph : Graph {
        @Serializable
        object RecipeList
    }

    @Serializable
    data object ShoppingGraph : Graph {
        @Serializable
        object ShoppingList
    }
}