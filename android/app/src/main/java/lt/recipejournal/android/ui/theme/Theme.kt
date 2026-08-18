package lt.recipejournal.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF357D18),
    onPrimary = Color(0xffF6FBF2),
    primaryContainer = Color(0xffD0EFC4),
    onPrimaryContainer = Color(0xff12300A),
    secondary = Color(0xff5C6152),
    onSecondary = Color(0xffF8F9F5),
    secondaryContainer = Color(0xffDEE3D6),
    onSecondaryContainer = Color(0xff1B1F16),
    tertiary = Color(0xff8A4C68),
    onTertiary = Color(0xffFFF7FB),
    tertiaryContainer = Color(0xffF6D9E8),
    onTertiaryContainer = Color(0xff3A1027),
    error = Color(0xffB3261E),
    onError = Color(0xffFFF5F4),
    errorContainer = Color(0xffF9DEDC),
    onErrorContainer = Color(0xff410E0B),
    background = Color(0xffFAF7F0),
    onBackground = Color(0xff1C1B18),
    surface = Color(0xffFAF7F0),
    onSurface = Color(0xff1C1B18),
    surfaceVariant = Color(0xffE6E3D9),
    onSurfaceVariant = Color(0xff4A4941),
    surfaceContainerLowest = Color(0xffFFFDFA),
    surfaceContainerLow = Color(0xffF5F2EB),
    surfaceContainer = Color(0xffF0EDE5),
    surfaceContainerHigh = Color(0xffEAE7DF),
    surfaceContainerHighest = Color(0xffE4E1D8),
    outline = Color(0xff7B7A70),
    outlineVariant = Color(0xffCCCABF),
    scrim = Color(0x9916150F),
    inverseSurface = Color(0xff31302B),
    inverseOnSurface = Color(0xffF3F0E9),
    inversePrimary = Color(0xffA5D98C),
)

@Composable
fun AndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}