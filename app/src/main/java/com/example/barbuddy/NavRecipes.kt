package com.example.barbuddy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Blender
import androidx.compose.material.icons.rounded.Liquor
import androidx.compose.material.icons.rounded.LocalBar
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController

@Composable
fun RecipesBodyContent(navController: NavController){
    Column {
        BuildFilterRow(listOf("Method","Descriptor","Ingredient"))
        LazyColumn{
            item{
                Dao.getAllRecipes().forEach { recipe ->
                    val tags = collateTags(recipe)
                    Divider()
                    RecipeListItem(
                        navController,
                        title = recipe.name,
                        method = recipe.method,
                        tags = tags
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildFilterChip(name: String) {
    val isSelected by remember { mutableStateOf(false) }
    val showDialog by remember { mutableStateOf(false) }
    FilterChip(
//        modifier = Modifier.padding(start=0.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        selected = isSelected,
        onClick = { showDialog != showDialog },
        label = { Text(name) },
        trailingIcon = { Icon(Icons.Rounded.ArrowDropDown, contentDescription = null) }
    )
    if (showDialog) {
        FilterPopup()
    }
}

fun collateTags(recipe: Recipes): String{
    var tags = recipe.method + ", "
    if (recipe.boozy == 1) tags += "Boozy, "
    if (recipe.citrusy == 1) tags += "Citrusy, "
    if (recipe.frozen == 1) tags += "Frozen, "
    if (recipe.fruity == 1) tags += "Fruity, "
    if (recipe.sweet == 1) tags += "Sweet, "
    if (recipe.tart == 1) tags += "Tart, "
    if (recipe.warm == 1) tags += "Warm, "
    tags = tags.dropLast(2)
    return tags
}

@Composable
fun FilterPopup(){
    Popup (
        alignment = Alignment.BottomStart
    ){
        Text("hello world")
    }
}

fun clickRecipe(navController: NavController, name:String){
    navController.navigate("recipeDetail/$name")
}

@Composable
fun RecipeListItem(navController: NavController,title:String, method:String, tags: String){
    val onClickAction = { clickRecipe(navController, title) }
    ListItem(
        modifier = Modifier.clickable(onClick = onClickAction),
        headlineContent = { Text(title) },
        supportingContent = { Text(tags) },
        leadingContent = {
            Icon(
                getMethodIcon(method),
                contentDescription = "test"
            )
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BuildFilterRow(filters: List<String>) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        for (filter in filters) {
            BuildFilterChip(name = filter)
        }
    }
}

fun getMethodIcon(method: String): ImageVector {
    if (method == "Stirred") { return Icons.Rounded.LocalBar }
    if (method == "Shaken") { return Icons.Rounded.Science }
    if (method == "Blended") { return Icons.Rounded.Blender }
    return Icons.Rounded.Liquor
}