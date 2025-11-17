package com.example.apppronsticoclima.Presentation.Clima

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores base del diseño
private val colorPrimary = Color(0xFF4A90E2)
private val colorCardBackground = Color(0xFFFFFFFF)
private val colorScreenBackground = Color(0xFFF0F4F8)
private val colorTextPrimary = Color(0xFF333333)
private val colorTextSecondary = Color(0xFF666666)
private val colorGridBackground = Color(0xFFF7F9FC)


@Composable
fun ClimaView(
    state: ClimaEstado,
    onAction: (ClimaIntencion) -> Unit
) {
    Scaffold(
        topBar = {
            // Solo mostramos la barra superior si el estado es Exitoso
            if (state is ClimaEstado.Exitoso) {
                WeatherTopBar(
                    cityName = state.ciudad,
                    onBackClick = { onAction(ClimaIntencion.VolverAtras) }
                )
            }
        },
        bottomBar = {
            // Solo mostramos el botón si el estado es Exitoso
            if (state is ClimaEstado.Exitoso) {
                ShareButton(
                    onClick = { onAction(ClimaIntencion.CompartirPronostico) }
                )
            }
        },
        containerColor = colorScreenBackground
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // Manejamos los 4 estados posibles
            when (state) {
                is ClimaEstado.Cargando -> {
                    CircularProgressIndicator()
                }
                is ClimaEstado.Error -> {
                    Text(
                        text = state.mensaje,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                is ClimaEstado.Exitoso -> {
                    // Contenido principal de la pantalla
                    WeatherContent(state = state)
                }
                is ClimaEstado.Vacio -> {
                    Text(text = "No hay datos")
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(state: ClimaEstado.Exitoso) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CurrentWeatherCard(state = state)
        }
        item {
            AdditionalInfoGrid(state = state)
        }
        item {
            Text(
                text = "Pronóstico de 5 días",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorTextPrimary
            )
        }
        items(state.pronostico) { forecastDay ->
            ForecastItem(day = forecastDay)
        }
    }
}

@Composable
fun WeatherTopBar(cityName: String, onBackClick: () -> Unit) {
    Surface(
        color = colorPrimary,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            }
            Text(
                text = cityName,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
private fun CurrentWeatherCard(state: ClimaEstado.Exitoso) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Text(
                text = state.temperatura,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = colorTextPrimary
            )
            Text(
                text = state.descripcion,
                fontSize = 20.sp,
                color = colorTextPrimary
            )
            Text(
                text = state.sensacionTermica,
                fontSize = 16.sp,
                color = colorTextSecondary
            )
            Divider(
                modifier = Modifier.padding(vertical = 15.dp),
                color = colorScreenBackground
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                InfoColumn(
                    label = "Humedad",
                    value = state.humedad
                )
                InfoColumn(
                    label = "Viento",
                    value = state.viento
                )
            }
        }
    }
}

@Composable
private fun InfoColumn(label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = colorTextSecondary
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorTextPrimary
            )
        }
    }
}

@Composable
private fun AdditionalInfoGrid(state: ClimaEstado.Exitoso) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Información Adicional",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = colorTextPrimary
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoBox(
                    label = "Visibilidad",
                    value = state.visibilidad,
                    description = state.visibilidadDesc
                )
                InfoBox(
                    label = "Nubosidad",
                    value = state.nubosidad,
                    description = state.nubosidadDesc
                )
                InfoBox(
                    label = "Temp. Máxima",
                    value = state.tempMaxHoy,
                    description = "Máxima de hoy"
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoBox(
                    label = "Presión",
                    value = state.presion,
                    description = state.presionDesc
                )
                InfoBox(
                    label = "Sol",
                    value = state.amanecer,
                    description = state.atardecer,
                    isSunTime = true
                )
                InfoBox(
                    label = "Temp. Mínima",
                    value = state.tempMinHoy,
                    description = "Mínima de hoy"
                )
            }
        }
    }
}


@Composable
private fun InfoBox(
    label: String,
    value: String,
    description: String,
    isSunTime: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorGridBackground)
            .padding(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // No hay ícono
                Text(
                    text = label,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTextSecondary
                )
            }
            if (isSunTime) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Subida:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorTextPrimary)
                    Spacer(Modifier.width(4.dp))
                    Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorTextPrimary)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Bajada:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorTextPrimary)
                    Spacer(Modifier.width(4.dp))
                    Text(description, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorTextPrimary)
                }
            } else {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTextPrimary
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = colorTextSecondary
                )
            }
        }
    }
}

@Composable
private fun ForecastItem(day: ForecastDay) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Text(
                text = day.iconEmoji,
                fontSize = 32.sp,
                modifier = Modifier
                    .size(40.dp)
                    .background(colorGridBackground, RoundedCornerShape(8.dp)),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${day.dia}, ${day.fecha}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorTextPrimary
                )
                Text(
                    text = day.descripcion,
                    fontSize = 14.sp,
                    color = colorTextSecondary
                )
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = day.tempMax,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorTextPrimary
            )
            Text(
                text = day.tempMin,
                fontSize = 14.sp,
                color = colorTextSecondary
            )
        }
    }
}

@Composable
private fun ShareButton(onClick: () -> Unit) {
    Surface(
        shadowElevation = 8.dp,
        color = colorScreenBackground
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Text(
                text = "Compartir pronóstico",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}