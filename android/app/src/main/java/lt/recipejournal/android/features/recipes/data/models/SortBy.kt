package lt.recipejournal.android.features.recipes.data.models

import androidx.annotation.StringRes
import lt.recipejournal.android.R

enum class SortBy {
    RECENTLY_ADDED,
    NAME,
    TIME
}

@get:StringRes
val SortBy.stringRes: Int
    get() = when (this) {
        SortBy.RECENTLY_ADDED -> R.string.sort_by_recent
        SortBy.NAME -> R.string.sort_by_name
        SortBy.TIME -> R.string.sort_by_total_time
    }