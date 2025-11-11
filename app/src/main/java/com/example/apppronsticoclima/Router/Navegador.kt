package com.example.apppronsticoclima.Router

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apppronsticoclima.VistaBuscador
import com.example.apppronsticoclima.VistaClima


@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "VistaBuscador"
    ) {
        composable("VistaBuscador") { VistaBuscador(navController) }
        composable("VistaClima") { VistaClima(navController) }
    }
}