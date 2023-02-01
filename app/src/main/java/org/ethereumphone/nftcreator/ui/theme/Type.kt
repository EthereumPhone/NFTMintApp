package org.ethereumphone.nftcreator.ui.theme


import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.ethereumphone.nftcreator.R

//import com.example.collectiongallery.R

// Set of Material typography styles to start with
/*val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
*/
// ethOS Theme

private val Inter = FontFamily(
    Font(R.font.inter_light,FontWeight.Light),
    Font(R.font.inter_regular,FontWeight.Normal),
    Font(R.font.inter_medium,FontWeight.Medium),
    Font(R.font.inter_semibold,FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold)
)
val ethOSTypography = Typography(
    h1 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 80.sp,
        lineHeight = 84.sp,
        letterSpacing = (-0.05).sp
    ),
    h2 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 56.sp,
        lineHeight = (61.6).sp,
        letterSpacing = (-0.05).sp
    ),
    h3 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = (52.8).sp,
        letterSpacing = (-0.04).sp
    ),
    h4 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.04).sp
    ),
    h5 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.04).sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = (43.2).sp,
        letterSpacing = (-0.05).sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = (31.2).sp,
        letterSpacing = (-0.05).sp
    ),
    body1 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 19.sp,
        lineHeight = (26.6).sp,
        letterSpacing = (-0.04).sp
    ),
    body2 = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = (23.8).sp,
        letterSpacing = (-0.04).sp
    ),
    button = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = (19.6).sp,
    ),

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)