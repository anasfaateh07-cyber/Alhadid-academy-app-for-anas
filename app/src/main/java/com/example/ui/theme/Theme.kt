package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val V5LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = Color.White,
    primaryContainer = SoftMintBg,
    onPrimaryContainer = PrimaryGreen,
    secondary = PrimaryGold,
    onSecondary = Color.White,
    secondaryContainer = SoftGoldBg,
    onSecondaryContainer = Color(0xFF3D3000),
    tertiary = BrightActionGreen,
    onTertiary = Color.White,
    background = CreamBackground,
    onBackground = TextDarkSlate,
    surface = CardWhite,
    onSurface = TextDarkSlate,
    surfaceVariant = Color(0xFFF5F2E4),
    onSurfaceVariant = TextMutedGrey,
    outline = SoftGoldBorder
)

val AcademyShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AlHadidAcademyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = V5LightColorScheme,
        typography = Typography,
        shapes = AcademyShapes,
        content = content
    )
}
