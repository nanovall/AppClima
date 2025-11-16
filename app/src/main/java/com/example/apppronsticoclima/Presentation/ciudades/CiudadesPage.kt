package com.example.apppronsticoclima.Presentation.ciudades

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apppronsticoclima.Repository.RepositorioApi
import com.example.apppronsticoclima.Repository.UserPreferencesImpl

@Composable
fun CiudadesPage(
    navController:  NavHostController
) {
    val context = LocalContext.current

    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = RepositorioApi(),
            navController = navController,
            userPreferences = UserPreferencesImpl(context)
        )
    )
    CiudadesView(
        state = viewModel.uiState,
        onAction = { intencion ->
            viewModel.ejecutar(intencion)
        }
    )
}