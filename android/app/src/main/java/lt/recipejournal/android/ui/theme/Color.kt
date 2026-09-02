package lt.recipejournal.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MealTagColorRole(
    val container: Color,
    val onContainer: Color,
)

@Immutable
data class MealTagColors(
    val breakfast: MealTagColorRole,
    val lunch: MealTagColorRole,
    val dinner: MealTagColorRole,
    val dessert: MealTagColorRole,
    val snack: MealTagColorRole,
    val side: MealTagColorRole,
)

private val BreakfastBase = Color(0xFF6C5CD4)
private val LunchBase = Color(0xFFC9761A)
private val DinnerBase = Color(0xFF4B9E2C)
private val DessertBase = Color(0xFFD24C8E)
private val SnackBase = Color(0xFF047369)
private val SideBase = Color(0xFF580081)

private const val ContainerAlpha = 0.2f

private fun role(base: Color) = MealTagColorRole(
    container = base.copy(alpha = ContainerAlpha),
    onContainer = base,
)

val LightMealTagColors = MealTagColors(
    breakfast = role(BreakfastBase),
    lunch = role(LunchBase),
    dinner = role(DinnerBase),
    dessert = role(DessertBase),
    snack = role(SnackBase),
    side = role(SideBase),
)

val LocalMealTagColors = staticCompositionLocalOf { LightMealTagColors }

val MaterialTheme.mealTagColors: MealTagColors
    @Composable
    @ReadOnlyComposable
    get() = LocalMealTagColors.current