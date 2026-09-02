package lt.recipejournal.android.features.recipes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import lt.recipejournal.android.R
import lt.recipejournal.android.features.recipes.data.models.Cuisine
import lt.recipejournal.android.features.recipes.data.models.MealCategory
import lt.recipejournal.android.features.recipes.data.models.tagColors
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
        Box(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
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
                        .clip(RoundedCornerShape(8.dp))
                )

                Column(
                    modifier = Modifier.weight(.8f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        val icon = if (isFavourite) R.drawable.heart_filled
                        else R.drawable.heart
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable(onClick = onFavouriteClicked),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetaLabel(
                            text = "$cookingTimeInMinutes min",
                            icon = R.drawable.clock_4
                        )

                        MetaLabel(
                            text = "$servings servings",
                            icon = R.drawable.users
                        )

                        MetaLabel(
                            text = "$ingredientCount items",
                            icon = R.drawable.list_todo
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MealCategoryChip(
                            containerColor = mealCategory.tagColors.container,
                            textColor = mealCategory.tagColors.onContainer,
                            borderColor = mealCategory.tagColors.onContainer,
                            label = mealCategory.name
                        )

                        Icon(
                            painter = painterResource(R.drawable.dot),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = cuisine,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
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
                    cuisine = Cuisine.entries.random().name,
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
                    cuisine = Cuisine.entries.random().name,
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