package lt.recipejournal.android.features.recipes.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R
import lt.recipejournal.android.components.AppModalBottomSheet
import lt.recipejournal.android.components.SearchField
import lt.recipejournal.android.core.component_defaults.ScreenContentDefaults
import lt.recipejournal.android.features.recipes.components.CategoryFilterChip
import lt.recipejournal.android.features.recipes.components.EmptyStateMessage
import lt.recipejournal.android.features.recipes.components.FilterButton
import lt.recipejournal.android.features.recipes.components.FilterChipItem
import lt.recipejournal.android.features.recipes.components.HeroCard
import lt.recipejournal.android.features.recipes.components.HorizontalRecipeCard
import lt.recipejournal.android.features.recipes.components.SectionLabel
import lt.recipejournal.android.features.recipes.components.TopBar
import lt.recipejournal.android.features.recipes.data.models.CookingTime
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary
import lt.recipejournal.android.features.recipes.data.models.SortBy
import lt.recipejournal.android.features.recipes.data.models.Source
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
            TopBar(
                text = stringResource(R.string.recipes),
                totalCount = state.totalRecipeCount,
                showedCount = state.recipes.size
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(ScreenContentDefaults.ContentPadding),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchField(
                    modifier = Modifier.weight(1f),
                    text = state.filterState.searchInput,
                    showClearAction = true,
                    onSearch = { onAction(RecipeListActions.SearchUpdated(it)) }
                )

                FilterButton(
                    onClick = { onAction(RecipeListActions.ShowFilterBottomSheet(true)) }
                )
            }
            MealCategoryChipRow(state, onAction)
            Spacer(modifier.height(15.dp))
            Text(
                text = stringResource(R.string.your_favourites),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (state.favourites.isEmpty()) {
                EmptyStateMessage(
                    text = stringResource(R.string.no_favourites_message)
                )
            } else {
                CardCarousel(state, onAction)
            }

            Spacer(modifier.height(15.dp))
            Text(
                text = stringResource(R.string.your_recipes),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                if (state.recipes.isEmpty()) {
                    item {
                        EmptyStateMessage(
                            text = stringResource(R.string.empty_recipe_list_state_message)
                        )
                    }
                } else {
                    items(
                        items = state.recipes,
                        key = { x -> x.id }
                    ) { recipe ->
                        HorizontalRecipeCard(
                            name = recipe.name,
                            mealCategory = recipe.mealCategory,
                            isFavourite = recipe.isFavourite,
                            cuisine = stringResource(recipe.cuisine.stringRes),
                            servings = recipe.servings,
                            cookingTimeInMinutes = recipe.cookTime.toMinutes().toInt(),
                            ingredientCount = recipe.ingredientCount,
                            image = recipe.image,
                            onFavouriteClicked = { onAction(RecipeListActions.ToggleFavourite(recipe.id)) },
                            onClick = {}
                        )
                    }
                }
            }

            if (state.showFilterBottomSheet) {
                BottomSheet(
                    onAction = onAction,
                    state = state
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onAction: (RecipeListActions) -> Unit,
    state: RecipeListUIState
) {
    AppModalBottomSheet(
        onDismissRequest = {
            onAction(RecipeListActions.ShowFilterBottomSheet(false))
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = stringResource(R.string.filter),
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = stringResource(R.string.clear_all),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        onAction(RecipeListActions.ResetFilters)
                    }
                )
            )
        }

        Spacer(Modifier.height(15.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CuisineFilterSection(
                state = state,
                onFilterClick = { cuisine ->
                    onAction(RecipeListActions.ToggleCuisineFilter(cuisine))
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            CookingTimeFilterSection(
                state = state,
                onFilterClick = { cookingTime ->
                    onAction(RecipeListActions.ToggleCookingTimeFilter(cookingTime))
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            SourceFilterSection(
                state = state,
                onFilterClick = { source ->
                    onAction(RecipeListActions.ToggleSourceFilter(source))
                }
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            SortByFilterSection(
                state = state,
                onFilterClick = { sortBy ->
                    onAction(RecipeListActions.ToggleSortByFilter(sortBy))
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
                Text("Filter")
            }
        }
    }
}

@Composable
private fun SourceFilterSection(
    state: RecipeListUIState,
    onFilterClick: (Source) -> Unit
) {
    Column {
        SectionLabel(text = stringResource(R.string.source))
        FlowRow {
            state.availableSourceFilters.forEach { source ->
                FilterChipItem(
                    selected = state.filterState.selectedSourceFilters.contains(
                        source
                    ),
                    text = stringResource(source.stringRes),
                    onClick = {
                        onFilterClick(source)
                    }
                )
            }
        }
    }
}


@Composable
private fun SortByFilterSection(
    state: RecipeListUIState,
    onFilterClick: (SortBy) -> Unit
) {
    Column {
        SectionLabel(text = stringResource(R.string.sort_by))
        FlowRow {
            state.availableSortByFilter.forEach { sortBy ->
                FilterChipItem(
                    selected = state.filterState.sortBy == sortBy,
                    text = stringResource(sortBy.stringRes),
                    onClick = {
                        onFilterClick(sortBy)
                    }
                )
            }
        }
    }
}

@Composable
private fun CuisineFilterSection(
    state: RecipeListUIState,
    onFilterClick: (Cuisine) -> Unit
) {
    Column {
        SectionLabel(text = stringResource(R.string.cuisine))
        FlowRow {
            state.availableCuisineFilters.forEach { cuisine ->
                FilterChipItem(
                    selected = state.filterState.selectedCuisines.contains(
                        cuisine
                    ),
                    text = stringResource(cuisine.stringRes),
                    onClick = {
                        onFilterClick(cuisine)
                    }
                )
            }
        }
    }
}

@Composable
private fun CookingTimeFilterSection(
    state: RecipeListUIState,
    onFilterClick: (CookingTime) -> Unit
) {
    Column {
        SectionLabel(text = stringResource(R.string.cooking_time))
        FlowRow {
            state.availableCookingTimeFilters.forEach { cookingTime ->
                FilterChipItem(
                    selected = state.filterState.selectedCookingTimeFilters.contains(
                        cookingTime
                    ),
                    text = stringResource(cookingTime.stringRes),
                    onClick = { onFilterClick(cookingTime) }
                )
            }
        }
    }
}


@Composable
private fun MealCategoryChipRow(
    state: RecipeListUIState,
    onAction: (RecipeListActions) -> Unit
) {
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

@Composable
private fun CardCarousel(
    state: RecipeListUIState,
    onAction: (RecipeListActions) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = state.favourites,
            key = { x -> x.id }
        ) { recipe ->
            HeroCard(
                name = recipe.name,
                mealCategory = recipe.mealCategory,
                isFavourite = recipe.isFavourite,
                cuisine = stringResource(recipe.cuisine.stringRes),
                servings = recipe.servings,
                cookingTimeInMinutes = recipe.cookTime.toMinutes().toInt(),
                ingredientCount = recipe.ingredientCount,
                image = recipe.image,
                onFavouriteClicked = { onAction(RecipeListActions.ToggleFavourite(recipe.id)) },
                onClick = {}
            )
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
                showFilterBottomSheet = true,
                totalRecipeCount = 5,
                recipes = List(5) { index ->
                    RecipeSummary(
                        id = UUID.randomUUID(),
                        name = "Recipe ${index + 1}",
                        isFavourite = false,
                        mealCategory = MealCategory.entries.random(),
                        cuisine = Cuisine.entries.random(),
                        servings = 3,
                        cookTime = java.time.Duration.ofMinutes(25),
                        ingredientCount = 15,
                        image = "some"
                    )
                }
            )
        )
    }
}


