package lt.recipejournal.android.features.recipes.list

import lt.recipejournal.android.features.recipes.data.models.CookingTime
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.SortBy
import lt.recipejournal.android.features.recipes.data.models.Source
import java.util.UUID

sealed interface RecipeListActions {
    data class SearchUpdated(val searchTerm: String) : RecipeListActions
    data class ToggleMealCategoryFilter(val category: MealCategory?) : RecipeListActions
    data class ToggleCuisineFilter(val cuisine: Cuisine?) : RecipeListActions
    data class ToggleCookingTimeFilter(val cookingTime: CookingTime?) : RecipeListActions
    data class ToggleSourceFilter(val source: Source?) : RecipeListActions
    data class ToggleSortByFilter(val sortBy: SortBy) : RecipeListActions
    data class ToggleFavourite(val recipeId: UUID) : RecipeListActions
    data class ShowFilterBottomSheet(val show: Boolean) : RecipeListActions
    data object ShowFilteredRecipes : RecipeListActions
    data object ResetFilters : RecipeListActions
}