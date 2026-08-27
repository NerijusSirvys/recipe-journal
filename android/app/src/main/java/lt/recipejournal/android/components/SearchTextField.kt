package lt.recipejournal.android.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    text: String,
    onSearch: (String) -> Unit = {},
    placeholder: String = stringResource(R.string.recipe_list_search_placeholder),
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
                modifier = Modifier.size(AppTextFieldDefaults.IconSize),
            )
        },
        trailing = if (showClearAction && text.isNotEmpty()) {
            {
                ClearAction(onClick = { onSearch("") })
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
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.x),
            contentDescription = stringResource(R.string.recipe_list_search_clear),
            tint = AppTextFieldDefaults.leadingIconColor,
            modifier = Modifier.size(AppTextFieldDefaults.IconSize),
        )
    }
}
