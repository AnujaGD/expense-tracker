package com.sunset.expensetracker
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import kotlinx.coroutines.Dispatchers

import java.net.URL
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ApiListActivity()
        }
    }
}


@Composable
fun ApiListActivity() {
    val context = LocalContext.current
    var data by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Make the API call when the Composable is first launched
    LaunchedEffect(true) {
        var response: String? =null;// URL("http://localhost:8080/api/expenses").readText()
         withContext(Dispatchers.IO) {  // ✅ Runs in a background thread
            response = URL("http://10.0.2.2:8080/api/expenses").readText()
        }
        val jsonArray = JSONArray(response)
        val expensesList = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val description = jsonObject.getString("description")
            val amount = jsonObject.getDouble("amount")
            expensesList.add("$description: $$amount")
        }
        data =expensesList;
    }

    // Function to fetch data from the API
    fun fetchData() {
        isLoading = true
        try {
            // Replace this with your actual API URL
            val response = URL("http://10.0.2.2:8080/api/expenses").readText()

        } catch (e: Exception) {
            Log.e("ApiListActivity", "Error fetching data", e)
        } finally {
            isLoading = false
        }
    }
    // Column layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Data List",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

//       // Button to refresh the list
//        Button(
//            onClick = { LaunchedEffect(true) { fetchData() } },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text(text = if (isLoading) "Loading..." else "Refresh Data")
//        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display list of data
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(data) { item ->
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}


