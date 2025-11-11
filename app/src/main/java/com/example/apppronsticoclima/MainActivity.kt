package com.example.apppronsticoclima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apppronsticoclima.ui.theme.AppPronósticoClimaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AppPronósticoClimaTheme{
                val navController = rememberNavController()

                NavHost(navController, startDestination = "VistaBuscador") {
                    composable ("VistaBuscador" ) { VistaBuscador(navController) }
                    composable ( "VistaClima" ) { VistaClima(navController) }
                }
            }
        }
    }
}