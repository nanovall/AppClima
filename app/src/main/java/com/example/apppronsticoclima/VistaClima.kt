package com.example.apppronsticoclima

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController




@Composable
fun VistaClima(navController: NavController)
{

    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Button(
            onClick = {
                navController.navigate("VistaBuscador")
            }
        ) {
            Text(text = "Ir a la vista del Buscador")
        }

    }






}




@Preview(showBackground = true)
@Composable
fun VistaClimaPreview() {
    VistaClima(navController = rememberNavController())
}