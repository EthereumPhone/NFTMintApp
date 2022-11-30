package org.ethereumphone.nftcreator.ui.components

import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ethereumphone.nftcreator.ui.theme.InputFiledColors
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.ui.theme.md_theme_dark_onSurface


@Composable
fun InputField(
    value: String = "",
    label: String = "",
    readOnly: Boolean = false,
    singeLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = RoundedCornerShape(50.dp),
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    onChange: ((value: String) -> Unit)? = null
) {
    var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 16)))
    }


    TextField(
        value = text,
        onValueChange = {
                            text = it
                            if (onChange != null) {
                                onChange(it.text)
                            }
                        },
        label = {
            Text(text = label, color = md_theme_dark_onSurface)
        },
        readOnly = readOnly,
        colors = InputFiledColors(),
        shape = shape,
        modifier = modifier,
        singleLine = singeLine,
        maxLines = maxLines,
        trailingIcon = trailingIcon
    )
}


@Composable
@Preview
fun PreviewInputFiledEmpty() {
    NftCreatorTheme {
        InputField() {
            println(it)
        }
    }
}

@Composable
@Preview
fun PreviewInputFiled() {
    NftCreatorTheme {
        InputField("Test"){
            println(it)
        }
    }
}

@Composable
@Preview
fun PreviewInputFiledBox() {
    NftCreatorTheme {
        InputField(
            "Test",
            singeLine = false,
            maxLines = 5,
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.7f)
        ){
            println(it)
        }
    }
}