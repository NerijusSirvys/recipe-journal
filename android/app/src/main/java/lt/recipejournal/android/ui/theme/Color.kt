package lt.recipejournal.android.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Light color scheme, matching the Figma "App (Light)" variables
val LightPrimary = Color(0xFF357D18)
val LightOnPrimary = Color(0xffF6FBF2)
val LightPrimaryContainer = Color(0xffD0EFC4)
val LightOnPrimaryContainer = Color(0xff12300A)
val LightSecondary = Color(0xff5C6152)
val LightOnSecondary = Color(0xffF8F9F5)
val LightSecondaryContainer = Color(0xffDEE3D6)
val LightOnSecondaryContainer = Color(0xff1B1F16)
val LightTertiary = Color(0xff8A4C68)
val LightOnTertiary = Color(0xffFFF7FB)
val LightTertiaryContainer = Color(0xffF6D9E8)
val LightOnTertiaryContainer = Color(0xff3A1027)
val LightError = Color(0xffB3261E)
val LightOnError = Color(0xffFFF5F4)
val LightErrorContainer = Color(0xffF9DEDC)
val LightOnErrorContainer = Color(0xff410E0B)
val LightBackground = Color(0xffFAF7F0)
val LightOnBackground = Color(0xff1C1B18)
val LightSurface = Color(0xffFAF7F0)
val LightOnSurface = Color(0xff1C1B18)
val LightSurfaceVariant = Color(0xffE6E3D9)
val LightOnSurfaceVariant = Color(0xff4A4941)
val LightSurfaceContainerLowest = Color(0xffFFFDFA)
val LightSurfaceContainerLow = Color(0xffF5F2EB)
val LightSurfaceContainer = Color(0xffF0EDE5)
val LightSurfaceContainerHigh = Color(0xffEAE7DF)
val LightSurfaceContainerHighest = Color(0xffE4E1D8)
val LightOutline = Color(0xff7B7A70)
val LightOutlineVariant = Color(0xffCCCABF)
val LightScrim = Color(0x5216150F)
val LightInverseSurface = Color(0xff31302B)
val LightInverseOnSurface = Color(0xffF3F0E9)
val LightInversePrimary = Color(0xffA5D98C)

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