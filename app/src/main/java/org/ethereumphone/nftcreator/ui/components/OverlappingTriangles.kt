package org.ethereumphone.nftcreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.ethereumphone.nftcreator.ui.theme.NftCreatorTheme

/*

@Composable
fun OverlappingTriangles(modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = modifier) {
        var spacer = this.maxHeight.times(0.08f)*4
        var triangleHight = this.maxHeight.times(0.50f)


        //purple triangle
        Box(modifier = Modifier
            .size(width = this.maxWidth, height = triangleHight)
            .offset(y = spacer)
            .clip(TriangleCurvedTop())
            .background(MaterialTheme.colors.primary)
        )

        Box(Modifier
            .size(width = this.maxWidth, height = spacer)
            .clip(RectangleShape)
            .background(MaterialTheme.colors.primary)
        )

        //white triangle
        spacer /= 2

        Box(modifier = Modifier
            .size(width = this.maxWidth, height = triangleHight)
            .offset(y = spacer)
            .clip(TriangleCurvedTop())
            .background(Color.White)
        )
        Box(Modifier
            .size(width = this.maxWidth, height = spacer)
            .clip(RectangleShape)
            .background(Color.White)
        )

        // background
        Box(modifier = Modifier
            .size(width = this.maxWidth, height = triangleHight)
            .clip(Triangle())
            .background(MaterialTheme.colors.background)
        )
    }
}

class TriangleCurvedTop : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width.times(.55f), size.height.times(.225f))
            quadraticBezierTo(
                size.width.times(.50f), size.height.times(.25f),
                size.width.times(.45f), size.height.times(.225f)
            )
            close()
        }
        return Outline.Generic(path)
    }
}

class Triangle : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width.times(.50f), size.height.times(.25f))
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
@Preview(showBackground = false)
fun previewOverlappingTriangles() {
    NftCreatorTheme {
        OverlappingTriangles(modifier = Modifier.aspectRatio(1f))
    }
}

 */

