package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PlayfairDisplayFamily
import com.example.ui.theme.PrimaryGold
import com.example.ui.theme.PrimaryGreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startEntryAnimation by remember { mutableStateOf(false) }

    // 0f to 1f fade + scale in 1 second (1000ms)
    val entryScale by animateFloatAsState(
        targetValue = if (startEntryAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "splash_entry_scale"
    )
    val entryAlpha by animateFloatAsState(
        targetValue = if (startEntryAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "splash_entry_alpha"
    )

    // Lottie-style subtle continuous pulse on logo + expanding golden aura ring
    val infiniteTransition = rememberInfiniteTransition(label = "lottie_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val haloRadiusMultiplier by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.28f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_radius"
    )
    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_alpha"
    )

    LaunchedEffect(Unit) {
        startEntryAnimation = true
        delay(3000)
        onSplashFinished()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PrimaryGreen) // #0F4C0F
            .padding(24.dp)
            .testTag("splash_screen_root")
    ) {
        // Center Logo + Titles
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(entryScale)
                .alpha(entryAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(156.dp)
            ) {
                // Lottie-style pulsing golden ring aura
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = PrimaryGold.copy(alpha = haloAlpha),
                        radius = (60.dp.toPx()) * haloRadiusMultiplier,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }

                // 120dp Logo circle with 3dp gold border
                Box(modifier = Modifier.scale(pulseScale)) {
                    AlHadidLogoBadge(
                        size = 120.dp,
                        borderWidth = 3.dp,
                        showLabelInside = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Al Hadid Academy",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PlayfairDisplayFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("splash_title_text")
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Nasirabad Jatlam, Azad Kashmir",
                color = PrimaryGold, // #C6A700
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("splash_subtitle_text")
            )
        }

        // Bottom Center Developer Branding
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .alpha(entryAlpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "Made with ❤️ by",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "ANAS DEVELOPRS",
                color = PrimaryGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("splash_developer_brand")
            )
            Text(
                text = "2026",
                color = Color(0xFFB0BEC5),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AlHadidLogoBadge(
    size: Dp,
    borderWidth: Dp = 2.dp,
    showLabelInside: Boolean = true,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .border(BorderStroke(borderWidth, PrimaryGold), CircleShape),
        shape = CircleShape,
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .border(BorderStroke(1.dp, PrimaryGreen.copy(alpha = 0.45f)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mosque,
                    contentDescription = "Al Hadid Academy Logo",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(if (showLabelInside) size * 0.44f else size * 0.62f)
                )
                if (showLabelInside && size >= 64.dp) {
                    Text(
                        text = "Al Hadid",
                        color = PrimaryGreen,
                        fontSize = if (size >= 100.dp) 13.sp else 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = PlayfairDisplayFamily,
                        lineHeight = if (size >= 100.dp) 14.sp else 10.sp
                    )
                    Text(
                        text = "Academy",
                        color = PrimaryGold,
                        fontSize = if (size >= 100.dp) 9.sp else 7.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = if (size >= 100.dp) 10.sp else 8.sp
                    )
                }
            }
        }
    }
}
