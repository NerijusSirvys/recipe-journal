package lt.recipejournal.android.features.recipes.list

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeListScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val vm = koinViewModel<RecipeListViewmodel>()
    val state by vm.state.collectAsStateWithLifecycle()

    when (state.contentState) {
        ContentState.LOADING -> RecipeListLoadingContent(
            modifier = modifier
        )

        ContentState.ERROR -> TODO("Implement error screen")
        ContentState.LOADED -> RecipeListContent(
            modifier = modifier,
            state = state,
            onAction = vm::onAction
        )

        ContentState.EMPTY -> RecipeListEmptyContent(
            modifier = modifier,
            onCreateRecipe = { TODO("Navigate to Create screen") },
            onImportFromWeb = { TODO("Navigate to Import from web screen") }
        )
    }
}
