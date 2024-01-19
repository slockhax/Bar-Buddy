package com.example.barbuddy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Block
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun RecipeDetailScreen(name: String) {
    val recipe: Recipes = Dao.getRecipeByName(name)
    Column {
        SectionMixingMethod(recipe.method, recipe.iceMethod)
        SectionIngredients(recipe.ingredients)
        recipe.garnish?.let { SectionIngredients(it, isGarnish = true) }
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
fun SectionIngredients(ingredients: String, isGarnish: Boolean = false) {
    val ingredientsList = ingredients.split(" - ")
    Column {
        BuildTitleText(title = if (!isGarnish) "Ingredients" else "Garnish")
        for (item in ingredientsList) { BuildIngredientItem(item, isGarnish) }
    }
}


// TODO : add section for instructions / notes
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
                imageVector = CONST.MethodIcon[content]?: Icons.Rounded.AcUnit,
                contentDescription = null
            )
        },
        headlineContent = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            (CONST.MethodOptions[content]?: CONST.IceOptions[content])?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}

@Suppress("VARIABLE_WITH_REDUNDANT_INITIALIZER")
@Composable
fun BuildIngredientItem(itemName: String, garnish: Boolean = false){
    var nameOnly = itemName
    var inStock = "garnish"
    if (!garnish) {
        nameOnly = itemName.split(" ").drop(2).joinToString(" ")
        inStock = Dao.getIngredientByName(nameOnly).available
    }
    ListItem (
        modifier = Modifier.padding(start = 20.dp),
        leadingContent = {
            Icon(
                imageVector = when (inStock) {
                    "garnish" -> ImageVector.vectorResource(R.drawable.rounded_nutrition_24)
                    "true" -> Icons.Rounded.CheckCircle
                    "false" -> Icons.Rounded.Block
                    else -> Icons.Rounded.Help
                },
                contentDescription = null,
                tint = when (inStock) {
                    "garnish" -> Color.Black
                    "true" -> colorResource(R.color.in_stock)
                    "false" -> colorResource(R.color.out_of_stock)
                    else -> Color.Black
                },
            )
        },
        headlineContent = {
            Text(
                text = itemName,
                style = MaterialTheme.typography.bodyLarge
            )
        },
    )
}