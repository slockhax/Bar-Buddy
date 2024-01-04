package com.example.barbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController

@Composable
fun RecipesBodyContent(navController: NavController){
    Column {
        BuildFilterRow()
        LazyColumn{
            item{
                Dao.getAllRecipes().forEach { recipe ->
                    var tags = recipe.method
                    recipe.descriptors?.let { tags += ", $it" }
                    Divider()
                    RecipeListItem(
                        navController,
                        itemName = recipe.name,
                        tags = tags
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildFilterChip(name: String, dropdown: Boolean = true) {
    val isSelected by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    FilterChip(
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.background,
            selectedLabelColor = MaterialTheme.colorScheme.onBackground
        ),
        selected = false,
        onClick = { showDialog = !showDialog },
        label = { Text(name) },
        trailingIcon = { if (dropdown) Icon(Icons.Rounded.ArrowDropDown, contentDescription = null) }
    )
    if (showDialog) {
//        FilterPopup2(name, onDismiss = {showDialog = false})
        Text("")
    }
}

@Composable
fun FilterPopup2(name: String = "Title", onDismiss: () -> Unit) {
    Popup {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onDismiss() }
            )
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {  }
                )
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors (
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
        ) {
            Text (
                text = name,
                modifier = Modifier
                    .padding(top = 15.dp, start = 15.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            Card (
                modifier = Modifier
                    .padding(20.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                val filters = listOf("Boozy","Citrusy","Frozen","Fruity","Sweet","Tart","Warm")

                LazyColumn {
                    items(filters) { filterItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White)
                                .padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val isChecked = remember { mutableStateOf(false) }
                            Checkbox(
                                checked = isChecked.value,
                                onCheckedChange = { isChecked.value = !isChecked.value }
                            )
                            Text(
                                modifier = Modifier.padding(start = 5.dp),
                                text = filterItem
                            )
                        }
                        Divider()
                    }
                }
            }
        }
    }
    }
}

fun clickRecipe(navController: NavController, name:String){
    navController.navigate("recipeDetail/$name")
}

@Composable
fun RecipeListItem(navController: NavController, itemName:String, tags: String){
    val onClickAction = { clickRecipe(navController, itemName) }
    val inStock = Dao.getRecipeByName(itemName).craftable == 1
    ListItem(
        modifier = Modifier.clickable(onClick = onClickAction),
        headlineContent = { Text(itemName) },
        supportingContent = { Text(tags) },
        leadingContent = {
            Icon(
                imageVector = if (inStock) Icons.Rounded.Check else Icons.Rounded.Close,
                tint = if (inStock) Color.Green else Color.Red,
                contentDescription = "test"
            )
        }
    )
}

@Composable
fun BuildFilterRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
            BuildFilterChip("Filters")
            BuildFilterChip("Ingredients")
            BuildFilterChip("Craftable Only", dropdown = false)
    }
}