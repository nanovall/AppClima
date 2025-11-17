package com.example.apppronsticoclima.Router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.apppronsticoclima.Presentation.Clima.ClimaPage
import com.example.apppronsticoclima.Presentation.ciudades.CiudadesPage
import com.example.apppronsticoclima.Repository.UserPreferencesImpl

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Startup"
    ) {
        composable("Startup") {
            StartupScreen(navController=navController)
        }
        composable("VistaBuscador") { CiudadesPage(navController) }
        composable(
            route = "VistaClima/{lat}/{lon}/{nombre}",
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lon") { type = NavType.FloatType },
                navArgument("nombre") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getFloat("lat") ?: 0f
            val lon = backStackEntry.arguments?.getFloat("lon") ?: 0f
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""

            ClimaPage(
                navController = navController,
                lat = lat,
                lon = lon,
                nombre = nombre
            )
        }
    }
}

@Composable
private fun StartupScreen(navController: NavHostController) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val userPreferences = UserPreferencesImpl(context)
        val ciudadGuardada = userPreferences.obtenerCiudadSeleccionada()

        if (ciudadGuardada != null) {
            // Hay ciudad guardada → vamos directo a Clima
            navController.navigate(
                "VistaClima/${ciudadGuardada.lat}/${ciudadGuardada.lon}/${ciudadGuardada.name}"
            ) {
                popUpTo("Startup") { inclusive = true } // sacamos Startup del backstack
            }
        } else {
            // No hay ciudad → mostramos el buscador de ciudades
            navController.navigate("VistaBuscador") {
                popUpTo("Startup") { inclusive = true }
            }
        }
    }
}