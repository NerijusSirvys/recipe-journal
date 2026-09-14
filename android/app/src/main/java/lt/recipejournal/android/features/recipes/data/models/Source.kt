package lt.recipejournal.android.features.recipes.data.models

import androidx.annotation.StringRes
import lt.recipejournal.android.R


enum class Source {
    ALL,
    IMPORTED,
    CREATED
}


@get:StringRes
val Source.stringRes: Int
    get() = when (this) {
        Source.ALL -> R.string.all
        Source.IMPORTED -> R.string.source_imported
        Source.CREATED -> R.string.source_created
    }
