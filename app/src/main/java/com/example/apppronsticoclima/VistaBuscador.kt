package com.example.apppronsticoclima

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun VistaBuscador(navController: NavController) {


    Column(
        modifier = Modifier.fillMaxSize().padding(top = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text("Seleccionar Ciudad", fontSize = 15.sp,)
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(top = 35.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(text = "ACA VA EL BUSCADOR")
    }





    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Button(onClick = {
            //--- Valores de prueba ---
            val lat = -34.61f
            val lon = -58.38f
            val nombre = "Buenos Aires "
            //-------------------------
            navController.navigate("VistaClima/$lat/$lon/$nombre")
        }) {
            Text(text = "Ir a Buenos Aires (Prueba)")
        }

    }



}

@Preview(showBackground = true)
@Composable
fun VistaBuscadorPreview() {
    VistaBuscador(navController = rememberNavController())
}