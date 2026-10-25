package lt.recipejournal.android.core.modules

import lt.recipejournal.android.features.recipes.data.RecipeRepository
import lt.recipejournal.android.features.recipes.list.RecipeListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val recipesModule = module {
    viewModelOf(::RecipeListViewModel)
    singleOf(::RecipeRepository)
}