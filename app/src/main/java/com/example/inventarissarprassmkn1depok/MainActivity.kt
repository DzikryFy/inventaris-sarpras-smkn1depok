package com.example.inventarissarprassmkn1depok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.inventarissarprassmkn1depok.data.local.AppDatabase
import com.example.inventarissarprassmkn1depok.ui.navigation.AppNavHost
import com.example.inventarissarprassmkn1depok.ui.theme.InventarisSarprasSMKN1DepokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getInstance(applicationContext)

        setContent {
            InventarisSarprasSMKN1DepokTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavHost(database = database)
                }
            }
        }
    }
}
