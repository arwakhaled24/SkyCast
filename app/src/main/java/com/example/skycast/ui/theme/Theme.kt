package com.example.skycast.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Color definitions based on provided palette
val DeepPurple = Color(0xFF666666)  // Main brand color - deep purple
val BrightPurple = Color(0xFF666666) // Secondary bright purple
val LavenderPurple = Color(0xFF666666) // Lighter purple shade
val PaleLavender = Color(0xFF666666) // Lightest purple shade

// Additional complementary colors
val PureWhite = Color(0xFF666666)
val OffWhite = Color(0xFF333333)
val DarkGray = Color(0xFF333333)
val MediumGray = Color(0xFF666666)
/*
var PrimaryContainer = Color(0xff2A3040)
*/
var PrimaryContainer = Color(0xff0473c8)

private val LightColorScheme = lightColorScheme(
    primary = DeepPurple,        // Primary brand color
    secondary = BrightPurple,    // Secondary brand color
    tertiary = LavenderPurple,   // Accent color
    background = OffWhite,       // Background color
    surface = PureWhite,         // Surface/card color
    onPrimary = PureWhite,       // Text/icons on primary
    onSecondary = PureWhite,     // Text/icons on secondary
    onTertiary = PureWhite,      // Text/icons on tertiary
    onBackground = DarkGray,     // Text/icons on background
    onSurface = DarkGray,        // Text/icons on surface
    surfaceVariant = PaleLavender, // Alternative surface color
    onSurfaceVariant = DeepPurple, // Text on alternative surfac
    onPrimaryContainer = PrimaryContainer


)

private val DarkColorScheme = darkColorScheme(
    primary = BrightPurple,      // Primary brand color in dark mode
    secondary = LavenderPurple,  // Secondary brand color in dark mode
    tertiary = PaleLavender,     // Accent color in dark mode
    background = Color(0xFF121212), // Dark background
    surface = Color(0xFF1E1E1E),   // Dark surface
    onPrimary = PureWhite,         // Text/icons on primary
    onSecondary = PureWhite,       // Text/icons on secondary
    onTertiary = DarkGray,         // Text/icons on tertiary
    onBackground = OffWhite,       // Text/icons on background
    onSurface = OffWhite,          // Text/icons on surface
    surfaceVariant = DeepPurple,   // Alternative surface color
    onSurfaceVariant = PaleLavender // Text on alternative surface
)

@Composable
fun SkyCastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

/*
package com.example.skycast.ui.theme

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime


private val LightColorScheme = lightColorScheme(
    primary = DeepOcean,          // Primary brand color
    secondary = BluePeriwinkle,      // Secondary brand color
    tertiary = CoralBlush,           // Accent color
    background = BluePeriwinkle,     // Your preferred background
    surface = CloudWhite,            // Surface/card color
    onPrimary = CloudWhite,          // Text/icons on primary
    onSecondary = MidnightBlue,      // Text/icons on secondary
    onTertiary = MidnightBlue,       // Text/icons on tertiary
    onBackground = MidnightBlue,     // Text/icons on background
    onSurface = MidnightBlue,


    */
/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    *//*

)
@Composable
fun SkyCastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> LightColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

*/
/*@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SkyCastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val currentHour = LocalTime.now().hour
    val isMorning = currentHour in 6..18

    val defaultColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val backgroundColor = if (isMorning) MorningBackground else NightBackground
    val textColor = if (isMorning) MorningText else NightText

    Log.d("TAG", "Applied Background: $backgroundColor")

    MaterialTheme(
        colorScheme = defaultColorScheme,
        typography = Typography,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor) // Ensure time-based background is applied
        ) {
            content()
        }
    }
}*/

