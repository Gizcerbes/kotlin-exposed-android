package com.uogames.kotlin_exposed_test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uogames.kotlin_exposed_test.db.TestTable
import com.uogames.kotlin_exposed_test.db.delete
import com.uogames.kotlin_exposed_test.db.getAll
import com.uogames.kotlin_exposed_test.db.initDB
import com.uogames.kotlin_exposed_test.db.save
import com.uogames.kotlin_exposed_test.ui.theme.KotlinexposedtestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDB("${filesDir.absolutePath}/database.db")
        setContent {
            KotlinexposedtestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }

    }
}

@Composable
fun Greeting() {

    var text by remember { mutableStateOf("") }
    var data by remember { mutableStateOf<List<TestTable.Test>>(emptyList()) }
    var refresh by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = refresh) {
        data = TestTable.getAll()
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(13.dp)
    ) {

        item {
            TextField(value = text, onValueChange = { text = it })
        }

        item {
            Button(
                onClick = {
                    TestTable.save(TestTable.Test(text = text))
                    refresh++
                    text = ""
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text(text = "Save")
            }
        }

        items(count = data.size) {
            val item = data[it]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(50.dp)
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = item.text)
                    Button(onClick = {
                        TestTable.delete(item)
                        refresh++
                    }) {
                        Text(text = "delete")
                    }
                }
            }

        }


    }

}