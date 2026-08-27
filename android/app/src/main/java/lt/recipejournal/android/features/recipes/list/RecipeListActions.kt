package lt.recipejournal.android.features.recipes.list

import lt.recipejournal.android.features.recipes.data.models.MealCategory

sealed interface RecipeListActions {
    data class SearchUpdated(val searchTerm: String) : RecipeListActions
    data class ToggleMealCategoryFilter(val category: MealCategory?) : RecipeListActions
}