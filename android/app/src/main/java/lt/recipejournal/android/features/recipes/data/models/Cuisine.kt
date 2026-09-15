package lt.recipejournal.android.features.recipes.data.models

import androidx.annotation.StringRes
import lt.recipejournal.android.R

enum class Cuisine {
    ITALIAN,
    CHINESE,
    JAPANESE,
    MEXICAN,
    INDIAN,
    THAI,
    FRENCH,
    SPANISH,
    GREEK,
    TURKISH,
    LITHUANIAN,
    EASTERN_EUROPEAN,
    AMERICAN
}

@get:StringRes
val Cuisine.stringRes: Int
    get() = when (this) {
        Cuisine.ITALIAN -> R.string.cuisine_italian
        Cuisine.CHINESE -> R.string.cuisine_chinese
        Cuisine.JAPANESE -> R.string.cuisine_japanese
        Cuisine.MEXICAN -> R.string.cuisine_mexican
        Cuisine.INDIAN -> R.string.cuisine_indian
        Cuisine.THAI -> R.string.cuisine_thai
        Cuisine.FRENCH -> R.string.cuisine_french
        Cuisine.SPANISH -> R.string.cuisine_spanish
        Cuisine.GREEK -> R.string.cuisine_greek
        Cuisine.TURKISH -> R.string.cuisine_turkish
        Cuisine.LITHUANIAN -> R.string.cuisine_lithuanian
        Cuisine.EASTERN_EUROPEAN -> R.string.cuisine_eastern_european
        Cuisine.AMERICAN -> R.string.cuisine_american
    }