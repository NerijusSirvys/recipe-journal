package lt.recipejournal.android.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.ui.theme.AndroidTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource? = null,
    shape: CornerBasedShape = AppTextFieldDefaults.shape,
    textStyle: TextStyle = AppTextFieldDefaults.textStyle,
    containerColor: Color = AppTextFieldDefaults.containerColor,
    contentColor: Color = AppTextFieldDefaults.contentColor,
    placeholderColor: Color = AppTextFieldDefaults.placeholderColor,
    borderColor: Color = AppTextFieldDefaults.borderColor,
    cursorColor: Color = AppTextFieldDefaults.cursorColor,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .background(color = containerColor, shape = shape)
            .border(
                width = AppTextFieldDefaults.BorderWidth,
                color = borderColor,
                shape = shape
            ),
        enabled = enabled,
        readOnly = false,
        textStyle = textStyle.copy(color = contentColor),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(cursorColor),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
//                    .defaultMinSize(minHeight = 40.dp)
                    .padding(AppTextFieldDefaults.HorizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(AppTextFieldDefaults.ContentGap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                leading?.invoke()
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle,
                            color = placeholderColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    innerTextField()
                }
                trailing?.invoke()
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun Empty() {
    AndroidTheme {
        Surface {
            AppTextField(
                value = "",
                onValueChange = {},
                placeholder = "Search",
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun WithText() {
    AndroidTheme {
        Surface {
            AppTextField(
                value = "Jimmy",
                onValueChange = {},
                placeholder = "Search",
            )
        }
    }
}

object AppTextFieldDefaults {

    val MinHeight: Dp = 48.dp
    val BorderWidth: Dp = 1.dp
    val Elevation: Dp = 2.dp
    val HorizontalPadding: Dp = 10.dp
    val ContentGap: Dp = 10.dp
    val IconSize: Dp = 24.dp


    val shape: CornerBasedShape
        @Composable @ReadOnlyComposable get() = MaterialTheme.shapes.small

    val textStyle: TextStyle
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography.bodyMedium

    val containerColor: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.surfaceContainerLowest

    val borderColor: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.outlineVariant

    val contentColor: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurface

    val placeholderColor: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurfaceVariant

    val leadingIconColor: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.onSurfaceVariant

    val cursorColor: Color
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme.primary
}