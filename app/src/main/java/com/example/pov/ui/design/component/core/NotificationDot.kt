package com.example.pov.ui.design.component.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp

fun Modifier.notificationDot(): Modifier = composed {
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    drawWithContent {
        drawContent()
        drawCircle(
            tertiaryColor,
            radius = 5.dp.toPx(),
            center = center + Offset(64.dp.toPx() * 45f, 32.dp.toPx() * -.45f - 5.dp.toPx())
        )
    }
}