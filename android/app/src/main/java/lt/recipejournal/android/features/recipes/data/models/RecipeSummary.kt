package lt.recipejournal.android.features.recipes.data.models

import java.net.URI
import java.time.Duration
import java.util.UUID

data class RecipeSummary(
    val id: UUID,
    val name: String,
    val image: String,
    val isFavourite: Boolean,
    val mealCategory: MealCategory,
    val cuisine: Cuisine,
    val servings: Int,
    val cookTime: Duration,
    val ingredientCount: Int
)
