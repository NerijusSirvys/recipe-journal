package lt.recipejournal.android.features.recipes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R

@Composable
fun FilterChipItem(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    FilterChip(
        modifier = Modifier.padding(end = 4.dp),
        onClick = onClick,
        colors = FilterChipDefaults.filterChipColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        leadingIcon = {
            if (selected) {
                Icon(
                    painter = painterResource(R.drawable.check),
                    contentDescription = null
                )
            }
        },
        border = BorderStroke(1.dp, Color(0xFF3B3A3C)),
        label = {
            Text(text = text)
        },
        selected = selected
    )
}