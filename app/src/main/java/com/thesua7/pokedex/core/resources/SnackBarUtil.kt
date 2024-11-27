package com.thesua7.pokedex.core.resources

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// Enum for Snack bar types
enum class SnackBarType {
    SUCCESS, ERROR, WARNING, INFO
}

// Snack bar configuration class
data class SnackBarConfig(
    val message: String,
    val actionLabel: String? = null,
    val type: SnackBarType = SnackBarType.INFO,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

// Global Snack bar Manager
@Singleton
class SnackBarManager @Inject constructor() {
    private val scope = CoroutineScope(Dispatchers.Main)
    private val _hostState = SnackbarHostState()
    val hostState: SnackbarHostState = _hostState

    fun show(config: SnackBarConfig) {
        scope.launch {
            _hostState.showSnackbar(
                message = config.message,
                actionLabel = config.actionLabel,
                duration = config.duration
            )
        }
    }
}

// Custom Snack bar Composable
@Composable
fun CustomSnackBarHost(
    snackBarManager: SnackBarManager,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackBarManager.hostState,
        modifier = modifier.padding(16.dp).background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp)),
        snackbar = { snackBarData: SnackbarData ->
            CustomSnackBar(
                snackBarData = snackBarData,
                onActionClick = { snackBarData.performAction() }
            )
        }
    )
}

@Composable
fun CustomSnackBar(
    snackBarData: SnackbarData,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier
            .padding(12.dp)
            .clip(RoundedCornerShape(16.dp)), // Add rounded corners
        shape = RoundedCornerShape(16.dp), // Ensure shape is rounded
        containerColor = Color.Gray, // Optional: customize background
        contentColor = Color.Black, // Optional: customize text color
        action = {
            snackBarData.visuals.actionLabel?.let { label ->
                TextButton(onClick = onActionClick) {
                    Text(label)
                }
            }
        }
    ) {
        Text(text = snackBarData.visuals.message)
    }
}

// Extension function for easy usage
fun SnackBarManager.showSnackBar(
    message: String,
    type: SnackBarType = SnackBarType.INFO,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
) {
    show(
        SnackBarConfig(
            message = message,
            type = type,
            actionLabel = actionLabel,
            duration = duration
        )
    )
}