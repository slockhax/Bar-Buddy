package com.example.barbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IngredientsBodyContent() {
    LazyColumn {
        item {
            CollapsibleCard(
                title = "Spirits",
                content = { BuildCheckboxGrid(Dao.getSpirits()) })}
        item {
            CollapsibleCard(
                title = "Cordials",
                content = { BuildCheckboxGrid(Dao.getCordials()) })}
        item {
            CollapsibleCard(
                title = "Mixers",
                content = { BuildCheckboxGrid(Dao.getMixers()) })}
        item {
            CollapsibleCard(
                title = "Garnishes",
                content = { BuildCheckboxGrid(Dao.getGarnishes()) })}
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
fun BuildIndividualCheckbox(title: String, state: Boolean){
    val checkedState = remember { mutableStateOf(state) }
    Row(
        modifier = Modifier.width(180.dp),
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
fun CollapsibleCard(title: String, content: @Composable () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ){
        Text(
            modifier = Modifier
                .padding(start=20.dp,top=10.dp,bottom=5.dp)
            ,
            text = title,
            fontSize = 20.sp
        )
        content()
    }
}

fun updateCheck(title:String,value:Boolean){
    Dao.updateInventory(title, value.toString())
}