package lt.recipejournal.android.features.recipes.list

import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary

data class RecipeListUIState(
    val contentState: ContentState = ContentState.LOADING,
    val recipes: List<RecipeSummary> = emptyList(),
    val favourites: List<RecipeSummary> = emptyList(),
    val totalRecipeCount: Int = 0,
    val filterState: RecipeListFilterState = RecipeListFilterState()
)

data class RecipeListFilterState(
    val searchInput: String = "",
    val selectedCategories: Set<MealCategory> = emptySet(),
    val selectedCuisines: Set<Cuisine> = emptySet(),
    val cookingTime: CookingTime = CookingTime.ALL,
    val source: Source? = null,
    val sortBy: SortBy = SortBy.RECENTLY_ADDED
)

sealed interface LibraryLoad {
    data class Success(
        val summaries: List<RecipeSummary>,
        val totalCount: Int
    ) : LibraryLoad

    data object Failure : LibraryLoad
}

enum class SortBy {
    RECENTLY_ADDED,
    NAME,
    TIME
}

enum class Source {
    IMPORTED,
    CREATED
}

enum class CookingTime {
    ALL,
    UNDER_30,
    FROM_30_TO_60,
    FROM_60_TO_120,
    OVER_120
}

enum class ContentState {
    LOADING, ERROR, LOADED
}