package lt.recipejournal.android.features.recipes.data.models

import androidx.annotation.StringRes
import lt.recipejournal.android.R

enum class CookingTime {
    ALL,
    UNDER_30,
    FROM_30_TO_60,
    FROM_60_TO_120,
    OVER_120
}

@get:StringRes
val CookingTime.stringRes: Int
    get() = when (this) {
        CookingTime.ALL -> R.string.all
        CookingTime.UNDER_30 -> R.string.cooking_time_under30
        CookingTime.FROM_30_TO_60 -> R.string.cooking_time_30_to_60_min
        CookingTime.FROM_60_TO_120 -> R.string.cooking_time_1_to_2_h
        CookingTime.OVER_120 -> R.string.cooking_time_over_2_h
    }
