package com.example.barbuddy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.*
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScaffold()
        }
    }
}

// TODO : Update to a dictionary
val AlcoholIngredients = listOf(
    "rum",
    "vodka",
    "gin",
    "sloe gin",
    "tequila",
    "mezcal",
    "bourbon",
    "whiskey",
    "Irish whiskey",
    "scotch",
    "sweet vermouth",
    "dry vermouth",
    "sake",
    "brandy",
    "cognac",
    "aquavit",
    "absinthe"
)
val LiqueursIngredients = listOf(
    "amaretto",
    "coffee liquor (Kahlua)",
    "cream liquor (Baileys)",
    "amaro",
    "Aperol",
    "Benedictine",
    "Campari",
    "Chambord",
    "Chartreuse",
    "creme de cacao",
    "creme de cassis",
    "creme de menthe",
    "Drambuie",
    "Jagermeiser",
    "Galliano",
    "limoncello",
    "peppermint schnapps",
    "St Germain",
)
val MixerIngredients = listOf(
    "sour mix",
    "lime juice",
    "lemon juice",
    "cranberry juice",
    "tonic water",
    "club soda",
    "simple syrup",
    "grenadine",
    "ginger beer",
    "bitters"
)
val GarnishIngredients = listOf(
    "rimming sugar",
    "maraschino cherry",
    "lime",
    "lemon",
    "orange slice"
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    Scaffold (
        topBar = {
            TopAppBar(
                 colors = TopAppBarDefaults.largeTopAppBarColors(
                     containerColor = MaterialTheme.colorScheme.primaryContainer,
                     titleContentColor = MaterialTheme.colorScheme.primary
                 ),
                 title = {
                     Text(
                         text = "Bar Buddy",
                     )
                }
            )
        },
        content = { BodyContent() },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    label = { Text("Ingredients") },
                    icon = { Icon(
                        Icons.Rounded.ListAlt,
                        contentDescription = "test image")
                    }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    label = { Text("Drinks") },
                    icon = {
                        Icon(Icons.Rounded.Liquor,
                        contentDescription = "test image")
                    }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    label = { Text("Recipes") },
                    icon = {
                        Icon(Icons.Rounded.Assignment,
                            contentDescription = "test image")
                    }
                )
            }
        }
    )
}

@Composable
fun BodyContent() {
    LazyColumn(
        modifier = Modifier
            .padding(top = 70.dp, bottom= 80.dp)
    ){
        item {CollapsibleCard(title = "Spirits", content = { ItemContent(AlcoholIngredients) })}
        item {CollapsibleCard(title = "Cordials", content = { ItemContent(LiqueursIngredients) })}
        item {CollapsibleCard(title = "Mixers", content = { ItemContent(MixerIngredients) })}
        item {CollapsibleCard(title = "Garnishes", content = { ItemContent(GarnishIngredients) })}
    }
}

@Composable
fun ItemContent(ingredients: List<String>){
    Column {
        ingredients.chunked(3).forEach { chunk ->
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
            ) {
                for (item in chunk) {CheckBoxContent(item) }
            }
        }
    }
}


@Composable
fun CheckBoxContent(title: String){
        Row(
            modifier = Modifier.width(130.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = {}
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
            .fillMaxWidth()
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