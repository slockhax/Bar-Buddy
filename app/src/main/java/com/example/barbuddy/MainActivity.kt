package com.example.barbuddy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme

val Dao = DatabaseProvider.db.IngredientDao()

class MainActivity : ComponentActivity() {
    override fun onDestroy() {
        DatabaseProvider.db.close()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                MainScaffold()
            }
        }
    }
}

@Composable
fun MainScaffold() {

    val navController = rememberNavController()
    val navBarColors = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = MaterialTheme.colorScheme.primary,
    )

    Scaffold(
        topBar = { BuildTopAppBar(navController) },
        bottomBar = {
            // TODO : (bug) back button doesn't update nav bar selection
            var selectedNavItem by remember { mutableIntStateOf(0) }
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                NavigationBarItem(selected = selectedNavItem == 0, onClick = {
                    navController.navigate("Recipes")
                    selectedNavItem = 0
                }, label = { Text("Recipes") }, icon = {
                    Icon(
                        Icons.Rounded.Assignment, contentDescription = "test image"
                    )
                }, colors = navBarColors
                )
                NavigationBarItem(selected = selectedNavItem == 1, onClick = {
                    navController.navigate("Ingredients")
                    selectedNavItem = 1
                }, label = { Text("Ingredients") }, icon = {
                    Icon(
                        Icons.Rounded.ListAlt, contentDescription = "test image"
                    )
                }, colors = navBarColors
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
        ) {
            NavHost(navController = navController, startDestination = "Recipes") {
                composable("Ingredients") { IngredientsBodyContent() }
                composable("Recipes") { RecipesBodyContent(navController) }
                composable("I'm Feeling Lucky") { RandomBodyContent(navController) }
                composable("Add New Recipe") { AddNewRecipe() }
                composable("Add New Ingredient") { AddNewIngredient() }
                composable("recipeDetail/{recipeName}") { backStackEntry ->
                    backStackEntry.arguments?.getString("recipeName")
                        ?.let { RecipeDetailScreen(it) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuildTopAppBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val recipeName = navBackStackEntry?.arguments?.getString("recipeName")
    val topAppColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
    )
    when (currentRoute) {
        "Ingredients", "Recipes" -> TopAppBar(
            title = { Text("Home Bar Buddy") },
            colors = topAppColors,
            actions = {
                IconButton(onClick = { navController.navigate("I'm Feeling Lucky") }) {
                    Icon(Icons.Outlined.Casino, "I'm Feeling Lucky")
                }
                IconButton(onClick = { addNewItem(currentRoute, navController) }) {
                    Icon(Icons.Filled.Add, "Add new item")
                }
            })
        "recipeDetail/{recipeName}" -> TopAppBar(
            title = {
                if (recipeName != null) { Text(recipeName) }
            },
            colors = topAppColors,
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back Button")
                }
            },
        )
        "Add New Recipe" -> TopAppBar(
            title = { Text(currentRoute.toString()) },
            colors = topAppColors,
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back Button")
                }
            }
        )
        else -> TopAppBar(
//            title = { Text(currentRoute.toString()) },
            title = { Text("else") },
            colors = topAppColors,
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back Button")
                }
            },
        )
    }
}

// TODO : move new item into separate file
fun addNewItem(type: String?, navController: NavController) {
    if (type != null) {
        when (type) {
            "Recipes" -> navController.navigate("Add New Recipe")
            "Ingredients" -> navController.navigate("Add New Ingredient")
        }
    }
}

fun saveRecipe(data: List<CONST.RecipeIngredientRow>){
    Log.e("FAB","Start")
    data.forEach { item ->
        Log.e("FAB", item.toString())
    }
    Log.e("FAB","End")
}
@Composable
fun AddNewRecipe() {
    val ingredientRows = remember {mutableStateListOf(
        CONST.RecipeIngredientRow("","",""),
        CONST.RecipeIngredientRow("","","")
    )}

    Box(modifier = Modifier.fillMaxSize()) {
        ExtendedFloatingActionButton(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 20.dp, end = 15.dp),
            onClick = { saveRecipe(ingredientRows) }
        ) {
            Icon(Icons.Filled.Save, "Save Button")
            Text("  Save Recipe")
        }
    }
    Column {
        var recipeName by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text("Name") },
            singleLine = true
        )
        Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
            NewItemDropDown(
                listItems = CONST.MethodOptions.keys.toList(),
                dataValue = "",
                onValueChange = {},
                weight = 1f,
                defaultValue = "",
                helper = "Method"
            )
            NewItemDropDown(
                listItems = CONST.IceOptions.keys.toList(),
                dataValue = "",
                onValueChange = {},
                weight = 1f,
                defaultValue = "",
                helper = "Ice"
            )
        }
        Divider(modifier = Modifier.padding(top = 20.dp, bottom = 20.dp))
        Text(
            modifier = Modifier.padding(start = 10.dp), text = "Ingredients"
        )

        ingredientRows.forEach { data ->
            NewRecipeIngredientRow(data)
        }

        // TODO : move button so it doesn't interfere with FAB
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Rounded.AddCircle,
                modifier = Modifier
                    .clickable { ingredientRows += CONST.RecipeIngredientRow("","","") }
                    .align(Alignment.CenterEnd)
                    .padding(10.dp),
                tint = colorResource(R.color.in_stock),
                contentDescription = "Add new row")
        }
    }
}


@Composable
fun NewRecipeIngredientRow(data: CONST.RecipeIngredientRow) {
    val ingredients = Dao.getAllIngredients()
    var volume by remember { mutableStateOf(data.volume) }
    var measurement by remember { mutableStateOf(data.measurement) }
    var ingredientName by remember { mutableStateOf(data.item) }

    Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp)) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .padding(top = 0.dp, start = 5.dp, end = 5.dp),
            value = volume,
            onValueChange = { volume = it },
            label = { },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        NewItemDropDown(
            listItems = listOf("ml", "barspoons", "dash"),
            dataValue = measurement,
            onValueChange = {measurement = it},
            weight = 1f,
            defaultValue = "ml",
            helper = ""
        )
        NewItemDropDown(
            listItems = ingredients.map { it.name },
            dataValue = ingredientName,
            onValueChange = {ingredientName = it},
            weight = 2f,
            defaultValue = "",
            helper = "Ingredient"
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.NewItemDropDown(
    listItems: List<String>,
    dataValue: String,
    onValueChange: (String) -> Unit,
    weight: Float,
    defaultValue: String,
    helper: String
) {
    var isExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(modifier = Modifier.weight(weight),
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }) {
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 0.dp, start = 5.dp, end = 5.dp)
                .menuAnchor(),
            value = dataValue,
            onValueChange = onValueChange,
            label = { Text(helper) },
            readOnly = true,
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
        )
        DropdownMenu(
            modifier = Modifier
                .exposedDropdownSize(true)
                .padding(start = 10.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            offset = DpOffset(x = 5.dp, y = 0.dp)
        ) {
            listItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        isExpanded = false
                        onValueChange(item)
                    },
                )
            }
        }
    }
}


@Composable
fun AddNewIngredient() {

}