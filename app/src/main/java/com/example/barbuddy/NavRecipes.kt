package com.example.barbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val filters = listOf("Mixed","Shaken","Blended","Sweet","Fruity")

@Composable
fun RecipesBodyContent(){
    Column{
        BuildFilterRow()
        LazyColumn{
            Dao.getAllRecipes().chunked(2).forEach { chunk ->
                item{
                    Row(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        for (recipe in chunk) {
                            RecipeCard(
                                title = recipe.name,
                                content = { RecipeContent(recipe.tags, recipe.ingredients) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.RecipeCard(title: String, content: @Composable () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(300.dp)
            .weight(1f)
            .padding(5.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
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

@Composable
fun RecipeContent(tags: String?, ingredients: String){
    Column (
        modifier = Modifier
            .padding(10.dp)
    ) {
        if (tags != null) {
            val newTags = tags.replace(" ", "\n")
            Text("\nTags:\n$newTags")
        }
        Text("\nIngredients:\n")
        Text(ingredients.replace(", ","\n"))
    }
}

@Composable
fun BuildFilterRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        for (filter in filters) {
            BuildFilterChip(name = filter)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildFilterChip(name: String) {
    var isSelected by remember { mutableStateOf(false) }

    FilterChip(
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        selected = isSelected,
        onClick = { isSelected = !isSelected },
        label = { Text(name) },
    )
}

