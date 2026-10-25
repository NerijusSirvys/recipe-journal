package lt.recipejournal.android.features.recipes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.core.extensions.dashedBorder

@Composable
fun EmptyStateMessage(
    modifier: Modifier = Modifier,
    text: String
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .dashedBorder(
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.small,
            )
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            style = MaterialTheme.typography.labelSmall,
            text = text
        )
    }
}