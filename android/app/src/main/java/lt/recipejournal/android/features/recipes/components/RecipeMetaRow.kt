package lt.recipejournal.android.features.recipes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R

@Composable
fun RecipeMetaRow(
    modifier: Modifier = Modifier,
    cookingTimeInMinutes: Int,
    servings: Int,
    ingredientCount: Int
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        MetaLabel(
            text = pluralStringResource(
                R.plurals.recipe_card_minutes,
                cookingTimeInMinutes,
                cookingTimeInMinutes
            ),
            icon = R.drawable.clock_4
        )

        MetaLabel(
            text = pluralStringResource(R.plurals.recipe_card_servings, servings, servings),
            icon = R.drawable.users
        )

        MetaLabel(
            text = pluralStringResource(
                R.plurals.recipe_card_ingredients,
                ingredientCount,
                ingredientCount
            ),
            icon = R.drawable.list_todo
        )
    }
}
