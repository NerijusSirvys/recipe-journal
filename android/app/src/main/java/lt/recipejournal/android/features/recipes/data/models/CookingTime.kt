package lt.recipejournal.android.features.recipes.data.models

import androidx.annotation.StringRes
import lt.recipejournal.android.R
import java.time.Duration

enum class CookingTime {
    UNDER_30,
    FROM_30_TO_60,
    FROM_60_TO_120,
    OVER_120
}

fun CookingTime.matches(duration: Duration): Boolean {
    val minutes = duration.toMinutes()
    return when (this) {
        CookingTime.UNDER_30 -> minutes < 30
        CookingTime.FROM_30_TO_60 -> minutes in 30..60
        CookingTime.FROM_60_TO_120 -> minutes in 61..120
        CookingTime.OVER_120 -> minutes > 120
    }
}

@get:StringRes
val CookingTime.stringRes: Int
    get() = when (this) {
        CookingTime.UNDER_30 -> R.string.cooking_time_under_30_min
        CookingTime.FROM_30_TO_60 -> R.string.cooking_time_30_to_60_min
        CookingTime.FROM_60_TO_120 -> R.string.cooking_time_1_to_2_h
        CookingTime.OVER_120 -> R.string.cooking_time_over_2_h
    }
