package org.ethereumphone.nftcreator.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.ethereumphone.nftcreator.R


/*@Composable
@Preview
fun ethOSSnackbarPreview() {
    ethOSTheme {
        val scaff = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        val hostState = remember { SnackbarHostState() }
        val s = rememberSnackbarDelegate(hostState,scope)

        /*Scaffold(
            scaffoldState= scaff,
        ) {*/
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(blue)
                    //.padding(8.dp)
                    //.padding(paddingValues = it)
            ) {

                Column(modifier = Modifier.fillMaxSize()) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            s.coroutineScope.launch {
                                s.showSnackbar(SnackbarState.DEFAULT,"DEFAULT")
                            }
                        }) {
                        Text("Show Default")
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            s.coroutineScope.launch {
                                s.showSnackbar(SnackbarState.SUCCESS,"SUCCESS")
                            }
                        }) {
                        Text("Show Success")
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            s.coroutineScope.launch {
                                s.showSnackbar(SnackbarState.WARNING,"WARNING")
                            }
                        }) {
                        Text("Show Warning")
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            s.coroutineScope.launch {
                                s.showSnackbar(SnackbarState.ERROR,"ERROR")
                            }
                        }) {
                        Text("Show Error")
                    }

                }
                /*SnackbarHost(
                    modifier=Modifier.align(Alignment.BottomStart),
                    hostState = snackState
                ) { snackbarData: SnackbarData ->
                    CustomSnackBar(
                        R.drawable.baseline_check_24,
                        snackbarData.message,
                        isRtl = true,
                        containerColor = white
                    )
                }*/
                ethOSSnackbarHost(delegate = s, modifier = Modifier.padding(12.dp).align(alignment = Alignment.BottomStart))
            }
        //}


    }
}*/

@Composable
fun CustomSnackBar(
    @DrawableRes drawableRes: Int,
    delegate: SnackbarDelegate,
    message: String,
    //isRtl: Boolean = true,
    //containerColor: Color = white
) {
    Snackbar(
        contentColor = delegate.snackbarOnColor,
        shape =  RoundedCornerShape(12.dp),
        backgroundColor = delegate.snackbarBackgroundColor,
        elevation = 8.dp,
    ) {
        /*CompositionLocalProvider(
            LocalLayoutDirection provides
                    if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {*/
        Row (
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = message,
                style = MaterialTheme.typography.body1
            )
            Image(
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(delegate.snackbarOnColor),
                painter = painterResource(id = delegate.snackbarIcon),
                contentDescription = "Snackbar icon"
            )
        }
        //}
    }
}

@Composable
fun ethOSSnackbarHost(
    delegate: SnackbarDelegate,
    modifier: Modifier
) {
    //ethOSTheme() {
        SnackbarHost(
            modifier= modifier,
            hostState = delegate.snackbarHostState
        ) { snackbarData: SnackbarData ->
            CustomSnackBar(
                R.drawable.baseline_check_24,
                delegate,
                snackbarData.message
            )
        }

    //}

}




