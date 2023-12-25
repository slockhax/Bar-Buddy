package com.example.barbuddy

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CraftableBodyContent(){
    Column {
        for (item in Dao.getSpirits()) {
            val name = item.name
            val available = item.available
            Text("$name : $available")
        }
    }
}