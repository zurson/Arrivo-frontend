package com.thesis.arrivo.components.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

fun Modifier.strikethroughX(enabled: Boolean): Modifier {
    return if (enabled) {
        this.drawBehind {
            val startOffset1 = Offset(0f, 0f)
            val endOffset1 = Offset(size.width, size.height)

            drawLine(
                color = Color.Gray,
                start = startOffset1,
                end = endOffset1,
                strokeWidth = 5f
            )

            val startOffset2 = Offset(size.width, 0f)
            val endOffset2 = Offset(0f, size.height)

            drawLine(
                color = Color.Gray,
                start = startOffset2,
                end = endOffset2,
                strokeWidth = 5f
            )
        }
    } else {
        this
    }
}
