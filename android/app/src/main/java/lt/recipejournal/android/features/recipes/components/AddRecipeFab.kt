package lt.recipejournal.android.features.recipes.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import lt.recipejournal.android.R
import lt.recipejournal.android.ui.theme.AndroidTheme
import kotlin.math.roundToInt

@Composable
fun AddRecipeFab(
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false,
    onWriteYourOwnClick: () -> Unit,
    onImportFromWebClick: () -> Unit
) {
    val menuState = remember { MutableTransitionState(initiallyExpanded) }
    var fabBounds by remember { mutableStateOf<Rect?>(null) }
    val closeMenu = { menuState.targetState = false }

    Box(modifier = modifier) {
        AddRecipeFabButton(
            onClick = { menuState.targetState = true },
            modifier = Modifier
                .onGloballyPositioned { fabBounds = Rect(it.positionOnScreen(), it.size.toSize()) }
                .alpha(if (menuState.targetState) 0f else 1f)
        )

        val bounds = fabBounds
        if (bounds != null && (menuState.currentState || menuState.targetState)) {
            SpeedDialMenu(
                menuState = menuState,
                fabBounds = bounds,
                onDismiss = closeMenu,
                onImportFromWebClick = {
                    closeMenu()
                    onImportFromWebClick()
                },
                onWriteYourOwnClick = {
                    closeMenu()
                    onWriteYourOwnClick()
                }
            )
        }
    }
}

@Composable
private fun AddRecipeFabButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
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

@Composable
private fun SpeedDialMenu(
    menuState: MutableTransitionState<Boolean>,
    fabBounds: Rect,
    onDismiss: () -> Unit,
    onImportFromWebClick: () -> Unit,
    onWriteYourOwnClick: () -> Unit
) {
    var windowOrigin by remember { mutableStateOf<Offset?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        // A dialog window dims the screen on its own; turn that off so only our scrim dims
        val window = (LocalView.current.parent as? DialogWindowProvider)?.window
        SideEffect { window?.setDimAmount(0f) }

        AnimatedVisibility(
            visibleState = menuState,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(150))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { windowOrigin = it.positionOnScreen() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim)
                        .clearAndSetSemantics { }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismiss
                        )
                )

                Column(
                    modifier = Modifier.alignBottomEndTo(fabBounds, windowOrigin),
                    horizontalAlignment = Alignment.End
                ) {
                    Column(
                        modifier = Modifier.animateEnterExit(
                            enter = slideInVertically(tween(150)) { it / 2 } + scaleIn(tween(150)),
                            exit = slideOutVertically(tween(150)) { it / 2 } + scaleOut(tween(150))
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        SpeedDialItem(
                            label = stringResource(R.string.btn_import_from_web),
                            iconRes = R.drawable.download,
                            onClick = onImportFromWebClick
                        )

                        SpeedDialItem(
                            label = stringResource(R.string.btn_write_your_own),
                            iconRes = R.drawable.notebook,
                            onClick = onWriteYourOwnClick
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    CloseMenuButton(onClick = onDismiss)
                }
            }
        }
    }
}

@Composable
private fun CloseMenuButton(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        shadowElevation = 6.dp,
        modifier = Modifier.size(56.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(R.drawable.x),
                contentDescription = stringResource(R.string.close_menu),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Places the content so its bottom-end corner sits on the FAB's bottom-end corner.
// Both positions are in screen coordinates (the dialog is a separate window, so window
// coordinates would not match), and subtracting the dialog window's origin converts
// the FAB's position into the dialog's own coordinates.
private fun Modifier.alignBottomEndTo(fabBounds: Rect, windowOrigin: Offset?) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        layout(constraints.maxWidth, constraints.maxHeight) {
            // Not placed (so not drawn) until the dialog window's position is known
            val origin = windowOrigin ?: return@layout
            val endEdge = if (layoutDirection == LayoutDirection.Ltr) {
                fabBounds.right - origin.x - placeable.width
            } else {
                fabBounds.left - origin.x
            }
            placeable.place(
                x = endEdge.roundToInt(),
                y = (fabBounds.bottom - origin.y).roundToInt() - placeable.height
            )
        }
    }

@Composable
private fun SpeedDialItem(
    label: String,
    iconRes: Int,
    onClick: () -> Unit
) {
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
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AddRecipeFab(
                initiallyExpanded = true,
                onWriteYourOwnClick = {},
                onImportFromWebClick = {}
            )
        }
    }
}