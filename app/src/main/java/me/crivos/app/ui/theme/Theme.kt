package me.crivos.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class CrivosSemanticColors(
    val bgPaper: Color,
    val bgCard: Color,
    val bgSubtle: Color,
    val bgElevated: Color,
    val inkPrimary: Color,
    val inkMuted: Color,
    val border: Color,
    val borderSubtle: Color,
    val red: Color = CrivosRed,
    val blue: Color = CrivosBlue,
    val yellow: Color = CrivosYellow,
    val green: Color = CrivosGreen,
    val isDark: Boolean
)

val LocalCrivosColors = staticCompositionLocalOf {
    CrivosSemanticColors(
        bgPaper = AmoledPaper,
        bgCard = AmoledCard,
        bgSubtle = AmoledSubtle,
        bgElevated = AmoledElevated,
        inkPrimary = AmoledInkPrimary,
        inkMuted = AmoledInkMuted,
        border = AmoledBorder,
        borderSubtle = AmoledBorderSubtle,
        isDark = true
    )
}

private val CrivosDarkColorScheme = darkColorScheme(
    primary = CrivosRed,
    onPrimary = Color.White,
    primaryContainer = CrivosRed.copy(alpha = 0.20f),
    onPrimaryContainer = CrivosRed,
    secondary = CrivosBlue,
    onSecondary = Color.White,
    tertiary = CrivosYellow,
    onTertiary = Color(0xFF111111),
    background = AmoledPaper,
    onBackground = AmoledInkPrimary,
    surface = AmoledCard,
    onSurface = AmoledInkPrimary,
    surfaceVariant = AmoledSubtle,
    onSurfaceVariant = AmoledInkMuted,
    outline = AmoledBorder,
    outlineVariant = AmoledBorderSubtle,
    error = CrivosRed
)

private val CrivosLightColorScheme = lightColorScheme(
    primary = CrivosRed,
    onPrimary = Color.White,
    primaryContainer = CrivosRed.copy(alpha = 0.16f),
    onPrimaryContainer = CrivosRed,
    secondary = CrivosBlueLight,
    onSecondary = Color.White,
    tertiary = CrivosYellowLight,
    onTertiary = Color(0xFF111111),
    background = LightPaper,
    onBackground = LightInkPrimary,
    surface = LightCard,
    onSurface = LightInkPrimary,
    surfaceVariant = LightSubtle,
    onSurfaceVariant = LightInkMuted,
    outline = LightBorder,
    outlineVariant = LightBorderSubtle,
    error = CrivosRed
)

val CrivosShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(9.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(14.dp),
    extraLarge = RoundedCornerShape(18.dp)
)

val CrivosTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        letterSpacing = (-0.8).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = (-0.4).sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 11.sp,
        letterSpacing = 0.6.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun CrivosTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CrivosDarkColorScheme else CrivosLightColorScheme
    val crivosColors = if (darkTheme) {
        CrivosSemanticColors(
            bgPaper = AmoledPaper,
            bgCard = AmoledCard,
            bgSubtle = AmoledSubtle,
            bgElevated = AmoledElevated,
            inkPrimary = AmoledInkPrimary,
            inkMuted = AmoledInkMuted,
            border = AmoledBorder,
            borderSubtle = AmoledBorderSubtle,
            isDark = true
        )
    } else {
        CrivosSemanticColors(
            bgPaper = LightPaper,
            bgCard = LightCard,
            bgSubtle = LightSubtle,
            bgElevated = LightElevated,
            inkPrimary = LightInkPrimary,
            inkMuted = LightInkMuted,
            border = LightBorder,
            borderSubtle = LightBorderSubtle,
            isDark = false
        )
    }

    CompositionLocalProvider(LocalCrivosColors provides crivosColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = CrivosShapes,
            typography = CrivosTypography,
            content = content
        )
    }
}
