package com.example.apppronsticoclima.Presentation.startup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apppronsticoclima.Repository.UserPreferencesImpl

@Composable
fun StartupPage(navController: NavHostController) {
    val context = LocalContext.current
    val viewModel: StartupViewModel = viewModel(
        factory = StartupViewModelFactory(
            userPreferences = UserPreferencesImpl(context)
        )
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.decidirRutaInicial()
        viewModel.efecto.collect { efecto ->
            when (efecto) {
                is StartupEfecto.NavegarACiudades -> {
                    navController.navigate("VistaBuscador") {
                        popUpTo("Startup") { inclusive = true }
                    }
                }
                is StartupEfecto.NavegarAClima -> {
                    navController.navigate(
                        "VistaClima/${efecto.lat}/${efecto.lon}/${efecto.nombre}"
                    ) {
                        popUpTo("Startup") { inclusive = true } 
                    }
                }
            }
        }
    }
}