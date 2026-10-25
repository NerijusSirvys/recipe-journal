package lt.recipejournal.android.features.recipes.list.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R
import lt.recipejournal.android.components.AppModalBottomSheet
import lt.recipejournal.android.features.recipes.components.FilterChipItem
import lt.recipejournal.android.features.recipes.components.SectionLabel
import lt.recipejournal.android.features.recipes.data.models.stringRes
import lt.recipejournal.android.features.recipes.list.RecipeListActions
import lt.recipejournal.android.features.recipes.list.RecipeListUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeFilterSheet(
    onAction: (RecipeListActions) -> Unit,
    state: RecipeListUIState,
) {
    AppModalBottomSheet(
        onDismissRequest = {
            onAction(RecipeListActions.ShowFilterBottomSheet(false))
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filter_sheet_title),
                style = MaterialTheme.typography.titleLarge
            )

            TextButton(
                onClick = { onAction(RecipeListActions.ResetFilters) },
                // Keeps the text close to the right edge; the touch target stays 48dp tall
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                Text(
                    text = stringResource(R.string.clear_all),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // The header is now 48dp tall (button touch target), so less space is needed below it
        Spacer(Modifier.height(5.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChipSection(
                chips = state.availableCuisineFilters,
                selected = {
                    it == null
                            && state.filterState.selectedCuisines.isEmpty()
                            || state.filterState.selectedCuisines.contains(it)
                },
                title = R.string.cuisine,
                labelRes = { it.stringRes },
                onFilterClick = { onAction(RecipeListActions.ToggleCuisineFilter(it)) }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            FilterChipSection(
                chips = state.availableCookingTimeFilters,
                selected = {
                    it == null
                            && state.filterState.selectedCookingTimeFilters.isEmpty()
                            || state.filterState.selectedCookingTimeFilters.contains(it)
                },
                labelRes = { it.stringRes },
                title = R.string.cooking_time,
                onFilterClick = { onAction(RecipeListActions.ToggleCookingTimeFilter(it)) }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            FilterChipSection(
                chips = state.availableSourceFilters,
                labelRes = { it.stringRes },
                selected = {
                    it == null
                            && state.filterState.selectedSourceFilters.isEmpty()
                            || state.filterState.selectedSourceFilters.contains(it)
                },
                title = R.string.source,
                onFilterClick = { onAction(RecipeListActions.ToggleSourceFilter(it)) }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            FilterChipSection(
                selected = {
                    state.filterState.sortBy == it
                },
                showAll = false,
                chips = state.availableSortByFilter,
                title = R.string.sort_by,
                labelRes = { it.stringRes },
                onFilterClick = { sortBy ->
                    sortBy?.let { onAction(RecipeListActions.SelectSortBy(it)) }
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAction(RecipeListActions.ShowFilteredRecipes)
                },
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    pluralStringResource(
                        R.plurals.show_recipes,
                        state.filteredRecipeCount,
                        state.filteredRecipeCount
                    )
                )
            }
        }
    }
}


@Composable
private fun <T> FilterChipSection(
    selected: (T?) -> Boolean,
    @StringRes title: Int,
    labelRes: (T) -> Int,
    chips: Set<T>,
    onFilterClick: (T?) -> Unit,
    showAll: Boolean = true,
) {
    Column {
        SectionLabel(text = stringResource(title))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (showAll) {
                FilterChipItem(
                    selected = selected(null),
                    text = stringResource(R.string.all),
                    onClick = { onFilterClick(null) }
                )
            }

            chips.forEach { item ->
                FilterChipItem(
                    selected = selected(item),
                    text = stringResource(labelRes(item)),
                    onClick = { onFilterClick(item) }
                )
            }
        }
    }
}