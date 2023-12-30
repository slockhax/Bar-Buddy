package com.example.barbuddy

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Blender
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocalBar
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val MethodOptions = mapOf(
    "Blended" to "Mix in a blender until smooth",
    "Stirred" to "Mix item in serving glass",
    "Shaken" to "Shake in cocktail shaker then pour into glass",
)

val IceOptions = mapOf(
    "Strained" to "Strain out ice when pouring into glass",
    "Over Ice" to "Ice should be in serving glass",
    "Blended Ice" to "Blend with ice",
    "No Ice" to "No ice required",
    "Hot Drink" to "No ice required"
)

val MethodIcon = mapOf(
    "Blended" to Icons.Rounded.Blender,
    "Shaken" to Icons.Rounded.Science,
    "Stirred" to Icons.Rounded.LocalBar,
)

@Composable
fun RecipeDetailScreen(name: String) {
    val recipe: Recipes = Dao.getRecipeByName(name)
    Column {
        SectionMixingMethod(recipe.method, recipe.iceMethod)
        SectionIngredients(
            recipe.ingredients,
            recipe.garnish)
        SectionInstructions()
    }
}

@Composable
fun SectionMixingMethod(method: String, iceMethod: String) {
    Column {
        BuildTitleText(title = "Mixing Method")
        BuildMethodItem(content = method)
        BuildMethodItem(content = iceMethod)
    }
}

@Composable
fun SectionIngredients(liquor: String, garnish: String?) {
    val liquorList = liquor.split(", ")
    val garnishList = garnish?.split(", ")
    Column {
        BuildTitleText(title = "Ingredients")
        for (item in liquorList) { BuildIngredientItem(item) }
        if (garnishList != null) {
            for (item in garnishList) { BuildIngredientItem(item, garnish = true) }
        }
    }
}

@Composable
fun SectionInstructions() {
    Column {
//        BuildTitleText(title = "Instructions")

    }
}


@Composable
fun BuildTitleText(title: String){
    Text(
        text = title,
        modifier = Modifier.padding(start = 20.dp, top = 30.dp, bottom = 10.dp),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun BuildMethodItem(content: String){
    ListItem (
        modifier = Modifier.padding(start = 20.dp),
        leadingContent = {
            Icon(
                imageVector = MethodIcon[content]?: Icons.Rounded.AcUnit,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        supportingContent = {
            (MethodOptions[content]?: IceOptions[content])?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Composable
fun BuildIngredientItem(itemName: String, garnish: Boolean = false){
    var nameOnly = itemName
    if (!garnish) {
        nameOnly = itemName.split(" ").drop(2).joinToString(" ")
        Log.e("ASDF", nameOnly)
    }

    val inStock = Dao.getIngredientByName(nameOnly).available.toBoolean()
    ListItem (
        modifier = Modifier.padding(start = 20.dp),
        leadingContent = {
            Icon(
                imageVector = if (inStock) Icons.Rounded.Check else Icons.Rounded.Close,
                contentDescription = null,
                tint = if (inStock) Color.Green else Color.Red
            )
        },
        headlineContent = {
            Text(
                text = itemName,
                style = MaterialTheme.typography.bodyMedium
            )
        },
    )
}

@Composable
fun BuildMethodChip(method: String) {
    AssistChip(
        modifier = Modifier.padding(start=10.dp, end=10.dp),
        onClick = { /*TODO*/ },
        label = { Text(method) },
        border = AssistChipDefaults.assistChipBorder(borderWidth = 0.dp),
        leadingIcon = {
            Icon(
                getMethodIcon(method),
                contentDescription = "Mixing Method Icon"
            )
        }
    )
}