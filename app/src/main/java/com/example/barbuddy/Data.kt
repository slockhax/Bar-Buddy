package com.example.barbuddy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Blender
import androidx.compose.material.icons.rounded.LocalBar
import androidx.compose.material.icons.rounded.Science

object CONST {

    val MethodOptions = mapOf(
        "Blended" to "Mix in a blender until smooth",
        "Stirred" to "Mix item in serving glass",
        "Shaken" to "Shake in cocktail shaker then pour into glass",
    )

    val IceOptions = mapOf(
        "Strained" to "Strain out ice when pouring into glass",
        "Over Ice" to "Serve with ice in glass",
        "Blended Ice" to "Blend with ice",
        "No Ice" to "No ice required",
        "Hot Drink" to "No ice required"
    )

    val MethodIcon = mapOf(
        "Blended" to Icons.Rounded.Blender,
        "Shaken" to Icons.Rounded.Science,
        "Stirred" to Icons.Rounded.LocalBar,
    )

    data class RecipeIngredientRow(
        var volume: String,
        var measurement: String,
        var item: String
    )
}

