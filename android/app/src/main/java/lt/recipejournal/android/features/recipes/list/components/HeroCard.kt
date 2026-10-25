package lt.recipejournal.android.features.recipes.list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import lt.recipejournal.android.R
import lt.recipejournal.android.features.recipes.components.RecipeMetaRow
import lt.recipejournal.android.features.recipes.components.RecipeTagRow
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.stringRes
import lt.recipejournal.android.ui.theme.AndroidTheme

@Composable
fun HeroCard(
    modifier: Modifier = Modifier,
    name: String,
    mealCategory: MealCategory,
    isFavourite: Boolean,
    onFavouriteChange: (Boolean) -> Unit,
    onClick: () -> Unit,
    cuisine: String,
    servings: Int,
    cookingTimeInMinutes: Int,
    ingredientCount: Int,
    image: String
) {
    Card(
        modifier = modifier.width(250.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        onClick = onClick,
        border = BorderStroke(
            width = Dp.Hairline,
            brush = SolidColor(MaterialTheme.colorScheme.outlineVariant)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        )
    ) {
        Column {
            Box {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 7f)
                )

                HeroFavouriteButton(
                    isFavourite = isFavourite,
                    onCheckedChange = onFavouriteChange,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

            Column(
                modifier = Modifier.padding(
                    start = 13.dp,
                    top = 10.dp,
                    end = 13.dp,
                    bottom = 13.dp
                )
            ) {
                Text(
                    text = name,
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
    }
}

@Composable
private fun HeroFavouriteButton(
    isFavourite: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val icon = if (isFavourite) R.drawable.heart_filled else R.drawable.heart

    IconToggleButton(
        checked = isFavourite,
        onCheckedChange = onCheckedChange,
        modifier = modifier
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shadowElevation = 2.dp,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = stringResource(R.string.favourite),
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Preview
@Composable
private fun Preview() {
    AndroidTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(25.dp),
            ) {
                HeroCard(
                    name = "Recipe 1 ",
                    isFavourite = true,
                    mealCategory = MealCategory.LUNCH,
                    cuisine = stringResource(Cuisine.ITALIAN.stringRes),
                    servings = 3,
                    cookingTimeInMinutes = 25,
                    ingredientCount = 15,
                    image = "some uri",
                    onFavouriteChange = {},
                    onClick = {}
                )

                HeroCard(
                    name = "Supper very long recipe name about nothing and some more",
                    isFavourite = false,
                    mealCategory = MealCategory.DINNER,
                    cuisine = stringResource(Cuisine.GREEK.stringRes),
                    servings = 3,
                    cookingTimeInMinutes = 25,
                    ingredientCount = 15,
                    image = "some uri",
                    onFavouriteChange = {},
                    onClick = {}
                )
            }
        }
    }
}
