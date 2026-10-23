package lt.recipejournal.android.features.recipes.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.RecipeQuery
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary
import lt.recipejournal.android.features.recipes.data.models.SortBy
import lt.recipejournal.android.features.recipes.data.models.matches
import java.time.Duration
import java.time.Instant
import java.util.UUID

class RecipeRepository {

    private val recipesFlow = MutableStateFlow(
        List(5) { index ->
            RecipeSummary(
                id = UUID.randomUUID(),
                name = "Recipe ${index + 1}",
                isFavourite = false,
                mealCategory = MealCategory.entries.random(),
                cuisine = Cuisine.entries.random(),
                servings = 1.rangeTo(5).random(),
                cookTime = Duration.ofMinutes((15..150).random().toLong()),
                ingredientCount = 3.rangeTo(25).random(),
                image = "https://images.unsplash.com/photo-1484723091739-30a097e8f929",
                createdOn = Instant.now().minusSeconds((1..300).random().toLong())
            )
        }
    )

    fun getRecipeSummaries(query: RecipeQuery): Flow<List<RecipeSummary>> {
        return recipesFlow.map { list ->
            val filtered = list.filter {
                val matchesSearch = query.searchText.isBlank() ||
                        it.name.contains(query.searchText, ignoreCase = true)

                val matchesCategory = query.categories.isEmpty() ||
                        it.mealCategory in query.categories

                val matchesCuisine = query.cuisines.isEmpty() ||
                        it.cuisine in query.cuisines

                val matchesCookingTime = query.cookingTimes.isEmpty() ||
                        query.cookingTimes.any { cookingTime ->
                            cookingTime.matches(it.cookTime)
                        }

                matchesSearch && matchesCategory && matchesCuisine && matchesCookingTime
            }

            when (query.sortBy) {
                SortBy.RECENTLY_ADDED -> filtered.sortedByDescending { it.createdOn }
                SortBy.NAME -> filtered.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
                SortBy.TIME -> filtered.sortedWith(compareBy({ it.cookTime }, { it.name }))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAvailableCuisineFilters(): Flow<List<Cuisine>> {
        return recipesFlow.mapLatest { it.map { r -> r.cuisine } }.distinctUntilChanged()
    }

    fun getRecipeCount(): Flow<Int> {
        return recipesFlow.map { it.size }.distinctUntilChanged()
    }


    fun setFavourite(id: UUID) {
        recipesFlow.update { list ->
            list.map { if (it.id == id) it.copy(isFavourite = !it.isFavourite) else it }
        }
    }
}