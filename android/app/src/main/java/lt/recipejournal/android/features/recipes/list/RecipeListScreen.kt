package lt.recipejournal.android.features.recipes.list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.drop
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecipeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    reselectCount: () -> Int
) {
    val vm = koinViewModel<RecipeListViewmodel>()
    val state by vm.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Tapping the Recipes tab again scrolls the list back to the top
    val currentReselectCount by rememberUpdatedState(reselectCount)
    LaunchedEffect(listState) {
        snapshotFlow { currentReselectCount() }
            .drop(1) // the value it starts with is an old tap, not a new one
            .collect { listState.animateScrollToItem(0) }
    }

    when (state.contentState) {
        ContentState.LOADING -> RecipeListLoadingContent(
            modifier = modifier
        )

        ContentState.ERROR -> TODO("Implement error screen")
        ContentState.LOADED -> RecipeListContent(
            modifier = modifier,
            state = state,
            listState = listState,
            onAction = vm::onAction
        )

        ContentState.EMPTY -> RecipeListEmptyContent(
            modifier = modifier,
            onCreateRecipe = { TODO("Navigate to Create screen") },
            onImportFromWeb = { TODO("Navigate to Import from web screen") }
        )
    }
}
