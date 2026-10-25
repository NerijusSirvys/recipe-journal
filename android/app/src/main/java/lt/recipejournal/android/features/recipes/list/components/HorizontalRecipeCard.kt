package lt.recipejournal.android.features.recipes.list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import lt.recipejournal.android.features.recipes.components.FavouriteToggle
import lt.recipejournal.android.features.recipes.components.RecipeMetaRow
import lt.recipejournal.android.features.recipes.components.RecipeTagRow
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.stringRes
import lt.recipejournal.android.ui.theme.AndroidTheme

@Composable
fun HorizontalRecipeCard(
    modifier: Modifier = Modifier,
    name: String,
    mealCategory: MealCategory,
    isFavourite: Boolean,
    onFavouriteClicked: () -> Unit,
    onClick: () -> Unit,
    cuisine: String,
    servings: Int,
    cookingTimeInMinutes: Int,
    ingredientCount: Int,
    image: String
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = BorderStroke(
            width = Dp.Hairline,
            brush = SolidColor(MaterialTheme.colorScheme.outlineVariant)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        )
    ) {
        Box {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .weight(.2f)
                        .clip(MaterialTheme.shapes.small)
                )

                Column(
                    modifier = Modifier.weight(.75f)
                ) {
                    Text(
                        text = name,
                        modifier = Modifier.padding(end = 32.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    RecipeMetaRow(
                        cookingTimeInMinutes = cookingTimeInMinutes,
                        servings = servings,
                        ingredientCount = ingredientCount
                    )

                    Spacer(Modifier.height(10.dp))

                    RecipeTagRow(
                        mealCategory = mealCategory,
                        cuisine = cuisine
                    )
                }
            }

            FavouriteToggle(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onFavouriteClicked,
                isToggledOn = isFavourite
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AndroidTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(25.dp),
            ) {
                HorizontalRecipeCard(
                    name = "Recipe 1 ",
                    isFavourite = true,
                    mealCategory = MealCategory.entries.random(),
                    cuisine = stringResource(Cuisine.entries.random().stringRes),
                    servings = 3,
                    cookingTimeInMinutes = 25,
                    ingredientCount = 15,
                    image = "some uri",
                    onFavouriteClicked = {},
                    onClick = {}
                )

                HorizontalRecipeCard(
                    name = "Supper very long recipe name about nothing and some more",
                    isFavourite = false,
                    mealCategory = MealCategory.entries.random(),
                    cuisine = stringResource(Cuisine.entries.random().stringRes),
                    servings = 3,
                    cookingTimeInMinutes = 25,
                    ingredientCount = 15,
                    image = "some uri",
                    onFavouriteClicked = {},
                    onClick = {}
                )
            }
        }
    }
}