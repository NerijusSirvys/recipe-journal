package lt.recipejournal.android.features.recipes.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecipeListScreen(
    modifier: Modifier = Modifier,
) {
    val vm = koinViewModel<RecipeListViewmodel>()
    val state by vm.state.collectAsStateWithLifecycle()

    RecipeListContent(
        modifier = modifier,
        state = state,
        onAction = vm::onAction
    )
}