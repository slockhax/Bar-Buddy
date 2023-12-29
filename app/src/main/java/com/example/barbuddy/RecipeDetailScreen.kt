package com.example.barbuddy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AcUnit
import androidx.compose.material.icons.rounded.Blender
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RecipeDetailScreen(name: String) {
    SectionMixingMethod()

}
@Preview
@Composable
fun SectionMixingMethod(){
    Column {
        BuildTitleText(title = "Mixing Method")
        BuildBulletItem(icon = Icons.Rounded.Blender, content = "Blended")
        BuildBulletItem(icon = Icons.Rounded.AcUnit, content = "Over Ice")
    }
}

@Composable
fun SectionGlassware(){

}

@Composable
fun SectionIngredients(){

}

@Composable
fun SectionInstructions(){

}


@Composable
fun BuildTitleText(title: String){
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
fun BuildBulletItem(icon: ImageVector, content: String){
    Row{
        Icon(
            icon,
            modifier = Modifier.padding(start=20.dp,top=10.dp),
            contentDescription = null
        )
        Text(
            modifier = Modifier.padding(start=10.dp,top=10.dp),
            text = content,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun BuildMethodChip(method: String) {
    AssistChip(
        modifier = Modifier.padding(start=10.dp, end=10.dp),
        onClick = { /*TODO*/ },
        label = { Text(method) },
        border = AssistChipDefaults.assistChipBorder(borderWidth = 0.dp),
        leadingIcon = {
            Icon(
                getMethodIcon(method),
                contentDescription = "Mixing Method Icon"
            )
        }
    )
}