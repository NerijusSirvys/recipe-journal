package lt.recipejournal.android.features.recipes.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R
import lt.recipejournal.android.features.recipes.components.TopBar
import lt.recipejournal.android.ui.theme.AndroidTheme

@Composable
fun RecipeListEmptyContent(
    modifier: Modifier = Modifier,
    onCreateRecipe: () -> Unit,
    onImportFromWeb: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                text = stringResource(R.string.recipes),
                totalCount = 0,
                showedCount = 0
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.no_recipes),
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = stringResource(R.string.no_recipes_supporting_text),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(15.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCreateRecipe,
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    painter = painterResource(R.drawable.notebook),
                    contentDescription = null
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.btn_create_recipe)
                )
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onImportFromWeb,
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    painter = painterResource(R.drawable.download),
                    contentDescription = null
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.btn_import_from_web),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun preview() {
    AndroidTheme {
        RecipeListEmptyContent(
            onCreateRecipe = {},
            onImportFromWeb = {}
        )
    }
}