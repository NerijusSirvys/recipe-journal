package lt.recipejournal.android.features.recipes.list

import lt.recipejournal.android.features.recipes.data.models.CookingTime
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary
import lt.recipejournal.android.features.recipes.data.models.SortBy
import lt.recipejournal.android.features.recipes.data.models.Source

data class RecipeListUIState(
    val contentState: ContentState = ContentState.LOADING,
    val recipes: List<RecipeSummary> = emptyList(),
    val favourites: List<RecipeSummary> = emptyList(),
    val totalRecipeCount: Int = 0,
    val showFilterBottomSheet: Boolean = false,
    val filterState: RecipeListFilterState = RecipeListFilterState(),
    val availableCuisineFilters: Set<Cuisine> = emptySet(),
    val availableCookingTimeFilters: Set<CookingTime> = emptySet(),
    val availableSourceFilters: Set<Source> = emptySet(),
    val availableSortByFilter: Set<SortBy> = emptySet()
)

data class RecipeListFilterState(
    val searchInput: String = "",
    val selectedCategories: Set<MealCategory> = emptySet(),
    val selectedCuisines: Set<Cuisine> = setOf(Cuisine.ALL),
    val selectedCookingTimeFilters: Set<CookingTime> = setOf(CookingTime.ALL),
    val selectedSourceFilters: Set<Source> = setOf(Source.ALL),
    val sortBy: SortBy = SortBy.RECENTLY_ADDED
)

sealed interface LibraryLoad {
    data class Success(
        val summaries: List<RecipeSummary>,
        val totalCount: Int,
        val cuisineFilters: List<Cuisine>
    ) : LibraryLoad

    data object Failure : LibraryLoad
    data object Empty : LibraryLoad
}

enum class ContentState {
    LOADING, ERROR, LOADED, EMPTY
}