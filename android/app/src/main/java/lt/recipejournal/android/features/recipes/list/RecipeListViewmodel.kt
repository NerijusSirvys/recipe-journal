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
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import java.util.UUID

class RecipeListViewmodel(
    val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _filterState = MutableStateFlow(RecipeListFilterState())
    private val _retryTrigger = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _libraryLoad: Flow<LibraryLoad> =
        combine(_filterState, _retryTrigger) { filters, _ -> filters }
            .flatMapLatest { filters ->
                combine(
                    recipeRepository.getRecipeSummaries(),
                    recipeRepository.getRecipeCount(),
                ) { summaries, count ->
                    if (summaries.isNotEmpty()) {
                        LibraryLoad.Success(summaries, count) as LibraryLoad
                    } else {
                        LibraryLoad.Empty
                    }
                }.catch { emit(LibraryLoad.Failure) }
            }

    val state: StateFlow<RecipeListUIState> = combine(
        _libraryLoad,
        _filterState
    ) { load, filterState ->
        when (load) {
            is LibraryLoad.Success -> RecipeListUIState(
                contentState = ContentState.LOADED,
                recipes = load.summaries.filter { !it.isFavourite },
                favourites = load.summaries.filter { it.isFavourite },
                totalRecipeCount = load.totalCount,
                filterState = filterState
            )

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
        }
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