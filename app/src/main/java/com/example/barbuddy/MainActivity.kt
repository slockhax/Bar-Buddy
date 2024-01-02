@file:Suppress("UNUSED_VARIABLE")

package com.example.barbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.ListAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme

val Dao = DatabaseProvider.db.IngredientDao()

class MainActivity : ComponentActivity() {
    override fun onDestroy(){
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
    val navColor = NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.primaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unselectedIconColor =MaterialTheme.colorScheme.onSurface,
        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = MaterialTheme.colorScheme.primary,
    )

    Scaffold (
        topBar = { BuildTopAppBar(navController) },
        bottomBar = {
            var selectedNavItem by remember { mutableIntStateOf(0) }
            NavigationBar (
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                contentColor = MaterialTheme.colorScheme.onSurface
            ){
                NavigationBarItem(
                    selected = selectedNavItem == 0,
                    onClick = {
                        navController.navigate("Recipes")
                        selectedNavItem = 0
                    },
                    label = { Text("Recipes") },
                    icon = {
                        Icon(Icons.Rounded.Assignment,
                            contentDescription = "test image")
                    },
                    colors = navColor
                )
                NavigationBarItem(
                    selected = selectedNavItem == 1,
                    onClick = {
                        navController.navigate("Ingredients")
                        selectedNavItem = 1
                    },
                    label = { Text("Ingredients") },
                    icon = { Icon(
                        Icons.Rounded.ListAlt,
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
            NavHost(navController = navController, startDestination = "Recipes"){
                composable("Ingredients") { IngredientsBodyContent() }
                composable("Craftable") { CraftableBodyContent() }
                composable("Recipes") { RecipesBodyContent(navController) }
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
    if (recipeName == null) {
        CenterAlignedTopAppBar(
            title = { Text("Home Bar Buddy") },
            colors = topAppColors,
        )
    } else {
        TopAppBar(
            title = { Text((recipeName)) },
            colors = topAppColors,
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
        )
    }
}
