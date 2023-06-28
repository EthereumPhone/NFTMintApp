package org.ethereumphone.nftcreator.ui.components

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.ethereumphone.nftcreator.R
import org.ethereumphone.nftcreator.ui.theme.darkbrown
import org.ethereumphone.nftcreator.ui.theme.lightblue
import org.ethereumphone.nftcreator.ui.theme.positive
import org.ethereumphone.nftcreator.ui.theme.warning
import org.ethereumphone.nftcreator.ui.theme.white
import org.ethereumphone.nftcreator.ui.theme.error

enum class SnackbarState {
    DEFAULT,
    ERROR,
    SUCCESS,
    WARNING
}

class SnackbarDelegate(
    hostState: SnackbarHostState,
    coroutine: CoroutineScope
) {
    var snackbarHostState by mutableStateOf(hostState)
    var coroutineScope by mutableStateOf(coroutine)

    var snackbarState: SnackbarState = SnackbarState.DEFAULT


    var snackbarBackgroundColor = lightblue

    var snackbarOnColor = white

    var snackbarIcon = R.drawable.outline_info_24



    fun showSnackbar(
        state: SnackbarState,
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ){
        this.snackbarState = state

        this.snackbarBackgroundColor = when (this.snackbarState) {
            SnackbarState.DEFAULT -> lightblue
            SnackbarState.ERROR -> error
            SnackbarState.SUCCESS -> positive
            SnackbarState.WARNING -> warning
        }
        this.snackbarOnColor = if( snackbarState == SnackbarState.WARNING ) darkbrown else white

        this.snackbarIcon = when (this.snackbarState) {
            SnackbarState.DEFAULT -> R.drawable.outline_info_24
            SnackbarState.ERROR -> R.drawable.outline_error_outline_24
            SnackbarState.SUCCESS -> R.drawable.baseline_check_24
            SnackbarState.WARNING -> R.drawable.outline_warning_24
        }

        coroutineScope?.launch {
            snackbarHostState?.showSnackbar(message, actionLabel, duration)
        }
    }

}

@Composable
fun rememberSnackbarDelegate(
    hostState: SnackbarHostState,
    coroutine: CoroutineScope
) = remember(hostState,coroutine) {
    SnackbarDelegate(hostState,coroutine)
}