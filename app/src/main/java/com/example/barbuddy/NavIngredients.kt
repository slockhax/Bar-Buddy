package com.example.barbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IngredientsBodyContent() {
    LazyColumn {
        item { IngredientSection("Alcohol, Core", Dao.getCoreSpirits()) }
        item { IngredientSection("Alcohol, Misc.", Dao.getNonCoreSpirits()) }
        item { IngredientSection("Mixers", Dao.getMixers()) }
        item { IngredientSection("Garnishes", Dao.getGarnishes()) }
    }
}

@Composable
fun BuildCheckboxGrid(ingredients: List<CocktailIngredients>){
    Column {
        ingredients.chunked(2).forEach { chunk ->
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
            ) {
                for (item in chunk) {
                    BuildIndividualCheckbox( item.name, item.available.toBoolean() )
                }
            }
        }
    }
}

@Composable
fun RowScope.BuildIndividualCheckbox(title: String, state: Boolean){
    val checkedState = remember { mutableStateOf(state) }
    Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                updateCheck(title,checkedState.value) }
        )
        Text(
            modifier = Modifier.padding(),
            text = title,
        )
    }
}

@Composable
fun IngredientSection(title: String, items: List<CocktailIngredients>){
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top= 20.dp, bottom = 20.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ){
        Column{
            Text(
                text = title,
                modifier = Modifier.padding(start = 20.dp, bottom = 10.dp),
                style = MaterialTheme.typography.titleLarge,
            )
            BuildCheckboxGrid(items)
        }
    }
    Divider(thickness = 1.dp)
}

fun updateCheck(itemName:String,value:Boolean){
    Dao.updateInventory(itemName, value.toString())
    val effectedRecipes = Dao.getRecipesByIngredient(itemName)
    for (recipe in effectedRecipes) {
        val ingredientsList = recipe.ingredients.split(" - ")
        var isCraftable = 1
        for (item in ingredientsList) {
            val nameOnly = item.split(" ").drop(2).joinToString(" ")
            val isAvailable = Dao.getIngredientByName(nameOnly).available
            if (!isAvailable.toBoolean()) {
                isCraftable = 0
                break
            }
        }
        Dao.updateCraftableRecipe(recipe.name, isCraftable)
    }
}