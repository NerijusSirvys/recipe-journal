package lt.recipejournal.android.features.recipes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import lt.recipejournal.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    text: String,
    totalCount: Int,
    showedCount: Int
) {

    TopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(text = text)
                if(totalCount > 0){
                    Text(
                        text = stringResource(R.string.recipes_found_and_showed, totalCount, showedCount),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
    )
}