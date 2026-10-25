package lt.recipejournal.android.features.recipes.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R

@Composable
fun FavouriteToggle(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isToggledOn: Boolean,
) {
    IconToggleButton(
        onCheckedChange = { onClick() },
        checked = isToggledOn,
        modifier = modifier,
    ) {
        val icon = if (isToggledOn) R.drawable.heart_filled
        else R.drawable.heart

        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(R.string.favourite),
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}