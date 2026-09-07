package lt.recipejournal.android.features.recipes.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary
import java.time.Duration
import java.util.UUID

class RecipeRepository {

    private val recipesFlow = MutableStateFlow(
        List(5) { index ->
            RecipeSummary(
                id = UUID.randomUUID(),
                name = "Greek Chicken Salad",
                isFavourite = false,
                mealCategory = MealCategory.entries.random(),
                cuisine = Cuisine.entries.random(),
                servings = 1.rangeTo(5).random(),
                cookTime = Duration.ofMinutes(25),
                ingredientCount = 3.rangeTo(25).random(),
                image = "https://images.unsplash.com/photo-1484723091739-30a097e8f929"
            )
        }
    )

    suspend fun getRecipeSummaries(): Flow<List<RecipeSummary>> {
        return recipesFlow.asStateFlow()
    }


    fun getRecipeCount(): Flow<Int> =
        recipesFlow.map { it.size }.distinctUntilChanged()


    fun setFavourite(id: UUID) {
        println("Toggling favourite: $id")
        recipesFlow.update { list ->
            list.map { if (it.id == id) it.copy(isFavourite = !it.isFavourite) else it }
        }
    }
}