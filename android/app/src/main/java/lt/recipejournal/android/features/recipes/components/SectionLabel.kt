package lt.recipejournal.android.features.recipes.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SectionLabel(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall.copy(
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.6.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}