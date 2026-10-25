package lt.recipejournal.android.features.recipes.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lt.recipejournal.android.R
import lt.recipejournal.android.ui.theme.AndroidTheme

@Composable
fun AddRecipeFab(
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    onWriteYourOwnClick: () -> Unit,
    onImportFromWebClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        // Scrim overlay to close speed dial when clicking outside
        if (expanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { expanded = false }
                    )
            )
        }

        // Speed Dial Items Stacked Above Main FAB
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(150)) + slideInVertically(tween(150)) { it / 2 } + scaleIn(
                tween(
                    150
                )
            ),
            exit = fadeOut(tween(150)) + slideOutVertically(tween(150)) { it / 2 } + scaleOut(
                tween(
                    150
                )
            )
        ) {
            Column(
                modifier = Modifier.padding(bottom = 72.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {
                // Option 1: Import from web
                SpeedDialItem(
                    label = stringResource(R.string.btn_import_from_web),
                    iconRes = R.drawable.download,
                    onClick = {
                        expanded = false
                        onImportFromWebClick()
                    }
                )

                // Option 2: Write your own
                SpeedDialItem(
                    label = stringResource(R.string.btn_write_your_own),
                    iconRes = R.drawable.notebook,
                    onClick = {
                        expanded = false
                        onWriteYourOwnClick()
                    }
                )
            }
        }

        // Main FAB Container
        Surface(
            onClick = { expanded = !expanded },
            shape = if (expanded) CircleShape else MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 6.dp,
            modifier = Modifier.size(if (expanded) 56.dp else Dp.Unspecified)
        ) {
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                },
                label = "FabIconTransition"
            ) { isExpanded ->
                if (isExpanded) {
                    Box(
                        modifier = Modifier.size(56.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.x),
                            contentDescription = stringResource(R.string.close_menu),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plus),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.btn_add_recipe),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpeedDialItem(
    label: String,
    iconRes: Int,
    onClick: () -> Unit
) {
    // The whole row is one button, so TalkBack announces the item once
    // and the gap between the label and the icon is tappable too
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            role = Role.Button,
            onClick = onClick
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shadowElevation = 3.dp
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            shadowElevation = 3.dp,
            modifier = Modifier.size(48.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
@Preview
private fun AddRecipeFabPreview() {
    AndroidTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AddRecipeFab(
                onWriteYourOwnClick = {},
                onImportFromWebClick = {}
            )
        }
    }
}

@Composable
@Preview
private fun AddRecipeFabExpandedPreview() {
    AndroidTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AddRecipeFab(
                initiallyExpanded = true,
                onWriteYourOwnClick = {},
                onImportFromWebClick = {}
            )
        }
    }
}