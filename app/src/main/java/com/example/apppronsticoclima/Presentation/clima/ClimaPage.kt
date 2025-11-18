package com.example.apppronsticoclima.Presentation.Clima

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.apppronsticoclima.Repository.RepositorioApi

@Composable
fun ClimaPage(
    navController: NavHostController,
    lat: Float,
    lon: Float,
    nombre: String
) {
    val context = LocalContext.current
    val viewModel: ClimaViewModel = viewModel(
        factory = ClimaViewModelFactory(
            repositorio = RepositorioApi(),
            lat = lat,
            lon = lon,
            nombre = nombre
        )
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.ejecutar(ClimaIntencion.CargarClima)

        viewModel.efecto.collect { efecto ->
            when (efecto) {
                is ClimaEfecto.NavegarAtras -> {
                    // Comprueba si hay una pantalla anterior en la pila de navegación.
                    if (navController.previousBackStackEntry != null) {
                        // Si la hay, simplemente volvemos.
                        navController.popBackStack()
                    } else {
                        // Si no la hay, navegamos a la pantalla de búsqueda como nueva raíz.
                        navController.navigate("VistaBuscador") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
                is ClimaEfecto.Compartir -> {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, efecto.texto)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(
                        sendIntent,
                        "Compartir pronóstico con..."
                    )
                    context.startActivity(shareIntent)
                }
            }
        }
    }

    ClimaView(
        state = viewModel.uiState,
        onAction = { intencion ->
            viewModel.ejecutar(intencion)
        }
    )
}
