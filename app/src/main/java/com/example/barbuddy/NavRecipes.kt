package com.example.barbuddy

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController





class YourViewModel(private val dao: IngredientDao) : ViewModel() {
    private var descriptor: String = ""
    private var ingredient: String = ""
    var recipesLiveData = MutableLiveData<List<Recipes>>().apply{
        value = dao.getAllRecipes()
    }


    fun updateFilter(newDescriptor: String){
        descriptor = newDescriptor
        val data = dao.getFilteredRecipes(descriptor,ingredient)
        recipesLiveData.postValue(data)
        Log.e("ViewModel",descriptor)
        Log.e("ViewModel",data.toString())
        Log.e("ViewModel",this.toString())
    }
}

@Composable
fun RecipesBodyContent(
    navController: NavController,
    viewModel: YourViewModel = YourViewModel(Dao)
){
    val recipesList by viewModel.recipesLiveData.observeAsState()
    Column {
        BuildFilterRow()
        LazyColumn {
            items(recipesList.orEmpty()) { recipe ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildFilterChip(name: String, filters: List<String>? = null) {
    var showDialog by remember { mutableStateOf(false) }
    FilterChip(
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = Modifier.padding(start=5.dp, end = 5.dp),
        selected = false,
        onClick = { showDialog = !showDialog },
        label = { Text(name) },
        trailingIcon = { if (filters != null) Icon(Icons.Rounded.ArrowDropDown, contentDescription = null) }
    )
    if (showDialog) {
        FilterPopup(name, filters, onDismiss = {showDialog = false})
    }
}

@Composable
fun FilterPopup(
    name: String,
    filters:List<String>?,
    onDismiss: () -> Unit,
    viewModel: YourViewModel = YourViewModel(Dao)
) {
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
                    onClick = { }
                )
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors (
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = name,
                    modifier = Modifier
                        .padding(top = 15.dp, start = 15.dp),
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Clear Filters",
                    style = MaterialTheme.typography.titleSmall
                )
            }
            Card (
                modifier = Modifier
                    .padding(20.dp)
                    .heightIn(max = 400.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                filters?.let {
                LazyColumn {
                    items(it) { filterItem ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White)
                                .padding(15.dp)
                                .clickable(onClick = {
                                    viewModel.updateFilter(filterItem)
                                    Log.e("Popup", viewModel.toString())
                                    onDismiss()
                                }),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 5.dp)
                                ,
                                text = filterItem,
                            )
                        }
                        Divider()
                    }
                }}
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
        horizontalArrangement = Arrangement.Center
    ){
        val filtersList = Dao.getFiltersList().value.split(", ")
        val ingredientsList = mutableListOf<String>()
        Dao.getAllSpirits().forEach{ item -> ingredientsList.add(item.name) }
        BuildFilterChip("Filters", filtersList)
        BuildFilterChip("Ingredients", ingredientsList)
        BuildFilterChip("Craftable Only")
    }
}