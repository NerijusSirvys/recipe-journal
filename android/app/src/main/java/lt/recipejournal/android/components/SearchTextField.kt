package lt.recipejournal.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R
import lt.recipejournal.android.ui.theme.AndroidTheme

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String,
    onSearch: (String) -> Unit = {},
    placeholder: String = stringResource(R.string.search_placeholder),
    enabled: Boolean = true,
    showClearAction: Boolean = true,
) {

    AppTextField(
        value = text,
        onValueChange = onSearch,
        placeholder = placeholder,
        modifier = modifier,
        enabled = enabled,
        leading = {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = null,
                tint = AppTextFieldDefaults.leadingIconColor,
                modifier = Modifier.size(18.dp),
            )
        },
        trailing = if (showClearAction && text.isNotEmpty()) {
            {
                ClearAction(
                    onClick = { onSearch("") },
                    size = 18.dp
                )
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrectEnabled = false,
            imeAction = ImeAction.Search,
        ),
    )
}

@Composable
private fun ClearAction(
    onClick: () -> Unit,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.x),
            contentDescription = stringResource(R.string.search_clear),
            tint = AppTextFieldDefaults.leadingIconColor,
            modifier = Modifier.size(size),
        )
    }
}

@PreviewLightDark
@Composable
private fun Empty() {
    AndroidTheme {
        Surface {
            SearchField(
                modifier = Modifier.padding(10.dp),
                text = "",
                onSearch = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun WithText() {
    AndroidTheme {
        Surface {
            SearchField(
                modifier = Modifier.padding(10.dp),
                text = "Jimmy",
                onSearch = {},
            )
        }
    }
}
