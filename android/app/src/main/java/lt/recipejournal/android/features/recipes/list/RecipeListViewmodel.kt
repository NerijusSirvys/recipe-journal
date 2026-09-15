package lt.recipejournal.android.features.recipes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _filterState = MutableStateFlow(RecipeListFilterState())
    private val _retryTrigger = MutableStateFlow(0)
    private val _isFilterSheetVisible = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    private val _debounceSearch = _filterState
        .map { it.searchInput }
        .distinctUntilChanged()
        .debounce(300)

    private val _activeFilters =
        combine(_filterState, _debounceSearch) { filterState, debounceSearch ->
            filterState.copy(
                searchInput = debounceSearch.trim().lowercase()
            )
        }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _libraryLoad: Flow<LibraryLoad> =
        combine(_activeFilters, _retryTrigger) { filters, _ -> filters }
            .flatMapLatest { filters ->
                combine(
                    recipeRepository.getRecipeSummaries(filters),
                    recipeRepository.getRecipeCount(),
                    recipeRepository.getAvailableCuisineFilters()
                ) { summaries, count, cuisineFilters ->
                    if (summaries.isNotEmpty() || count > 0) {
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
                val (favourites, recipes) = load.summaries.partition { it.isFavourite }
                RecipeListUIState(
                    contentState = ContentState.LOADED,
                    recipes = recipes,
                    favourites = favourites,
                    totalRecipeCount = load.totalCount,
                    filterState = filterState,
                    showFilterBottomSheet = isFilterSheetVisible,
                    availableCuisineFilters = load.cuisineFilters.toSet(),
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
                selectedCuisines = emptySet(),
                selectedCookingTimeFilters = emptySet(),
                selectedSourceFilters = emptySet(),
                selectedCategories = emptySet(),
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

    private fun toggleCuisineFilter(cuisine: Cuisine?) {
        _filterState.update { it.copy(selectedCuisines = it.selectedCuisines.toggle(cuisine)) }
    }

    private fun toggleCookingTimeFilter(cookingTime: CookingTime?) {
        _filterState.update {
            it.copy(
                selectedCookingTimeFilters = it.selectedCookingTimeFilters.toggle(
                    cookingTime
                )
            )
        }
    }

    private fun toggleSourceFilter(source: Source?) {
        _filterState.update { it.copy(selectedSourceFilters = it.selectedSourceFilters.toggle(source)) }
    }

    private fun updateMealCategoryFilters(category: MealCategory?) {
        _filterState.update { it.copy(selectedCategories = it.selectedCategories.toggle(category)) }
    }

    private fun showFilterBottomSheet(show: Boolean) {
        _isFilterSheetVisible.update { show }
    }

    private fun toggleFavourite(recipeId: UUID) {
        recipeRepository.setFavourite(recipeId)
    }

    private fun updateSearchTerm(value: String) {
        _filterState.update {
            it.copy(searchInput = value)
        }
    }

    private fun <T> Set<T>.toggle(item: T?): Set<T> = when {
        item == null -> emptySet()
        contains(item) -> this - item
        else -> this + item
    }
}