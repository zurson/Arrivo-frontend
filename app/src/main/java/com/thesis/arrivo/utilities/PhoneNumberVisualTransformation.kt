package com.thesis.arrivo.utilities

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private const val mask = Settings.PHONE_NUMBER_MASK
private const val maskLetter = Settings.PHONE_NUMBER_MASK_LETTER

class PhoneVisualTransformation : VisualTransformation {

    private val maxLength = mask.count { it == maskLetter }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskLetter) {
                    val nextDigitIndex = mask.indexOf(maskLetter, maskIndex)
                    if (nextDigitIndex == -1) break
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(mask, maskLetter))
    }
}

private class PhoneOffsetMapper(val mask: String, val numberChar: Char) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        var noneDigitCount = 0
        var i = 0
        while (i < offset + noneDigitCount && i < mask.length) {
            if (mask[i++] != numberChar) noneDigitCount++
        }

        return (offset + noneDigitCount).coerceAtMost(mask.length)
    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}

