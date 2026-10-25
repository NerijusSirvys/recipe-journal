package lt.recipejournal.android.features.recipes.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import lt.recipejournal.android.R
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.stringRes
import lt.recipejournal.android.features.recipes.data.models.tagColors

@Composable
fun RecipeTagRow(
    modifier: Modifier = Modifier,
    mealCategory: MealCategory,
    cuisine: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MealCategoryChip(
            containerColor = mealCategory.tagColors.container,
            textColor = mealCategory.tagColors.onContainer,
            borderColor = mealCategory.tagColors.onContainer,
            label = stringResource(mealCategory.stringRes).uppercase()
        )

        Icon(
            painter = painterResource(R.drawable.dot),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = cuisine,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
    }
}
