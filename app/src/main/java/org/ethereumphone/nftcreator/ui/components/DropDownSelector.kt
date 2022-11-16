package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme
import org.ethereumphone.nftcreator.ui.theme.md_theme_dark_surface

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownSelector(
    label: String,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0])}

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
    ) {
        InputField(
            readOnly = true,
            label = label,
            value = selectedOptionText,
            trailingIcon = {
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        })

        NftCreatorTheme(
            shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(30.dp))
        ) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(md_theme_dark_surface.copy(alpha = 0.5f))
            ) {
                options.forEach { selectionOption ->
                    dropDownItem(
                        onClick = {
                            selectedOptionText = selectionOption
                            expanded = false
                        },
                        text = selectionOption
                    )
                }
            }
        }
    }
}

@Composable
fun dropDownItem(
    onClick: (() -> Unit),
    text: String
) {
    DropdownMenuItem(
        onClick = onClick,
    ) {
        Text(text)
    }
}


@Preview
@Composable
fun previewDropDownSelector() {
    NftCreatorTheme {
        val options = listOf("L1", "IMX")
        DropDownSelector("Network", options)
    }
}

@Preview
@Composable
fun previewDropDownItem(

) {
    NftCreatorTheme {
        dropDownItem({},"test")
    }
}