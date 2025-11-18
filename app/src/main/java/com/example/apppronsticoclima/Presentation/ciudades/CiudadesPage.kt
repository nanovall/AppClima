package com.example.apppronsticoclima.Presentation.ciudades

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apppronsticoclima.Repository.RepositorioApi
import com.example.apppronsticoclima.Repository.UserPreferencesImpl
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission") // La advertencia se suprime porque el permiso se verifica antes de la llamada.
@Composable
fun CiudadesPage(
    navController:  NavHostController
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val viewModel : CiudadesViewModel = viewModel(
        factory = CiudadesViewModelFactory(
            repositorio = RepositorioApi(),
            userPreferences = UserPreferencesImpl(context)
        )
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, obtener la ubicación.
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        // Una vez obtenida la ubicación, enviamos la intención al ViewModel para navegar.
                        viewModel.ejecutar(
                            CiudadesIntencion.NavegarAClimaConUbicacion(
                                lat = location.latitude.toFloat(),
                                lon = location.longitude.toFloat()
                            )
                        )
                    } else {
                        // No se pudo obtener la última ubicación conocida.
                        viewModel.ejecutar(CiudadesIntencion.FalloDeGeolocalizacion)
                    }
                }.addOnFailureListener {
                    // Fallo al intentar obtener la ubicación.
                    viewModel.ejecutar(CiudadesIntencion.FalloDeGeolocalizacion)
                }
            } else {
                // Permiso denegado.
                viewModel.ejecutar(CiudadesIntencion.FalloDeGeolocalizacion)
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.efecto.collect { efecto ->
            when (efecto) {
                is CiudadesEfecto.NavegarAClima -> {
                    navController.navigate("VistaClima/${efecto.lat}/${efecto.lon}/${efecto.nombre}")
                }
                is CiudadesEfecto.PedirPermisoUbicacion -> {
                    // Cuando el ViewModel lo solicita, se lanza el diálogo de permiso.
                    locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    }

    CiudadesView(
        state = viewModel.uiState,
        onAction = { intencion ->
            viewModel.ejecutar(intencion)
        }
    )
}