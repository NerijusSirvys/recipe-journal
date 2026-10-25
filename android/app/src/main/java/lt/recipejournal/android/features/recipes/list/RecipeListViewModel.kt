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
import lt.recipejournal.android.features.recipes.data.models.RecipeQuery
import lt.recipejournal.android.features.recipes.data.models.SortBy
import lt.recipejournal.android.features.recipes.data.models.Source
import java.util.UUID

class RecipeListViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val filterState = MutableStateFlow(RecipeListFilterState())
    private val retryTrigger = MutableStateFlow(0)
    private val isFilterSheetVisible = MutableStateFlow(false)

    @OptIn(FlowPreview::class)
    private val debouncedSearch = filterState
        .map { it.searchInput }
        .distinctUntilChanged()
        .debounce(300)

    private val recipeQuery =
        combine(filterState, debouncedSearch) { filters, search ->
            filters.toRecipeQuery(
                searchText = search.trim()
            )
        }.distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val libraryLoad: Flow<LibraryLoad> =
        combine(recipeQuery, retryTrigger) { query, _ -> query }
            .flatMapLatest { query ->
                combine(
                    recipeRepository.getRecipeSummaries(query),
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
        libraryLoad,
        filterState,
        isFilterSheetVisible
    ) { load, filters, isSheetVisible ->

        when (load) {
            is LibraryLoad.Success -> {
                val (favourites, recipes) = load.summaries.partition { it.isFavourite }
                RecipeListUIState(
                    contentState = ContentState.LOADED,
                    recipes = recipes,
                    favourites = favourites,
                    totalRecipeCount = load.totalCount,
                    filterState = filters,
                    showFilterBottomSheet = isSheetVisible,
                    availableCuisineFilters = load.cuisineFilters.toSet(),
                    availableSourceFilters = Source.entries.toSet(),
                    availableSortByFilter = SortBy.entries.toSet(),
                    availableCookingTimeFilters = CookingTime.entries.toSet()
                )
            }

            LibraryLoad.Failure -> RecipeListUIState(
                contentState = ContentState.ERROR,
                filterState = filters
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


    fun onAction(action: RecipeListActions) {
        when (action) {
            is RecipeListActions.SearchUpdated -> updateSearchTerm(action.searchTerm)
            is RecipeListActions.ToggleMealCategoryFilter -> toggleMealCategoryFilter(action.category)
            is RecipeListActions.SetFavourite -> setFavourite(action.recipeId, action.isFavourite)
            is RecipeListActions.ShowFilterBottomSheet -> showFilterBottomSheet(action.show)
            is RecipeListActions.ToggleCuisineFilter -> toggleCuisineFilter(action.cuisine)
            is RecipeListActions.ToggleCookingTimeFilter -> toggleCookingTimeFilter(action.cookingTime)
            is RecipeListActions.ToggleSourceFilter -> toggleSourceFilter(action.source)
            is RecipeListActions.SelectSortBy -> selectSortBy(action.sortBy)
            RecipeListActions.ShowFilteredRecipes -> showFilteredRecipes()
            RecipeListActions.ResetFilters -> resetFilters()
            RecipeListActions.CreateRecipe -> {}
            RecipeListActions.ImportFromWeb -> {}
        }
    }

    private fun resetFilters() {
        filterState.update {
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
        isFilterSheetVisible.update { false }
    }

    private fun selectSortBy(sortBy: SortBy) {
        filterState.update {
            it.copy(sortBy = sortBy)
        }
    }

    private fun toggleCuisineFilter(cuisine: Cuisine?) {
        filterState.update { it.copy(selectedCuisines = it.selectedCuisines.toggle(cuisine)) }
    }

    private fun toggleCookingTimeFilter(cookingTime: CookingTime?) {
        filterState.update {
            it.copy(
                selectedCookingTimeFilters = it.selectedCookingTimeFilters.toggle(
                    cookingTime
                )
            )
        }
    }

    private fun toggleSourceFilter(source: Source?) {
        filterState.update {
            it.copy(
                selectedSourceFilters = it.selectedSourceFilters.toggle(source)
            )
        }
    }

    private fun toggleMealCategoryFilter(category: MealCategory?) {
        filterState.update {
            it.copy(
                selectedCategories = it.selectedCategories.toggle(category)
            )
        }
    }

    private fun showFilterBottomSheet(show: Boolean) {
        isFilterSheetVisible.update { show }
    }

    private fun setFavourite(recipeId: UUID, isFavourite: Boolean) {
        recipeRepository.setFavourite(recipeId, isFavourite)
    }

    private fun updateSearchTerm(value: String) {
        filterState.update {
            it.copy(searchInput = value)
        }
    }

    // Search text comes in separately because the query uses the debounced value
    private fun RecipeListFilterState.toRecipeQuery(searchText: String) = RecipeQuery(
        searchText = searchText,
        categories = selectedCategories,
        cuisines = selectedCuisines,
        cookingTimes = selectedCookingTimeFilters,
        sources = selectedSourceFilters,
        sortBy = sortBy
    )

    private fun <T> Set<T>.toggle(item: T?): Set<T> = when {
        item == null -> emptySet()
        contains(item) -> this - item
        else -> this + item
    }
}