package lt.recipejournal.android.features.recipes.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R
import lt.recipejournal.android.components.SearchField
import lt.recipejournal.android.core.component_defaults.ScreenContentDefaults
import lt.recipejournal.android.features.recipes.components.CategoryFilterChip
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary
import lt.recipejournal.android.features.recipes.data.models.iconRes
import lt.recipejournal.android.features.recipes.data.models.stringRes
import lt.recipejournal.android.ui.theme.AndroidTheme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListContent(
    modifier: Modifier = Modifier,
    onAction: (RecipeListActions) -> Unit,
    state: RecipeListUIState
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = "Recipes")
                        Text(
                            text = "${state.totalRecipeCount} found - ${state.recipes.size} showed",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(ScreenContentDefaults.ContentPadding)
        ) {
            SearchField(
                text = state.filterState.searchInput,
                showClearAction = true,
                onSearch = { onAction(RecipeListActions.SearchUpdated(it)) }
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                item {
                    CategoryFilterChip(
                        selected = state.filterState.selectedCategories.isEmpty(),
                        onClick = { onAction(RecipeListActions.ToggleMealCategoryFilter(null)) },
                        label = R.string.mealCategory_all,
                        iconId = R.drawable.layout_grid
                    )
                }
                items(
                    items = MealCategory.entries.toTypedArray(),
                    key = { x -> x.name }
                ) { category ->
                    CategoryFilterChip(
                        selected = state.filterState.selectedCategories.contains(category),
                        onClick = { onAction(RecipeListActions.ToggleMealCategoryFilter(category)) },
                        label = category.stringRes,
                        iconId = category.iconRes
                    )
                }
            }

        }


        state.recipes.forEach {
            Text(it.name)
        }
    }
}


@PreviewLightDark
@Composable
private fun RecipeListScreenPreview() {
    AndroidTheme {
        RecipeListContent(
            onAction = {},
            state = RecipeListUIState(
                contentState = ContentState.LOADED,
                totalRecipeCount = 5,
                recipes = List(5) { index ->
                    RecipeSummary(
                        id = UUID.randomUUID(),
                        name = "Recipe ${index + 1}"
                    )
                }
            )
        )
    }
}


