package com.example.barbuddy

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RandomBodyContent(navController: NavController){
    // TODO : actually make this
    Box(modifier = Modifier.fillMaxSize())
    {
        Text(
            modifier = Modifier.padding(50.dp),
            text="I'm Feeling Lucky!!!")
    }
    Body()
}

fun saveRecipe2(data: List<CONST.RecipeIngredientRow>){
    Log.e("FAB","Start")
    data.forEach { item ->
        Log.e("FAB", item.toString())
    }
    Log.e("FAB","End")
}

@Composable
fun Body() {
    val ingredientRows2 = remember {
        mutableStateListOf(
            CONST.RecipeIngredientRow("", "", ""),
            CONST.RecipeIngredientRow("", "", ""),
        )
    }

    Column { // Using Column to layout rows vertically
        ingredientRows2.forEachIndexed { index, item ->
            BuildRow(
                data = item,
                onValueChange = { volume, measurement, ingredient ->
                    ingredientRows2[index] = CONST.RecipeIngredientRow(volume,measurement,ingredient)
                }
            )
        }
        Button(onClick = { saveRecipe2(ingredientRows2) }){
            Text("Click Me")
        }
    }
}

@Composable
fun BuildRow(data: CONST.RecipeIngredientRow, onValueChange: (String,String,String) -> Unit) {
    var name by remember { mutableStateOf(data.volume) }
    var measurement by remember { mutableStateOf(data.measurement) }
    var item by remember { mutableStateOf(data.item) }

    BuildTextBox(value = name, onValueChange = { name = it })
    BuildTextBox(value = measurement, onValueChange = { measurement = it })
    BuildTextBox(value = item, onValueChange = { item = it })
}

@Composable
fun BuildTextBox(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Label") },
        readOnly = false, // Set to true if you want it to be non-editable
        singleLine = true
    )
}