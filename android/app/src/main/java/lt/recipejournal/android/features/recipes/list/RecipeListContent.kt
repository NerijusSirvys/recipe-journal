package lt.recipejournal.android.features.recipes.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import lt.recipejournal.android.components.SearchField
import lt.recipejournal.android.core.component_defaults.ScreenContentDefaults
import lt.recipejournal.android.features.recipes.components.AddRecipeSpeedDialFab
import lt.recipejournal.android.features.recipes.components.CategoryFilterChip
import lt.recipejournal.android.features.recipes.components.EmptyStateMessage
import lt.recipejournal.android.features.recipes.components.FilterButton
import lt.recipejournal.android.features.recipes.components.HeroCard
import lt.recipejournal.android.features.recipes.components.HorizontalRecipeCard
import lt.recipejournal.android.features.recipes.components.TopBar
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary
import lt.recipejournal.android.features.recipes.data.models.iconRes
import lt.recipejournal.android.features.recipes.data.models.stringRes
import lt.recipejournal.android.features.recipes.list.components.RecipeFilterSheet
import lt.recipejournal.android.ui.theme.AndroidTheme
import java.time.Instant
import java.util.UUID

@Composable
internal fun RecipeListContent(
    modifier: Modifier = Modifier,
    onAction: (RecipeListActions) -> Unit,
    state: RecipeListUIState,
    listState: LazyListState = rememberLazyListState()
) {

    // Lets the last card scroll clear of the FAB (56dp FAB + 16dp margin + a little air)
    val fabClearance = 88.dp


    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                text = stringResource(R.string.recipes),
                totalCount = state.totalRecipeCount,
                showedCount = state.filteredRecipeCount
            )
        },
        floatingActionButton = {
            AddRecipeSpeedDialFab(
                onWriteYourOwnClick = { onAction(RecipeListActions.CreateRecipe) },
                onImportFromWebClick = { onAction(RecipeListActions.ImportFromWeb) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = ScreenContentDefaults.HorizontalPadding,
                end = ScreenContentDefaults.HorizontalPadding,
                bottom = fabClearance
            ),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item(key = "search", contentType = "search") {
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
            }

            item(key = "categories", contentType = "categories") {
                MealCategoryChipRow(state, onAction)
            }

            item(key = "favourites_title", contentType = "section_title") {
                SectionTitle(text = stringResource(R.string.your_favourites))
            }

            item(key = "favourites", contentType = "favourites") {
                if (state.favourites.isEmpty()) {
                    EmptyStateMessage(
                        text = stringResource(R.string.no_favourites_message)
                    )
                } else {
                    CardCarousel(state, onAction)
                }
            }

            item(key = "recipes_title", contentType = "section_title") {
                SectionTitle(text = stringResource(R.string.your_recipes))
            }

            if (state.recipes.isEmpty()) {
                item(key = "recipes_empty", contentType = "empty_message") {
                    EmptyStateMessage(
                        text = stringResource(R.string.empty_recipe_list_state_message)
                    )
                }
            } else {
                items(
                    items = state.recipes,
                    key = { x -> x.id },
                    contentType = { "recipe" }
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
            RecipeFilterSheet(
                onAction = onAction,
                state = state
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        // Same 25dp gap above section titles as the old Column (5 + 15 spacer + 5)
        modifier = Modifier.padding(top = 20.dp),
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface
    )
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
                label = R.string.meal_category_all,
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
                showFilterBottomSheet = false,
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
                        image = "some",
                        createdOn = Instant.now()
                    )
                }
            )
        )
    }
}


