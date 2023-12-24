package com.example.barbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.Liquor
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.barbuddy.ui.theme.AppTheme

val Dao = DatabaseProvider.db.IngredientDao()
val spiritsData = Dao.getSpirits()
val cordialsData = Dao.getCordials()
val mixersData = Dao.getMixers()
val garnishesData = Dao.getGarnishes()
val allRecipes = Dao.getAllRecipes()


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainScaffold()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    val navController = rememberNavController()

    val navColor = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedIconColor =MaterialTheme.colorScheme.onSurface,
        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = MaterialTheme.colorScheme.primary,
    )

    Scaffold (
        topBar = {
            TopAppBar(
                 colors = TopAppBarDefaults.largeTopAppBarColors(
                     containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                     titleContentColor = MaterialTheme.colorScheme.onSurface
                 ),
                 title = { Text("Bar Buddy") }
            )
        },
        bottomBar = {
            var selectedNavItem by remember { mutableIntStateOf(2) }
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                contentColor = MaterialTheme.colorScheme.onSurface
            ){
                NavigationBarItem(
                    selected = selectedNavItem == 0,
                    onClick = {
                        navController.navigate("Ingredients")
                        selectedNavItem = 0
                    },
                    label = { Text("Ingredients") },
                    icon = { Icon(
                        Icons.Rounded.ListAlt,
                        contentDescription = "test image")
                    },
                    colors = navColor
                )
                NavigationBarItem(
                    selected = selectedNavItem == 1,
                    onClick = {
                        navController.navigate("Craftable")
                        selectedNavItem = 1
                    },
                    label = { Text("Craftable") },
                    icon = {
                        Icon(Icons.Rounded.Liquor,
                        contentDescription = "test image")
                    },
                    colors = navColor
                )
                NavigationBarItem(
                    selected = selectedNavItem == 2,
                    onClick = {
                        navController.navigate("All Recipes")
                        selectedNavItem = 2
                    },
                    label = { Text("All Recipes") },
                    icon = {
                        Icon(Icons.Rounded.Assignment,
                            contentDescription = "test image")
                    },
                    colors = navColor
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            NavHost(navController = navController, startDestination = "All Recipes"){
                composable("Ingredients"){
                    IngredientsBodyContent()
                }
                composable("Craftable"){
                    CraftableBodyContent()
                }
                composable("All Recipes"){
                    RecipesBodyContent()
                }
            }
        }
    }
}

@Composable
fun CraftableBodyContent(){

}

@Composable
fun IngredientsBodyContent() {
    LazyColumn {
        item {
            CollapsibleCard(
                title = "Spirits",
                content = { BuildCheckboxGrid(spiritsData) })}
        item {
            CollapsibleCard(
                title = "Cordials",
                content = { BuildCheckboxGrid(cordialsData) })}
        item {
            CollapsibleCard(
                title = "Mixers",
                content = { BuildCheckboxGrid(mixersData) })}
        item {
            CollapsibleCard(
                title = "Garnishes",
                content = { BuildCheckboxGrid(garnishesData) })}
    }
}

@Composable
fun RecipesBodyContent(){
    Column(){
        BuildFilterRow()
        LazyColumn{
            allRecipes.chunked(2).forEach { chunk ->
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
fun RecipeContent(tags: String?, ingredients: String){
    Column (
        modifier = Modifier.padding(10.dp)
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
fun BuildCheckboxGrid(ingredients: List<CocktailIngredients>){
    Column {
        ingredients.chunked(3).forEach { chunk ->
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
    val checkedState = remember { mutableStateOf(state)}
    Row(
        modifier = Modifier.width(130.dp),
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

fun updateCheck(title:String,value:Boolean){
    Dao.updateInventory(title, value.toString())
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

@Composable
fun RowScope.RecipeCard(title: String, content: @Composable () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f)
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

val filters = listOf("Mixed","Shaken","Blended","Sweet","Fruity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildFilterChip(name: String) {
    var isSelected by remember { mutableStateOf(false) }

    FilterChip(
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.background,
            labelColor = MaterialTheme.colorScheme.onBackground,
            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
//            iconColor = MaterialTheme.colorScheme.primary,
        ),
        selected = isSelected,
        onClick = { isSelected = !isSelected },
        label = { Text(name) },
//        leadingIcon = {
//            if (isSelected) {
//                Icon(
//                    Icons.Rounded.Done,
//                    contentDescription = null,
//                    modifier = Modifier.size(FilterChipDefaults.IconSize)
//                )
//            }
//        }
    )
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
