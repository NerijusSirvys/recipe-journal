package lt.recipejournal.android.features.recipes.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.iconRes
import lt.recipejournal.android.features.recipes.data.models.stringRes
import lt.recipejournal.android.ui.theme.AndroidTheme


@Composable
fun CategoryFilterChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    @StringRes label: Int,
    @DrawableRes iconId: Int
) {

    ElevatedFilterChip(
        modifier = modifier,
        selected = selected,
        onClick = onClick,
        leadingIcon = {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null
            )
        },
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            selectedBorderColor = MaterialTheme.colorScheme.primary,
            borderColor = MaterialTheme.colorScheme.outline,
            selectedBorderWidth = 1.dp
        ),
        colors = FilterChipDefaults.filterChipColors().copy(
            selectedLabelColor = MaterialTheme.colorScheme.primary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.primary,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.outline,
            leadingIconColor = MaterialTheme.colorScheme.outline,
            containerColor = MaterialTheme.colorScheme.surface
        ),
        label = {
            Text(text = stringResource(label))
        }
    )
}

@PreviewLightDark
@Composable
private fun Selected() {
    AndroidTheme {
        Surface {
            CategoryFilterChip(
                selected = true,
                onClick = {},
                label = MealCategory.BREAKFAST.stringRes,
                iconId = MealCategory.BREAKFAST.iconRes
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Unselected() {
    AndroidTheme {
        Surface {
            CategoryFilterChip(
                selected = false,
                onClick = {},
                label = MealCategory.BREAKFAST.stringRes,
                iconId = MealCategory.BREAKFAST.iconRes
            )
        }
    }
}