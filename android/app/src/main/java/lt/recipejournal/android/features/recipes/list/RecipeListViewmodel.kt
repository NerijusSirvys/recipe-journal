package lt.recipejournal.android.features.recipes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import lt.recipejournal.android.features.recipes.data.RecipeRepository
import lt.recipejournal.android.features.recipes.data.models.CookingTime
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.SortBy
import lt.recipejournal.android.features.recipes.data.models.Source
import java.util.UUID

class RecipeListViewmodel(
    val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _filterState = MutableStateFlow(RecipeListFilterState())
    private val _retryTrigger = MutableStateFlow(0)
    private val _isFilterSheetVisible = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _libraryLoad: Flow<LibraryLoad> =
        combine(_filterState, _retryTrigger) { filters, _ -> filters }
            .flatMapLatest { filters ->
                combine(
                    recipeRepository.getRecipeSummaries(),
                    recipeRepository.getRecipeCount(),
                    recipeRepository.getAvailableCuisineFilters()
                ) { summaries, count, cuisineFilters ->
                    if (summaries.isNotEmpty()) {
                        LibraryLoad.Success(summaries, count, cuisineFilters) as LibraryLoad
                    } else {
                        LibraryLoad.Empty
                    }
                }.catch { emit(LibraryLoad.Failure) }
            }

    val state: StateFlow<RecipeListUIState> = combine(
        _libraryLoad,
        _filterState,
        _isFilterSheetVisible
    ) { load, filterState, isFilterSheetVisible ->

        when (load) {
            is LibraryLoad.Success -> {
                val cuisineFilters = load.cuisineFilters.toMutableList()
                cuisineFilters.add(0, Cuisine.ALL)
                RecipeListUIState(
                    contentState = ContentState.LOADED,
                    recipes = load.summaries.filter { !it.isFavourite },
                    favourites = load.summaries.filter { it.isFavourite },
                    totalRecipeCount = load.totalCount,
                    filterState = filterState,
                    showFilterBottomSheet = isFilterSheetVisible,
                    availableCuisineFilters = cuisineFilters.toSet(),
                    availableSourceFilters = Source.entries.toSet(),
                    availableSortByFilter = SortBy.entries.toSet(),
                    availableCookingTimeFilters = CookingTime.entries.toSet()
                )
            }

            LibraryLoad.Failure -> RecipeListUIState(
                contentState = ContentState.ERROR,
                filterState = filterState
            )

            LibraryLoad.Empty -> RecipeListUIState(
                contentState = ContentState.EMPTY
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RecipeListUIState()
    )


    fun onAction(actions: RecipeListActions) {
        when (actions) {
            is RecipeListActions.SearchUpdated -> updateSearchTerm(actions.searchTerm)
            is RecipeListActions.ToggleMealCategoryFilter -> updateMealCategoryFilters(actions.category)
            is RecipeListActions.ToggleFavourite -> toggleFavourite(actions.recipeId)
            is RecipeListActions.ShowFilterBottomSheet -> showFilterBottomSheet(actions.show)
            is RecipeListActions.ToggleCuisineFilter -> toggleCuisineFilter(actions.cuisine)
            is RecipeListActions.ToggleCookingTimeFilter -> toggleCookingTimeFilter(actions.cookingTime)
            is RecipeListActions.ToggleSourceFilter -> toggleSourceFilter(actions.source)
            is RecipeListActions.ToggleSortByFilter -> toggleSortByFilter(actions.sortBy)
            RecipeListActions.ShowFilteredRecipes -> showFilteredRecipes()
            RecipeListActions.ResetFilters -> resetFilters()
        }
    }

    private fun resetFilters() {
        _filterState.update {
            it.copy(
                selectedCuisines = setOf(Cuisine.ALL),
                selectedCookingTimeFilters = setOf(CookingTime.ALL),
                selectedSourceFilters = setOf(Source.ALL),
                sortBy = SortBy.RECENTLY_ADDED
            )
        }
    }

    private fun showFilteredRecipes() {
        _isFilterSheetVisible.update { false }
    }

    private fun toggleSortByFilter(sortBy: SortBy) {
        _filterState.update {
            it.copy(sortBy = sortBy)
        }
    }

    private fun toggleSourceFilter(source: Source) {
        _filterState.update {
            if (source == Source.ALL) {
                it.copy(selectedSourceFilters = setOf(Source.ALL))
            } else {
                val newFilters = if (it.selectedSourceFilters.contains(source)) {
                    it.selectedSourceFilters - source
                } else {
                    it.selectedSourceFilters + source
                } - Source.ALL

                if (newFilters.isEmpty()) {
                    it.copy(selectedSourceFilters = setOf(Source.ALL))
                } else {
                    it.copy(selectedSourceFilters = newFilters)
                }
            }
        }
    }

    private fun toggleCookingTimeFilter(cookingTime: CookingTime) {
        _filterState.update {
            if (cookingTime == CookingTime.ALL) {
                it.copy(selectedCookingTimeFilters = setOf(CookingTime.ALL))
            } else {
                val newFilters = if (it.selectedCookingTimeFilters.contains(cookingTime)) {
                    it.selectedCookingTimeFilters - cookingTime
                } else {
                    it.selectedCookingTimeFilters + cookingTime
                } - CookingTime.ALL

                if (newFilters.isEmpty()) {
                    it.copy(selectedCookingTimeFilters = setOf(CookingTime.ALL))
                } else {
                    it.copy(selectedCookingTimeFilters = newFilters)
                }
            }
        }
    }

    private fun toggleCuisineFilter(cuisine: Cuisine) {
        _filterState.update {
            if (cuisine == Cuisine.ALL) {
                it.copy(selectedCuisines = setOf(Cuisine.ALL))
            } else {
                val newFilters = if (it.selectedCuisines.contains(cuisine)) {
                    it.selectedCuisines - cuisine
                } else {
                    it.selectedCuisines + cuisine
                } - Cuisine.ALL

                if (newFilters.isEmpty()) {
                    it.copy(selectedCuisines = setOf(Cuisine.ALL))
                } else {
                    it.copy(selectedCuisines = newFilters)
                }
            }
        }
    }

    private fun showFilterBottomSheet(show: Boolean) {
        _isFilterSheetVisible.update { show }
    }

    private fun toggleFavourite(recipeId: UUID) {
        recipeRepository.setFavourite(recipeId)
    }

    private fun updateMealCategoryFilters(category: MealCategory?) {
        _filterState.update {
            if (category == null) {
                it.copy(selectedCategories = emptySet())
            } else {
                if (it.selectedCategories.contains(category)) {
                    it.copy(selectedCategories = it.selectedCategories - category)
                } else {
                    it.copy(selectedCategories = it.selectedCategories + category)
                }
            }
        }
    }

    private fun updateSearchTerm(value: String) {
        _filterState.update {
            it.copy(searchInput = value)
        }
    }
}