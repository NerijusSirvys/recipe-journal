package lt.recipejournal.android.features.recipes.data.models

import androidx.annotation.DrawableRes
import lt.recipejournal.android.R

enum class MealCategory {
    BREAKFAST,
    LUNCH,
    DINNER,
    DESSERT,
    SNACK,
    SIDE
}

val MealCategory.stringRes: Int
    get() = when (this) {
        MealCategory.BREAKFAST -> R.string.mealCategory_breakfast
        MealCategory.LUNCH -> R.string.mealCategory_lunch
        MealCategory.DINNER -> R.string.mealCategory_dinner
        MealCategory.DESSERT -> R.string.mealCategory_dessert
        MealCategory.SNACK -> R.string.mealCategory_snack
        MealCategory.SIDE -> R.string.mealCategory_side
    }

@get:DrawableRes
val MealCategory.iconRes: Int
    get() = when (this) {
        MealCategory.BREAKFAST -> R.drawable.sun
        MealCategory.LUNCH -> R.drawable.soup
        MealCategory.DINNER -> R.drawable.cooking
        MealCategory.DESSERT -> R.drawable.cake_slice
        MealCategory.SNACK -> R.drawable.apple
        MealCategory.SIDE -> R.drawable.salad
    }