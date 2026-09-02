package lt.recipejournal.android.features.recipes.data.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import lt.recipejournal.android.R
import lt.recipejournal.android.ui.theme.MealTagColorRole
import lt.recipejournal.android.ui.theme.mealTagColors

enum class MealCategory {
    BREAKFAST,
    LUNCH,
    DINNER,
    DESSERT,
    SNACK,
    SIDE
}

@get:Composable
val MealCategory.tagColors: MealTagColorRole
    get() = when (this) {
        MealCategory.BREAKFAST -> MaterialTheme.mealTagColors.breakfast
        MealCategory.LUNCH -> MaterialTheme.mealTagColors.lunch
        MealCategory.DINNER -> MaterialTheme.mealTagColors.dinner
        MealCategory.DESSERT -> MaterialTheme.mealTagColors.dessert
        MealCategory.SNACK -> MaterialTheme.mealTagColors.snack
        MealCategory.SIDE -> MaterialTheme.mealTagColors.side
    }

@get:StringRes
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