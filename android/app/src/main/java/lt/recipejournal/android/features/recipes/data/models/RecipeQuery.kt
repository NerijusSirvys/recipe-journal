package lt.recipejournal.android.features.recipes.data.models

data class RecipeQuery(
    val searchText: String = "",
    val categories: Set<MealCategory> = emptySet(),
    val cuisines: Set<Cuisine> = emptySet(),
    val cookingTimes: Set<CookingTime> = emptySet(),
    val sources: Set<Source> = emptySet(),
    val sortBy: SortBy = SortBy.RECENTLY_ADDED,
)
