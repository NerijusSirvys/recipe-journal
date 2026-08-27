package lt.recipejournal.android.features.recipes.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import lt.recipejournal.android.features.recipes.data.models.RecipeSummary
import java.util.UUID

class RecipeRepository {
    fun getRecipeSummaries(): Flow<List<RecipeSummary>> {
        val list = List(5) {
            RecipeSummary(
                id = UUID.randomUUID(),
                name = "Recipe $it"
            )
        }

        return flow {
            emit(list)
        }
    }

    fun getRecipeCount(): Flow<Int> {
        return flow { emit(15) }
    }
}