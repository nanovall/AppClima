package com.example.apppronsticoclima.Presentation.ciudades

import com.example.apppronsticoclima.Repository.modelos.Ciudad
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val colorScreenBackground = Color(0xFFF0F4F8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CiudadesView(
    state: CiudadesEstado,
    onAction: (CiudadesIntencion) -> Unit
) {
    var searchTerm by remember { mutableStateOf("") }
    val isLocating = state is CiudadesEstado.Cargando
    val locationError = state is CiudadesEstado.Error

    Scaffold(
        topBar = {},
        containerColor = colorScreenBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Color(0xFF64B5F6), Color(0xFF42A5F5))
                    )
                )
        ) {
            // Header con buscador y geolocalización
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(24.dp)
            ) {
                Text(
                    text = "App Clima",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Busca una ciudad para empezar",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )

                // Buscador (Input equivalent)
                OutlinedTextField(
                    value = searchTerm,
                    onValueChange = {
                        searchTerm = it
                        if (it.isNotBlank()) {
                            onAction(CiudadesIntencion.Buscar(it))
                        } else {
                            onAction(CiudadesIntencion.SetDefault)
                        }
                    },
                    label = { Text("Introduce una ciudad...", color = Color.White.copy(alpha = 0.8f)) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = Color.White.copy(alpha = 0.8f)) },
                    trailingIcon = {
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = { PlainTooltip { Text("Usar mi ubicación") } },
                            state = rememberTooltipState()
                        ) {
                            IconButton(onClick = { onAction(CiudadesIntencion.UsarGeolocalizacion) }) {
                                Icon(Icons.Default.LocationOn, contentDescription = "Usar mi ubicación", tint = Color.White)
                            }
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedContainerColor = Color.White.copy(alpha = 0.2f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                LocationError(
                    Error = locationError,
                    state = state,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEF5350).copy(alpha = 0.2f))
                        .padding(12.dp),
                )
            }

            // Lista de ciudades
            ContenedorListaCiudades(
                onAction = onAction,
                state = state,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
                    .padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun LocationError(
    Error: Boolean,
    state: CiudadesEstado,
    modifier: Modifier
) {
    if (Error) {
        val errorMsg = (state as CiudadesEstado.Error).mensaje
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Info, contentDescription = "Error", tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(errorMsg, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun ContenedorListaCiudades(
    onAction: (CiudadesIntencion) -> Unit,
    state: CiudadesEstado,
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        when (state) {
            CiudadesEstado.Cargando -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF64B5F6))
                }
            }

            is CiudadesEstado.Error -> {
                Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No se encontraron ciudades", color = Color.Gray, fontSize = 16.sp)
                }
            }

            is CiudadesEstado.Resultado -> ListaDeCiudades(state.ciudades, onAction, modifier = Modifier.fillMaxSize())
            CiudadesEstado.Vacio -> Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No se encontraron ciudades", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ListaDeCiudades(
    ciudades: List<Ciudad>,
    onAction: (CiudadesIntencion) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = ciudades) {
            Card(
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                onClick = { onAction(CiudadesIntencion.Seleccionar(it)) },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = it.name, color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        Text(text = it.country, color = Color.Gray, fontSize = 14.sp)
                    }
                    Icon(Icons.Filled.LocationOn, contentDescription = "Pin", tint = Color.LightGray)
                }
                Divider(color = Color.LightGray.copy(alpha = 1.5f), thickness = 0.75.dp, modifier = Modifier.padding(horizontal = 24.dp))
            }
        }
    }
}
