package com.example.apppronsticoclima.Presentation.Clima

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.apppronsticoclima.Repository.RepositorioApi

@Composable
fun ClimaPage(
    navController: NavHostController,
    lat: Float,
    lon: Float,
    nombre: String
) {
    //  Obtenemos el Contexto de Android.
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
    }

    val state = viewModel.uiState
    ClimaView(
        state = state,
        onAction = { intencion ->
            when (intencion) {
                ClimaIntencion.VolverAtras -> {
                    navController.popBackStack()
                }
                ClimaIntencion.CompartirPronostico -> {
                    //  Verificamos que el estado tenga datos
                    if (state is ClimaEstado.Exitoso) {
                        //  Creamos el texto que queremos compartir
                        val textoParaCompartir =
                            crearTextoParaCompartir(state)
                        //  Creamos la intención de tipo ENVIAR
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, textoParaCompartir)
                            type = "text/plain"
                        }
                        //  Creamos el menú "chooser" que se abre
                        val shareIntent = Intent.createChooser(
                            sendIntent,
                            "Compartir pronóstico con..."
                        )
                        //  Lanzamos la actividad de compartir
                        context.startActivity(shareIntent)
                    }
                }
                else -> {
                    viewModel.ejecutar(intencion)
                }
            }
        }
    )
}

private fun crearTextoParaCompartir(estado: ClimaEstado.Exitoso): String {
    val builder = StringBuilder()
    builder.append("¡Mira el pronóstico para ${estado.ciudad}!\n\n")
    builder.append("Ahora: ${estado.descripcion}, ${estado.temperatura}\n\n")
    builder.append("Pronóstico para los próximos 5 días:\n")

    estado.pronostico.forEach { dia ->
        builder.append(
            "• ${dia.dia}, ${dia.fecha}: ${dia.descripcion} (${dia.tempMax} / ${dia.tempMin})\n"
        )
    }

    return builder.toString()
}