package com.example.citymet.extensions

import java.text.Normalizer
import java.util.regex.Pattern

fun String.withoutAccent(): String {
    val nfdNormalizedString = Normalizer.normalize(this, Normalizer.Form.NFD)
    val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
    return pattern.matcher(nfdNormalizedString).replaceAll("")
}